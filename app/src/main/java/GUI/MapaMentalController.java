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
                
                Label lblTopicos = new Label(contarTopicos(mapa.getRoot()) + " tópicos");
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
        String topico = txtNovoTitulo.getText().trim();
        if (disciplina.isEmpty()) return;

        MentalMap existente = gerenciadorConteudo.obterMapaMentalDaDisciplina(disciplina);
        
        if (existente == null) {
            // CORREÇÃO: A raiz do mapa é SEMPRE a Disciplina (O Centro)
            MentalMap novo = new MentalMap(disciplina, disciplina);
            gerenciadorConteudo.adicionarMaterial(novo);
            
            // Se ele preencheu o campo de tópico, cria orbitando a raiz
            if (!topico.isEmpty() && !topico.equalsIgnoreCase(disciplina)) {
                novo.getRoot().addChild(topico);
            }
            abrirMapa(novo);
            
        } else {
            // CORREÇÃO: O mapa existe. Adiciona o novo tópico orbitando a raiz!
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

    // ==========================================
    // PARTE 2: A TELA DO MAPA ABERTO E NAVEGAÇÃO
    // ==========================================

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
        mapaAtual = null;
        nodeSelecionado = null;
        viewSelecionada = null;
        carregarListagem(); 
    }

    @FXML
    public void adicionarTopico() {
        String novoNome = txtNovoTopico.getText().trim();
        if (novoNome.isEmpty() || mapaAtual == null) return;
        
        // Se um nó estiver selecionado, o novo orbita ele. Se não, orbita o centro (raiz).
        if (nodeSelecionado != null) {
            nodeSelecionado.addChild(novoNome);
        } else {
            mapaAtual.getRoot().addChild(novoNome);
        }
        
        txtNovoTopico.clear();
        desenharMapaCompleto(); 
    }

    // ==========================================
    // PARTE 3: ALGORITMO DIAMETRAL (ESPAÇAMENTO PERFEITO)
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
            // A raiz ganha a "pizza" inteira (0 a 360 graus) para distribuir aos filhos
            desenharMapaDiametral(mapaAtual.getRoot(), centroX, centroY, 0, 360, 0, null, 0, 0);
        }
    }

    /**
     * Algoritmo de Distribuição Radial Fatiada
     * Evita sobreposições dividindo o ângulo do pai entre os filhos e aumentando o raio.
     */
    private void desenharMapaDiametral(MapNode no, double cx, double cy, double anguloInicio, double anguloFim, int nivel, MapNode pai, double paiX, double paiY) {
        double x = cx;
        double y = cy;

        if (nivel > 0) {
            // O nó é posicionado exatamente no MEIO da fatia de ângulo que recebeu do pai
            double anguloMeio = anguloInicio + (anguloFim - anguloInicio) / 2.0;
            
            // CORREÇÃO DE ESPAÇAMENTO: A cada nível, a órbita salta 160 pixels de distância!
            double raio = nivel * 160.0; 
            
            x = cx + raio * Math.cos(Math.toRadians(anguloMeio));
            y = cy + raio * Math.sin(Math.toRadians(anguloMeio));
        }

        // 1. Desenha a linha conectando ao pai
        if (pai != null) {
            desenharLinha(paiX, paiY, x, y);
        }

        // 2. Renderiza a bolinha
        renderizarNo(no, nivel == 0, x, y);

        // 3. Divide a "fatia" deste nó igualmente entre os filhos dele
        int numFilhos = no.getChildren() != null ? no.getChildren().size() : 0;
        if (numFilhos > 0) {
            double tamanhoDaFatiaFilho = (anguloFim - anguloInicio) / numFilhos;
            for (int i = 0; i < numFilhos; i++) {
                double inicioFilho = anguloInicio + (i * tamanhoDaFatiaFilho);
                double fimFilho = inicioFilho + tamanhoDaFatiaFilho;
                
                desenharMapaDiametral(
                    no.getChildren().get(i), 
                    cx, cy, 
                    inicioFilho, 
                    fimFilho, 
                    nivel + 1, 
                    no, x, y
                );
            }
        }
    }

    private void renderizarNo(MapNode no, boolean isRaiz, double x, double y) {
        StackPane view = new StackPane();
        // A raiz é maior (50) para destacar o centro do mapa
        Circle circulo = new Circle(isRaiz ? 50 : 35);
        circulo.setFill(Color.web(COR_FUNDO));
        circulo.setStroke(Color.web(COR_BORDA));
        circulo.setStrokeWidth(isRaiz ? 3 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.setTextFill(Color.WHITE);
        lbl.setWrapText(true);
        lbl.setMaxWidth(isRaiz ? 85 : 60);
        lbl.setAlignment(Pos.CENTER);
        
        if (isRaiz) {
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #c8e6c4; -fx-font-size: 13px;");
        }

        view.getChildren().addAll(circulo, lbl);
        
        // Centraliza a bolinha exatamente na coordenada matemática calculada
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