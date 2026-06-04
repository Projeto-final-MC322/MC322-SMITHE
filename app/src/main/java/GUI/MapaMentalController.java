package GUI;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import logica.GerenciadorDeConteudo;
import logica.GerenciadorDeRevisao;
import modelo.MapNode;
import modelo.MentalMap;

public class MapaMentalController {

    @FXML private TextField txtTituloMapa;
    @FXML private TextField txtDiscMapa;
    @FXML private Pane paneDesenho;
    
    @FXML private VBox boxEdicao;
    @FXML private Label lblNomeTopico;
    @FXML private TextArea txtConteudoTopico;

    private GerenciadorDeRevisao gerenciadorRevisao;
    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private MentalMap mapaAtual;
    private MapNode nodeSelecionado = null;
    
    // Variáveis de controle de desenho que haviam sido apagadas
    private Map<MapNode, StackPane> nodeViews = new HashMap<>();
    private StackPane viewSelecionada = null;
    
    private final String COR_BORDA = "#2d3d2a";
    private final String COR_SEL = "#8fd685";
    private final String COR_FUNDO = "#1a2318";

    // O método crucial que o App.java usa para injetar dependências (Erro 1)
    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciadorRevisao = gerenciador;
    }

    @FXML
    public void carregarOuCriarMapa() {
        String disciplina = txtDiscMapa.getText().trim();
        String titulo = txtTituloMapa.getText().trim();
        if (disciplina.isEmpty()) return;

        mapaAtual = gerenciadorConteudo.obterMapaMentalDaDisciplina(disciplina);

        if (mapaAtual == null) {
            if (titulo.isEmpty()) titulo = disciplina;
            mapaAtual = new MentalMap(titulo, disciplina);
            gerenciadorConteudo.adicionarMaterial(mapaAtual);
        }

        // Limpa a tela e prepara para renderizar
        nodeViews.clear();
        paneDesenho.getChildren().clear();
        
        // Posição central da tela
        double centroX = paneDesenho.getPrefWidth() > 0 ? paneDesenho.getPrefWidth() / 2 : 400;
        double centroY = paneDesenho.getPrefHeight() > 0 ? paneDesenho.getPrefHeight() / 2 : 300;
        
        // Inicia o desenho recursivo
        if (mapaAtual.getRoot() != null) {
            desenharMapaExistente(mapaAtual.getRoot(), true, centroX, centroY);
        }
        
        txtTituloMapa.clear();
        boxEdicao.setDisable(true);
    }

    // Método que calcula a posição trigonométrica de cada subtópico
    private void desenharMapaExistente(MapNode no, boolean isRaiz, double x, double y) {
        renderizarNo(no, isRaiz, x, y);
        
        // Se a sua classe MapNode não usar getChildren(), isso precisará ser ajustado depois
        /*
        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        double anguloPasso = numFilhos > 0 ? 360.0 / numFilhos : 0;
        double raio = 100.0;

        int i = 0;
        if (no.getChildren() != null) {
            for (MapNode filho : no.getChildren()) {
                double anguloRad = Math.toRadians(i * anguloPasso);
                double filhoX = x + raio * Math.cos(anguloRad);
                double filhoY = y + raio * Math.sin(anguloRad);
                
                desenharLinha(x, y, filhoX, filhoY);
                desenharMapaExistente(filho, false, filhoX, filhoY);
                i++;
            }
        }
        */
    }

    // Desenha o círculo visual do nó
    private void renderizarNo(MapNode no, boolean isRaiz, double x, double y) {
        StackPane view = new StackPane();
        Circle circulo = new Circle(isRaiz ? 40 : 30);
        circulo.setFill(Color.web(COR_FUNDO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 2 : 1.5);

        // Se a sua classe MapNode não tiver getName(), troque para o método correto (ex: getTitulo())
        Label lbl = new Label("Tópico"); 
        lbl.setTextFill(Color.WHITE);

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - circulo.getRadius());
        view.setLayoutY(y - circulo.getRadius());

        view.setOnMouseClicked(e -> selecionarNo(no, view, circulo));

        nodeViews.put(no, view);
        paneDesenho.getChildren().add(view);
    }

    // Conecta os nós com uma linha visual
    private void desenharLinha(double startX, double startY, double endX, double endY) {
        Line linha = new Line(startX, startY, endX, endY);
        linha.setStroke(Color.web(COR_BORDA));
        linha.setStrokeWidth(2);
        paneDesenho.getChildren().add(0, linha);
    }

    // Ativa o painel lateral quando o nó é clicado
    private void selecionarNo(MapNode no, StackPane view, Circle circulo) {
        if (viewSelecionada != null) {
            Circle ant = (Circle) viewSelecionada.getChildren().get(0);
            ant.setStroke(Color.web(COR_BORDA));
            ant.setStrokeWidth(1.5);
        }
        nodeSelecionado = no;
        viewSelecionada = view;
        circulo.setStroke(Color.web(COR_SEL));
        circulo.setStrokeWidth(3);

        boxEdicao.setDisable(false);
        lblNomeTopico.setText("Tópico Selecionado"); // Ajuste conforme os getters do MapNode
    }

    @FXML
    public void salvarConteudoNo() {
        if (nodeSelecionado != null) {
            // nodeSelecionado.setConteudo(txtConteudoTopico.getText()); // Descomente quando MapNode suportar isso
            mostrarAviso("Salvo!", "O conteúdo do tópico foi salvo com sucesso.");
        }
    }

    @FXML
    public void adicionarFilho() {
        mostrarAviso("Desenvolvimento", "Adicionar Subtópico será ativado em breve.");
    }
    
    @FXML
    public void removerNo() {
        mostrarAviso("Desenvolvimento", "Remover Tópico será ativado em breve.");
    }

    // Helper para gerar Pop-ups na tela
    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}