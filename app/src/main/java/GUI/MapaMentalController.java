package GUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import logica.GerenciadorDeRevisao;
import modelo.MapNode;
import modelo.MentalMap;

public class MapaMentalController {

    @FXML private TextField txtTituloMapa;
    @FXML private TextField txtDiscMapa;
    @FXML private Pane      paneDesenho;

    private GerenciadorDeRevisao gerenciador;
    private MentalMap            mapaAtual;

    // Mapeia cada MapNode para seu círculo visual no Pane
    private final Map<MapNode, StackPane> nodeViews = new HashMap<>();

    // Estado do drag
    private double dragOffsetX, dragOffsetY;

    // Nó selecionado para adicionar filho ou remover
    private MapNode nodeSelecionado = null;
    private StackPane viewSelecionada = null;

    // ── Cores do tema SMITHE ──────────────────────────────────
    private static final String COR_RAIZ     = "#4a7c45";
    private static final String COR_FILHO    = "#1e2e1b";
    private static final String COR_BORDA    = "#8fd685";
    private static final String COR_SEL      = "#8fd685";
    private static final String COR_TEXTO    = "#c8e6c4";
    private static final String COR_LINHA    = "#2d3d2a";

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciador = gerenciador;
    }

    // ─────────────────────────────────────────────────────────
    // CRIAR NOVO MAPA
    // ─────────────────────────────────────────────────────────

    @FXML
    public void criarNovoMapa() {
        String titulo     = txtTituloMapa.getText().trim();
        String disciplina = txtDiscMapa.getText().trim();
        if (titulo.isEmpty() || disciplina.isEmpty()) return;

        mapaAtual = new MentalMap(titulo, disciplina);
        nodeViews.clear();
        paneDesenho.getChildren().clear();
        nodeSelecionado = null;

        // Posiciona raiz no centro do painel
        double cx = paneDesenho.getPrefWidth()  / 2;
        double cy = paneDesenho.getPrefHeight() / 2;
        mapaAtual.getRoot().setLayoutX(cx);
        mapaAtual.getRoot().setLayoutY(cy);

        renderizarNo(mapaAtual.getRoot(), true);

        txtTituloMapa.clear();
        txtDiscMapa.clear();
    }

    // ─────────────────────────────────────────────────────────
    // ADICIONAR NÓ FILHO (via diálogo)
    // ─────────────────────────────────────────────────────────

    @FXML
    public void adicionarFilho() {
        if (nodeSelecionado == null) {
            mostrarAviso("Selecione um nó", "Clique em um nó do mapa para selecioná-lo primeiro.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Novo nó");
        dialog.setHeaderText("Adicionar filho a: " + nodeSelecionado.getName());
        dialog.setContentText("Nome do novo nó:");
        estilizarDialog(dialog.getDialogPane());

        dialog.showAndWait().ifPresent(nome -> {
            if (nome.isBlank()) return;

            MapNode novoNo = mapaAtual.adicionarNo(nodeSelecionado.getName(), nome);
            if (novoNo == null) return;

            // Posição: deslocada em relação ao pai
            double angle  = Math.random() * 2 * Math.PI;
            double radius = 120 + Math.random() * 60;
            novoNo.setLayoutX(nodeSelecionado.getLayoutX() + radius * Math.cos(angle));
            novoNo.setLayoutY(nodeSelecionado.getLayoutY() + radius * Math.sin(angle));

            // Linha pai → filho (adicionada atrás)
            desenharLinha(nodeSelecionado, novoNo);
            renderizarNo(novoNo, false);
        });
    }

    // ─────────────────────────────────────────────────────────
    // REMOVER NÓ SELECIONADO
    // ─────────────────────────────────────────────────────────

    @FXML
    public void removerNo() {
        if (nodeSelecionado == null) {
            mostrarAviso("Selecione um nó", "Clique em um nó para selecioná-lo primeiro.");
            return;
        }
        if (nodeSelecionado == mapaAtual.getRoot()) {
            mostrarAviso("Operação inválida", "Não é possível remover o nó raiz.");
            return;
        }

        // Remove todos os nós da subárvore visualmente
        List<MapNode> subarvore = nodeSelecionado.allNodes();
        for (MapNode no : subarvore) {
            StackPane view = nodeViews.remove(no);
            if (view != null) paneDesenho.getChildren().remove(view);
        }

        mapaAtual.removerNo(nodeSelecionado.getName());
        nodeSelecionado = null;
        viewSelecionada = null;

        // Redesenha as linhas (mais simples que gerenciar individualmente)
        redesenharLinhas();
    }

    // ─────────────────────────────────────────────────────────
    // REVISÃO ESPAÇADA DO MAPA
    // ─────────────────────────────────────────────────────────

    @FXML
    public void revisarMapa() {
        if (mapaAtual == null) {
            mostrarAviso("Nenhum mapa", "Crie um mapa mental primeiro.");
            return;
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Revisão do Mapa");
        dialog.setHeaderText("Como você domina este mapa?\n\"" + mapaAtual.getTitulo() + "\"");
        estilizarDialog(dialog.getDialogPane());

        ButtonType btnType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(btnType);

        // Botões de nota
        HBox notas = new HBox(8);
        notas.setStyle("-fx-padding: 16 0 0 0;");
        ToggleGroup grupo = new ToggleGroup();

        String[] labels  = {"1 ❌", "2 ⏳", "3 ☕", "4 👍", "5 🔥"};
        String[] estilos = {
            "-fx-background-color:#2d1a1a;-fx-text-fill:#e88;",
            "-fx-background-color:#2a2214;-fx-text-fill:#c8a84b;",
            "-fx-background-color:#1e2318;-fx-text-fill:#8aad84;",
            "-fx-background-color:#1a2a18;-fx-text-fill:#8fd685;",
            "-fx-background-color:#1e2e1b;-fx-text-fill:#8fd685;-fx-font-weight:bold;"
        };

        for (int i = 0; i < 5; i++) {
            ToggleButton btn = new ToggleButton(labels[i]);
            btn.setUserData(i + 1);
            btn.setToggleGroup(grupo);
            btn.setStyle(estilos[i] + "-fx-background-radius:8;-fx-border-radius:8;"
                + "-fx-border-color:#2d3d2a;-fx-cursor:hand;-fx-pref-width:62;-fx-pref-height:36;");
            notas.getChildren().add(btn);
        }
        dialog.getDialogPane().setContent(notas);

        dialog.setResultConverter(btn -> {
            if (btn == btnType && grupo.getSelectedToggle() != null) {
                return (Integer) grupo.getSelectedToggle().getUserData();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(nota -> {
            mapaAtual.revisar(nota);
            String msg = "Próxima revisão em " + mapaAtual.getIntervalosDias() + " dia(s).";
            Alert info = new Alert(Alert.AlertType.INFORMATION, msg);
            info.setTitle("Revisão registrada!");
            info.setHeaderText(null);
            estilizarDialog(info.getDialogPane());
            info.show();
        });
    }

    // ─────────────────────────────────────────────────────────
    // RENDERIZAÇÃO
    // ─────────────────────────────────────────────────────────

    private void renderizarNo(MapNode no, boolean isRaiz) {
        // Círculo de fundo
        Circle circulo = new Circle(isRaiz ? 42 : 34);
        circulo.setFill(Color.web(isRaiz ? COR_RAIZ : COR_FILHO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 2 : 1.5);

        // Texto
        Text texto = new Text(no.getName());
        texto.setFill(Color.web(COR_TEXTO));
        texto.setFont(Font.font("System", isRaiz ? FontWeight.BOLD : FontWeight.NORMAL, isRaiz ? 13 : 11));
        texto.setWrappingWidth(isRaiz ? 72 : 56);
        texto.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        StackPane view = new StackPane(circulo, texto);
        view.setLayoutX(no.getLayoutX() - circulo.getRadius());
        view.setLayoutY(no.getLayoutY() - circulo.getRadius());

        // ── Drag ──
        view.setOnMousePressed(e -> {
            dragOffsetX = e.getSceneX() - view.getLayoutX();
            dragOffsetY = e.getSceneY() - view.getLayoutY();
            selecionarNo(no, view, circulo);
            e.consume();
        });
        view.setOnMouseDragged(e -> {
            double nx = e.getSceneX() - dragOffsetX;
            double ny = e.getSceneY() - dragOffsetY;
            view.setLayoutX(nx);
            view.setLayoutY(ny);
            no.setLayoutX(nx + circulo.getRadius());
            no.setLayoutY(ny + circulo.getRadius());
            redesenharLinhas();
            e.consume();
        });

        nodeViews.put(no, view);
        paneDesenho.getChildren().add(view);
    }

    private void selecionarNo(MapNode no, StackPane view, Circle circulo) {
        // Deseleciona anterior
        if (viewSelecionada != null) {
            Circle ant = (Circle) viewSelecionada.getChildren().get(0);
            ant.setStroke(Color.web(COR_BORDA));
            ant.setStrokeWidth(no == mapaAtual.getRoot() ? 2 : 1.5);
        }
        nodeSelecionado = no;
        viewSelecionada = view;
        circulo.setStroke(Color.web(COR_SEL));
        circulo.setStrokeWidth(3);
    }

    private void desenharLinha(MapNode pai, MapNode filho) {
        Line linha = new Line(
            pai.getLayoutX(), pai.getLayoutY(),
            filho.getLayoutX(), filho.getLayoutY()
        );
        linha.setStroke(Color.web(COR_LINHA));
        linha.setStrokeWidth(1.5);
        // Linhas ficam atrás dos nós
        paneDesenho.getChildren().add(0, linha);
    }

    private void redesenharLinhas() {
        // Remove todas as linhas e redesenha
        paneDesenho.getChildren().removeIf(n -> n instanceof Line);
        if (mapaAtual == null) return;
        desenharLinhasRecursivo(mapaAtual.getRoot());
    }

    private void desenharLinhasRecursivo(MapNode pai) {
        for (MapNode filho : pai.getChildren()) {
            Line linha = new Line(
                pai.getLayoutX(), pai.getLayoutY(),
                filho.getLayoutX(), filho.getLayoutY()
            );
            linha.setStroke(Color.web(COR_LINHA));
            linha.setStrokeWidth(1.5);
            paneDesenho.getChildren().add(0, linha);
            desenharLinhasRecursivo(filho);
        }
    }

    // ─────────────────────────────────────────────────────────
    // UTILITÁRIOS
    // ─────────────────────────────────────────────────────────

    private void mostrarAviso(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        estilizarDialog(alert.getDialogPane());
        alert.show();
    }

    private void estilizarDialog(DialogPane dp) {
        dp.setStyle("-fx-background-color: #1a2318; -fx-border-color: #4a7c45;"
            + "-fx-border-width: 1.5; -fx-border-radius: 10;");
        if (dp.getScene() != null && dp.getScene().getRoot() != null) {
            dp.getScene().getRoot().setStyle("-fx-base: #1a2318;");
        }
    }
}