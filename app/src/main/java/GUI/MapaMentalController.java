package GUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

    @FXML private VBox telaMapa;
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
    
    private final String COR_BORDA = "#81c784"; // Verde médio
    private final String COR_SEL = "#2e7d32";   // Verde escuro intenso ao selecionar
    private final String COR_FUNDO = "#ffffff"; // Bolinha branca limpa

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciadorRevisao = gerenciador;
        carregarListagem();
    }
    public void setEstatisticas(EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
    }

    private void carregarListagem() {
        gridMapas.getChildren().clear();
        List<MaterialDeEstudo> todos = gerenciadorConteudo.obterTodososMateriais();
        
        for (MaterialDeEstudo mat : todos) {
            if (mat instanceof MentalMap) {
                MentalMap mapa = (MentalMap) mat;
                VBox card = new VBox(5);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(15));
               card.setStyle("-fx-background-color: white; -fx-border-color: #a5d6a7; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;");
                card.setPrefSize(160, 110);
    
                 Label lblDisc = new Label(mapa.getDisciplina());
                lblDisc.setStyle("-fx-text-fill: #1b5e20; -fx-font-weight: bold; -fx-font-size: 14px;");
    
                Label lblTopicos = new Label(contarTopicos(mapa.getRoot()) + " tópicos");
                lblTopicos.setStyle("-fx-text-fill: #388e3c; -fx-font-size: 11px;");
                
                card.getChildren().addAll(lblDisc, lblTopicos);
                card.setOnMouseClicked(e -> abrirMapa(mapa));
                gridMapas.getChildren().add(card);
            }
        }
    }

    private int contarTopicos(MapNode no) {
        if (no == null) return 0;
        int count = 1;
        for (MapNode filho : no.getChildren()) {
            count += contarTopicos(filho);
        }
        return count;
    }

    @FXML
    public void criarMapaDaListagem() {
        String disciplina = txtNovaDisciplina.getText().trim();
        String topico = txtNovoTitulo.getText().trim();
        if (disciplina.isEmpty()) return;

        MentalMap existente = gerenciadorConteudo.obterMapaMentalDaDisciplina(disciplina);
        if (existente == null) {
            MentalMap novo = new MentalMap(disciplina, disciplina);
            gerenciadorConteudo.adicionarMaterial(novo);
            if (!topico.isEmpty() && !topico.equalsIgnoreCase(disciplina)) {
                novo.getRoot().addChild(topico);
            }
            abrirMapa(novo);
        } else {
            if (!topico.isEmpty()) {
                boolean jaExiste = false;
                for (MapNode filho : existente.getRoot().getChildren()) {
                    if (filho.getName().equalsIgnoreCase(topico)) jaExiste = true;
                }
                if (!jaExiste && !topico.equalsIgnoreCase(existente.getRoot().getName())) {
                    existente.getRoot().addChild(topico);
                }
            }
            abrirMapa(existente);
        }
        txtNovaDisciplina.clear();
        txtNovoTitulo.clear();
    }

    private void abrirMapa(MentalMap mapa) {
        this.mapaAtual = mapa;
        telaListagem.setVisible(false);
        telaMapa.setVisible(true);
        lblTituloMapaAberto.setText("Mapa de " + mapa.getDisciplina());
        desenharMapaCompleto();
    }

    @FXML
    public void voltarParaListagem() {
        telaMapa.setVisible(false);
        telaListagem.setVisible(true);
        mapaAtual = null; nodeSelecionado = null; viewSelecionada = null;
        carregarListagem(); 
    }

    @FXML
    public void adicionarTopico() {
        String novoNome = txtNovoTopico.getText().trim();
        if (novoNome.isEmpty() || mapaAtual == null) return;
        
        if (nodeSelecionado != null) {
            nodeSelecionado.addChild(novoNome);
        } else {
            mapaAtual.getRoot().addChild(novoNome);
        }
        txtNovoTopico.clear();
        desenharMapaCompleto(); 
    }

    // Função engatilhada pelo botão "Reorganizar Mapa"
    @FXML
    public void reorganizarMapa() {
        if (mapaAtual != null) {
            desenharMapaCompleto(); // Recalcula posições diametrais do zero
        }
    }

    private void desenharMapaCompleto() {
        nodeViews.clear();
        paneDesenho.getChildren().clear();
        boxEdicao.setDisable(true);
        nodeSelecionado = null; viewSelecionada = null;
        
        double centroX = paneDesenho.getPrefWidth() > 0 ? paneDesenho.getPrefWidth() / 2 : 400;
        double centroY = paneDesenho.getPrefHeight() > 0 ? paneDesenho.getPrefHeight() / 2 : 300;
        
        if (mapaAtual.getRoot() != null) {
            desenharMapaDiametral(mapaAtual.getRoot(), centroX, centroY, 0, 360, 0, null, 0);
        }
    }

    // Agora o método retorna a view criada para poder amarrar a linha de forma dinâmica!
    private StackPane desenharMapaDiametral(MapNode no, double cx, double cy, double anguloInicio, double anguloFim, int nivel, StackPane viewPai, double raioPai) {
        double x = cx;
        double y = cy;

        if (nivel > 0) {
            double anguloMeio = anguloInicio + (anguloFim - anguloInicio) / 2.0;
            double raio = nivel * 160.0; 
            x = cx + raio * Math.cos(Math.toRadians(anguloMeio));
            y = cy + raio * Math.sin(Math.toRadians(anguloMeio));
        }

        boolean isRaiz = (nivel == 0);
        double raioAtual = isRaiz ? 50 : 35; // Define o raio baseado se é raiz ou filho

        // 1. Renderiza a bolinha e obtém a View
        StackPane viewAtual = renderizarNo(no, isRaiz, x, y, raioAtual);

        // 2. Desenha a linha ELÁSTICA (Binding) ligando o pai a este filho
        if (viewPai != null) {
            desenharLinhaElastica(viewPai, viewAtual, raioPai, raioAtual);
        }

        // 3. Renderiza os filhos recursivamente
        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        if (numFilhos > 0) {
            double tamanhoFatia = (anguloFim - anguloInicio) / numFilhos;
            for (int i = 0; i < numFilhos; i++) {
                double inicioFilho = anguloInicio + (i * tamanhoFatia);
                double fimFilho = inicioFilho + tamanhoFatia;
                desenharMapaDiametral(
                    no.getChildren().get(i), 
                    cx, cy, inicioFilho, fimFilho, 
                    nivel + 1, 
                    viewAtual, raioAtual
                );
            }
        }
        return viewAtual;
    }

    private StackPane renderizarNo(MapNode no, boolean isRaiz, double x, double y, double raio) {
        StackPane view = new StackPane();
        Circle circulo = new Circle(raio);
        circulo.setFill(Color.web(COR_FUNDO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 3 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.setTextFill(Color.web("#1b5e20")); // <- MUDE AQUI PARA #1b5e20 (Verde Escuro)
        lbl.setWrapText(true);
        lbl.setMaxWidth(isRaiz ? 85 : 60);
        lbl.setAlignment(Pos.CENTER);
        
        if (isRaiz) {
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1b5e20; -fx-font-size: 13px;"); // <- MUDE AQUI TAMBÉM
        }

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - raio);
        view.setLayoutY(y - raio);
        view.setCursor(javafx.scene.Cursor.HAND);

        // --- LÓGICA DE ARRASTAR E SOLTAR (DRAG AND DROP) ---
        view.setOnMousePressed(e -> {
            // Guarda a diferença entre o clique do rato e o canto superior da view
            view.setUserData(new double[]{view.getLayoutX() - e.getSceneX(), view.getLayoutY() - e.getSceneY()});
            selecionarNo(no, view, circulo);
        });

        view.setOnMouseDragged(e -> {
            double[] offset = (double[]) view.getUserData();
            // Move a bolinha pelo mapa de acordo com o cursor
            view.setLayoutX(e.getSceneX() + offset[0]);
            view.setLayoutY(e.getSceneY() + offset[1]);
        });

        nodeViews.put(no, view);
        paneDesenho.getChildren().add(view); 
        return view;
    }

    private void desenharLinhaElastica(StackPane startView, StackPane endView, double startRaio, double endRaio) {
        Line linha = new Line();
        linha.setStroke(Color.web(COR_BORDA));
        linha.setStrokeWidth(2);

        // A MÁGICA: A linha está "amarrada" ao layout das bolinhas + o raio (para ir pro centro)
        // Quando a bolinha for arrastada, a linha atualiza a sua posição automaticamente!
        linha.startXProperty().bind(startView.layoutXProperty().add(startRaio));
        linha.startYProperty().bind(startView.layoutYProperty().add(startRaio));
        
        linha.endXProperty().bind(endView.layoutXProperty().add(endRaio));
        linha.endYProperty().bind(endView.layoutYProperty().add(endRaio));

        paneDesenho.getChildren().add(0, linha); // Adiciona a linha no fundo (índice 0)
    }

    private void selecionarNo(MapNode no, StackPane view, Circle circulo) {
        if (viewSelecionada != null) {
            Circle ant = (Circle) viewSelecionada.getChildren().get(0);
            ant.setStroke(Color.web(COR_BORDA));
            ant.setStrokeWidth(ant.getRadius() > 40 ? 3 : 1.5);
        }
        nodeSelecionado = no;
        viewSelecionada = view;
        circulo.setStroke(Color.web(COR_SEL));
        circulo.setStrokeWidth(4); 

        boxEdicao.setDisable(false);
        lblNomeTopico.setText("Tópico: " + no.getName());
        txtConteudoTopico.setText(no.getDefinition() != null ? no.getDefinition() : "");
    }

    @FXML
    public void salvarConteudoNo() {
        if (nodeSelecionado != null) {
            nodeSelecionado.setDefinition(txtConteudoTopico.getText());
            mostrarAviso("Salvo!", "A redação do tópico '" + nodeSelecionado.getName() + "' foi salva.");
        }
    }
    
    @FXML
    public void removerNo() {
        if (nodeSelecionado != null && mapaAtual != null) {
            if (nodeSelecionado == mapaAtual.getRoot()) {
                mostrarAviso("Aviso", "Não é possível remover a Disciplina central.");
            } else {
                mapaAtual.removerNo(nodeSelecionado.getName());
                desenharMapaCompleto();
            }
        }
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}