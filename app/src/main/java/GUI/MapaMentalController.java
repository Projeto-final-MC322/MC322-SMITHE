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
import modelo.MapNode;
import modelo.MaterialDeEstudo;
import modelo.MentalMap;

public class MapaMentalController {

    // Tela 1: Listagem de Mapas (Galeria)
    @FXML private VBox telaListagem;
    @FXML private FlowPane gridMapas;
    @FXML private TextField txtNovaDisciplina;
    @FXML private TextField txtNovoTitulo;

    // Tela 2: Mapa Aberto (Edição)
    @FXML private VBox telaMapa;
    @FXML private Label lblTituloMapaAberto;
    @FXML private Pane paneDesenho;
    @FXML private TextField txtNovoTopico;
    
    // Painel Lateral (Redação)
    @FXML private VBox boxEdicao;
    @FXML private Label lblNomeTopico;
    @FXML private TextArea txtConteudoTopico;

    private GerenciadorDeRevisao gerenciadorRevisao;
    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private MentalMap mapaAtual;
    private MapNode nodeSelecionado = null;
    
    // Variáveis de renderização geométrica
    private Map<MapNode, StackPane> nodeViews = new HashMap<>();
    private StackPane viewSelecionada = null;
    
    private final String COR_BORDA = "#2d3d2a";
    private final String COR_SEL = "#8fd685";
    private final String COR_FUNDO = "#1a2318";

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciadorRevisao = gerenciador;
        carregarListagem(); // Carrega a galeria assim que a tela abre
    }

    // ==========================================
    // PARTE 1: A GALERIA DE MAPAS MENTAIS
    // ==========================================

    private void carregarListagem() {
        gridMapas.getChildren().clear();
        
        List<MaterialDeEstudo> todos = gerenciadorConteudo.obterTodososMateriais();
        
        for (MaterialDeEstudo mat : todos) {
            if (mat instanceof MentalMap) {
                MentalMap mapa = (MentalMap) mat;
                
                // Cria um cartãozinho visual para cada disciplina
                VBox card = new VBox(5);
                card.setAlignment(Pos.CENTER);
                card.setPadding(new Insets(15));
                card.setStyle("-fx-background-color: #1a2318; -fx-border-color: #4a7c45; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand;");
                card.setPrefSize(160, 110);
                
                Label lblDisc = new Label(mapa.getDisciplina());
                lblDisc.setStyle("-fx-text-fill: #c8e6c4; -fx-font-weight: bold; -fx-font-size: 14px;");
                
                Label lblTopicos = new Label(contarTopicos(mapa.getRoot()) + " tópicos criados");
                lblTopicos.setStyle("-fx-text-fill: #6a8a66; -fx-font-size: 11px;");
                
                card.getChildren().addAll(lblDisc, lblTopicos);
                
                // Ao clicar no cartão, a tela 2 abre!
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
        String titulo = txtNovoTitulo.getText().trim();
        if (disciplina.isEmpty()) return;
        if (titulo.isEmpty()) titulo = disciplina;

        MentalMap existente = gerenciadorConteudo.obterMapaMentalDaDisciplina(disciplina);
        if (existente == null) {
            MentalMap novo = new MentalMap(titulo, disciplina);
            gerenciadorConteudo.adicionarMaterial(novo);
            abrirMapa(novo); // Cria e já entra no mapa
        } else {
            abrirMapa(existente); // Já existe? Apenas entra
        }
        
        txtNovaDisciplina.clear();
        txtNovoTitulo.clear();
    }

    // ==========================================
    // PARTE 2: A TELA DO MAPA ABERTO E NAVEGAÇÃO
    // ==========================================

    private void abrirMapa(MentalMap mapa) {
        this.mapaAtual = mapa;
        telaListagem.setVisible(false);
        telaMapa.setVisible(true);
        
        lblTituloMapaAberto.setText(mapa.getDisciplina() + " - " + mapa.getTitulo());
        desenharMapaCompleto();
    }

    @FXML
    public void voltarParaListagem() {
        telaMapa.setVisible(false);
        telaListagem.setVisible(true);
        mapaAtual = null;
        nodeSelecionado = null;
        viewSelecionada = null;
        carregarListagem(); // Atualiza a galeria ao voltar
    }

    @FXML
    public void adicionarTopico() {
        String novoNome = txtNovoTopico.getText().trim();
        if (novoNome.isEmpty() || mapaAtual == null) return;
        
        // Se um nó estiver clicado, adiciona como filho dele. Senão, adiciona na raiz principal.
        if (nodeSelecionado != null) {
            nodeSelecionado.addChild(novoNome);
        } else {
            mapaAtual.getRoot().addChild(novoNome);
        }
        
        txtNovoTopico.clear();
        desenharMapaCompleto(); // Atualiza a tela com a nova bolinha
    }

    // ==========================================
    // PARTE 3: O MOTOR GEOMÉTRICO DO GRAFO
    // ==========================================

    private void desenharMapaCompleto() {
        nodeViews.clear();
        paneDesenho.getChildren().clear();
        boxEdicao.setDisable(true);
        nodeSelecionado = null;
        viewSelecionada = null;
        
        double centroX = paneDesenho.getPrefWidth() > 0 ? paneDesenho.getPrefWidth() / 2 : 400;
        double centroY = paneDesenho.getPrefHeight() > 0 ? paneDesenho.getPrefHeight() / 2 : 300;
        
        if (mapaAtual.getRoot() != null) {
            desenharMapaExistente(mapaAtual.getRoot(), true, centroX, centroY);
        }
    }

    private void desenharMapaExistente(MapNode no, boolean isRaiz, double x, double y) {
        renderizarNo(no, isRaiz, x, y);
        
        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        double anguloPasso = numFilhos > 0 ? 360.0 / numFilhos : 0;
        double raio = isRaiz ? 120.0 : 80.0;

        int i = 0;
        if (no.getChildren() != null) {
            for (MapNode filho : no.getChildren()) {
                double anguloRad = Math.toRadians(i * anguloPasso);
                double filhoX = x + raio * Math.cos(anguloRad);
                double filhoY = y + raio * Math.sin(anguloRad);
                
                desenharLinha(x, y, filhoX, filhoY); // Trilha a linha
                desenharMapaExistente(filho, false, filhoX, filhoY); // Desenha o filho por cima
                i++;
            }
        }
    }

    private void renderizarNo(MapNode no, boolean isRaiz, double x, double y) {
        StackPane view = new StackPane();
        Circle circulo = new Circle(isRaiz ? 40 : 30);
        circulo.setFill(Color.web(COR_FUNDO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 2 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.setTextFill(Color.WHITE);
        lbl.setWrapText(true);
        lbl.setMaxWidth(isRaiz ? 70 : 50);
        lbl.setAlignment(Pos.CENTER);

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - circulo.getRadius());
        view.setLayoutY(y - circulo.getRadius());

        // Atribui o evento de Clique!
        view.setOnMouseClicked(e -> selecionarNo(no, view, circulo));

        nodeViews.put(no, view);
        paneDesenho.getChildren().add(view); 
    }

    private void desenharLinha(double startX, double startY, double endX, double endY) {
        Line linha = new Line(startX, startY, endX, endY);
        linha.setStroke(Color.web(COR_BORDA));
        linha.setStrokeWidth(2);
        paneDesenho.getChildren().add(0, linha); 
    }

    // ==========================================
    // PARTE 4: A REDAÇÃO DE CONTEÚDO (CAIXA DE TEXTO)
    // ==========================================

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
        lblNomeTopico.setText("Tópico: " + no.getName());
        
        // Puxa da memória da classe MapNode o texto já redigido
        txtConteudoTopico.setText(no.getDefinition() != null ? no.getDefinition() : "");
    }

    @FXML
    public void salvarConteudoNo() {
        if (nodeSelecionado != null) {
            // Salva na memória da classe MapNode
            nodeSelecionado.setDefinition(txtConteudoTopico.getText());
            mostrarAviso("Salvo!", "A redação do tópico '" + nodeSelecionado.getName() + "' foi salva no mapa.");
        }
    }
    
    @FXML
    public void removerNo() {
        if (nodeSelecionado != null && mapaAtual != null) {
            if (nodeSelecionado == mapaAtual.getRoot()) {
                mostrarAviso("Aviso", "Não é possível remover o tópico raiz.");
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