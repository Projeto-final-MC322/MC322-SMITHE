package GUI;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import logica.GerenciadorDeConteudo;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;
import modelo.MapNode;
import modelo.MaterialDeEstudo;
import modelo.MentalMap;

public class MapaMentalController {
    @FXML private VBox telaListagem;
    @FXML private FlowPane gridMapas;
    @FXML private TextField txtNovaDisciplina;
    @FXML private TextField txtNovoTitulo;

    @FXML private StackPane telaMapa;
    @FXML private Label lblTituloMapaAberto;
    @FXML private Pane paneDesenho;
    @FXML private TextField txtNovoTopico;
    
    @FXML private VBox boxEdicao;
    @FXML private Label lblNomeTopico;
    @FXML private TextArea txtConteudoTopico;

    private GerenciadorDeRevisao gerenciadorRevisao;
    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private MentalMap mapaAtual;
    private MapNode nodeSelecionado = null;
    private Map<MapNode, StackPane> nodeViews = new HashMap<>();
    private StackPane viewSelecionada = null;

    private EstatisticaDesempenho estatisticas;
    private TelaPrincipal telaPrincipal;

    public void setEstatisticas(EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
    }

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciadorRevisao = gerenciador;
        carregarListagem();
    }

    private void carregarListagem() {
        gridMapas.getChildren().clear();
        for (MaterialDeEstudo mat : gerenciadorConteudo.obterTodososMateriais()) {
            if (mat instanceof MentalMap) {
                MentalMap mapa = (MentalMap) mat;
                VBox card = new VBox(5);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(15));
                card.getStyleClass().add("card-padrao");
                card.setPrefSize(160, 110);
                
                Label lblDisc = new Label(mapa.getDisciplina());
                lblDisc.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                lblDisc.getStyleClass().add("texto-padrao");
                
                Label lblTopicos = new Label("Clique para abrir");
                lblTopicos.getStyleClass().add("texto-mut");
                
                card.getChildren().addAll(lblDisc, lblTopicos);
                card.setOnMouseClicked(e -> abrirMapa(mapa));
                gridMapas.getChildren().add(card);
            }
        }
    }

    @FXML
    public void criarMapaDaListagem() {
        String disciplina = txtNovaDisciplina.getText().trim();
        String topico = txtNovoTitulo.getText().trim();
        if (disciplina.isEmpty()) return;

        MentalMap existente = gerenciadorConteudo.obterMapaMentalDaDisciplina(disciplina);
        if (existente == null) {
            // CORREÇÃO: O Tópico central é o Tópico, não a disciplina!
            String raiz = topico.isEmpty() ? "Tópico Central" : topico;
            MentalMap novo = new MentalMap(raiz, disciplina);
            gerenciadorConteudo.adicionarMaterial(novo);
            abrirMapa(novo);
        } else {
            if (!topico.isEmpty() && !topico.equalsIgnoreCase(existente.getRoot().getName())) {
                boolean jaExiste = false;
                for (MapNode filho : existente.getRoot().getChildren()) {
                    if (filho.getName().equalsIgnoreCase(topico)) jaExiste = true;
                }
                if (!jaExiste) existente.getRoot().addChild(topico);
            }
            abrirMapa(existente);
        }
        txtNovaDisciplina.clear(); txtNovoTitulo.clear();
    }

    private void abrirMapa(MentalMap mapa) {
        this.mapaAtual = mapa;
        telaListagem.setVisible(false);
        telaMapa.setVisible(true);
        lblTituloMapaAberto.setText("Mapa de " + mapa.getDisciplina());
        
        // CORREÇÃO: Garante que a tela mediu os tamanhos para centralizar o grafo
        Platform.runLater(this::desenharMapaCompleto);
    }

    @FXML
    public void voltarParaListagem() {
        telaMapa.setVisible(false); telaListagem.setVisible(true);
        mapaAtual = null; nodeSelecionado = null; viewSelecionada = null;
        carregarListagem(); 
    }

    @FXML
    public void adicionarTopico() {
        String novoNome = txtNovoTopico.getText().trim();
        if (novoNome.isEmpty() || mapaAtual == null) return;
        if (nodeSelecionado != null) nodeSelecionado.addChild(novoNome);
        else mapaAtual.getRoot().addChild(novoNome);
        txtNovoTopico.clear();
        desenharMapaCompleto(); 
    }

    @FXML
    public void reorganizarMapa() { if (mapaAtual != null) desenharMapaCompleto(); }

    private void desenharMapaCompleto() {
        nodeViews.clear(); paneDesenho.getChildren().clear();
        boxEdicao.setDisable(true); nodeSelecionado = null; viewSelecionada = null;
        
        double centroX = paneDesenho.getWidth() > 0 ? paneDesenho.getWidth() / 2.0 : 400;
        double centroY = paneDesenho.getHeight() > 0 ? paneDesenho.getHeight() / 2.0 : 300;
        
        if (mapaAtual.getRoot() != null) {
            desenharMapaDiametral(mapaAtual.getRoot(), centroX, centroY, 0, 360, 0, null, 0);
        }
    }

    private StackPane desenharMapaDiametral(MapNode no, double cx, double cy, double angInicio, double angFim, int nivel, StackPane viewPai, double raioPai) {
        double x = cx, y = cy;
        if (nivel > 0) {
            double angMeio = angInicio + (angFim - angInicio) / 2.0;
            double raio = nivel * 110.0; // Distância menor para não sair da tela facilmente
            x = cx + raio * Math.cos(Math.toRadians(angMeio));
            y = cy + raio * Math.sin(Math.toRadians(angMeio));
        }

        boolean isRaiz = (nivel == 0);
        double raioAtual = isRaiz ? 45 : 30;

        StackPane viewAtual = renderizarNo(no, isRaiz, x, y, raioAtual);
        if (viewPai != null) desenharLinhaElastica(viewPai, viewAtual, raioPai, raioAtual);

        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        if (numFilhos > 0) {
            double fatia = (angFim - angInicio) / numFilhos;
            for (int i = 0; i < numFilhos; i++) {
                desenharMapaDiametral(no.getChildren().get(i), cx, cy, angInicio + (i * fatia), angInicio + ((i+1) * fatia), nivel + 1, viewAtual, raioAtual);
            }
        }
        return viewAtual;
    }

    private StackPane renderizarNo(MapNode no, boolean isRaiz, double x, double y, double raio) {
        StackPane view = new StackPane();
        Circle circulo = new Circle(raio);
        circulo.getStyleClass().add("circulo-no"); // Aplica o CSS de Tema
        circulo.setStrokeWidth(isRaiz ? 3 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.getStyleClass().add("texto-padrao"); // Aplica o CSS de Tema
        lbl.setWrapText(true); lbl.setMaxWidth(isRaiz ? 80 : 55); lbl.setAlignment(Pos.CENTER);
        if (isRaiz) lbl.setStyle("-fx-font-weight: bold;");

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - raio); view.setLayoutY(y - raio);
        view.setCursor(javafx.scene.Cursor.HAND);

        view.setOnMousePressed(e -> {
            view.setUserData(new double[]{view.getLayoutX() - e.getSceneX(), view.getLayoutY() - e.getSceneY()});
            selecionarNo(no, view, circulo);
        });
        view.setOnMouseDragged(e -> {
            double[] offset = (double[]) view.getUserData();
            view.setLayoutX(e.getSceneX() + offset[0]); view.setLayoutY(e.getSceneY() + offset[1]);
        });

        nodeViews.put(no, view); paneDesenho.getChildren().add(view); 
        return view;
    }

    private void desenharLinhaElastica(StackPane v1, StackPane v2, double r1, double r2) {
        Line linha = new Line();
        linha.getStyleClass().add("linha-mapa"); // CSS
        linha.setStrokeWidth(2);
        linha.startXProperty().bind(v1.layoutXProperty().add(r1));
        linha.startYProperty().bind(v1.layoutYProperty().add(r1));
        linha.endXProperty().bind(v2.layoutXProperty().add(r2));
        linha.endYProperty().bind(v2.layoutYProperty().add(r2));
        paneDesenho.getChildren().add(0, linha); 
    }

    private void selecionarNo(MapNode no, StackPane view, Circle circulo) {
        if (viewSelecionada != null) {
            Circle ant = (Circle) viewSelecionada.getChildren().get(0);
            ant.getStyleClass().remove("circulo-no-sel");
        }
        nodeSelecionado = no; viewSelecionada = view;
        circulo.getStyleClass().add("circulo-no-sel"); // Highlight via CSS

        boxEdicao.setDisable(false);
        lblNomeTopico.setText("Tópico: " + no.getName());
        txtConteudoTopico.setText(no.getDefinition() != null ? no.getDefinition() : "");
    }

    @FXML
    public void salvarConteudoNo() {
        if (nodeSelecionado != null) {
            nodeSelecionado.setDefinition(txtConteudoTopico.getText());
        }
    }
    
    @FXML
    public void removerNo() {
        if (nodeSelecionado != null && mapaAtual != null) {
            if (nodeSelecionado == mapaAtual.getRoot()) return;
            mapaAtual.removerNo(nodeSelecionado.getName());
            desenharMapaCompleto();
        }
    }
}