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
        carregarListagem();
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
            abrirMapa(novo);
        } else {
            // NOVIDADE: Adiciona como subtópico se a disciplina já existir e o título for diferente
            if (!titulo.equals(existente.getRoot().getName()) && !titulo.equals(disciplina)) {
                boolean jaExiste = false;
                for(MapNode filho : existente.getRoot().getChildren()){
                    if(filho.getName().equalsIgnoreCase(titulo)) jaExiste = true;
                }
                if(!jaExiste){
                    existente.getRoot().addChild(titulo);
                }
            }
            abrirMapa(existente);
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

    // ==========================================
    // PARTE 3: O NOVO MOTOR GEOMÉTRICO (ÁRVORE RADIAL)
    // ==========================================

    private void desenharMapaCompleto() {
        nodeViews.clear();
        paneDesenho.getChildren().clear();
        boxEdicao.setDisable(true);
        nodeSelecionado = null;
        viewSelecionada = null;
        
        // Determina o centro absoluto do painel
        double centroX = paneDesenho.getPrefWidth() > 0 ? paneDesenho.getPrefWidth() / 2 : 400;
        double centroY = paneDesenho.getPrefHeight() > 0 ? paneDesenho.getPrefHeight() / 2 : 300;
        
        if (mapaAtual.getRoot() != null) {
            // Inicia o desenho dando os 360 graus completos para a raiz distribuir aos filhos
            desenharMapaRadial(mapaAtual.getRoot(), centroX, centroY, 0, 360, 0, null, 0, 0);
        }
    }

    /**
     * Algoritmo de Árvore Radial Concêntrica
     * Garante simetria perfeita e anéis distintos para cada nível (filhos, netos, etc.)
     */
    private void desenharMapaRadial(MapNode no, double cx, double cy, double anguloInicio, double anguloFim, int nivel, MapNode pai, double paiX, double paiY) {
        double x = cx;
        double y = cy;

        // Se não for a raiz, calcula a posição no anel concêntrico correspondente ao seu nível
        if (nivel > 0) {
            double anguloMeio = anguloInicio + (anguloFim - anguloInicio) / 2.0;
            double raio = nivel * 130.0; // Cada subnível fica 130px mais distante do centro
            
            x = cx + raio * Math.cos(Math.toRadians(anguloMeio));
            y = cy + raio * Math.sin(Math.toRadians(anguloMeio));
        }

        // 1. Desenha a linha de conexão PRIMEIRO (para ficar atrás da bolinha)
        if (pai != null) {
            desenharLinha(paiX, paiY, x, y);
        }

        // 2. Renderiza a bolinha e o texto
        renderizarNo(no, nivel == 0, x, y);

        // 3. Fatiamento recursivo do ângulo para os filhos deste nó
        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        if (numFilhos > 0) {
            double fatia = (anguloFim - anguloInicio) / numFilhos;
            for (int i = 0; i < numFilhos; i++) {
                desenharMapaRadial(
                    no.getChildren().get(i), 
                    cx, cy, 
                    anguloInicio + (i * fatia), 
                    anguloInicio + ((i + 1) * fatia), 
                    nivel + 1, 
                    no, x, y
                );
            }
        }
    }

    private void renderizarNo(MapNode no, boolean isRaiz, double x, double y) {
        StackPane view = new StackPane();
        Circle circulo = new Circle(isRaiz ? 45 : 30);
        circulo.setFill(Color.web(COR_FUNDO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 3 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.setTextFill(Color.WHITE);
        lbl.setWrapText(true);
        lbl.setMaxWidth(isRaiz ? 80 : 50);
        lbl.setAlignment(Pos.CENTER);
        
        if (isRaiz) {
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #c8e6c4;");
        }

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - circulo.getRadius());
        view.setLayoutY(y - circulo.getRadius());

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
            // Devolve a borda grossa se for a raiz, fina se for filho
            ant.setStrokeWidth(ant.getRadius() > 35 ? 3 : 1.5);
        }
        nodeSelecionado = no;
        viewSelecionada = view;
        circulo.setStroke(Color.web(COR_SEL));
        circulo.setStrokeWidth(4); // Destaca fortemente o selecionado

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
                mostrarAviso("Aviso", "Não é possível remover o tópico raiz (centro do mapa).");
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