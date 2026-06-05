package GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    private List<MentalMap> mapasAtuais = new ArrayList<>();
    private String disciplinaAtual = "";
    
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
        Map<String, Integer> contagemTopicos = new HashMap<>();

        // Agrupa todos os mapas/tópicos independentes pela Disciplina
        for (MaterialDeEstudo mat : gerenciadorConteudo.obterTodososMateriais()) {
            if (mat instanceof MentalMap) {
                MentalMap m = (MentalMap) mat;
                String disc = m.getDisciplina();
                contagemTopicos.put(disc, contagemTopicos.getOrDefault(disc, 0) + contarNomes(m.getRoot()));
            }
        }

        // Cria apenas um cartão (Pasta) por Disciplina
        for (String disc : contagemTopicos.keySet()) {
            VBox card = new VBox(5);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(15));
            card.getStyleClass().add("card-padrao");
            card.setPrefSize(160, 110);
            card.setStyle("-fx-cursor: hand;");
            
            Label lblDisc = new Label(disc);
            lblDisc.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            lblDisc.getStyleClass().add("texto-padrao");
            
            Label lblTopicos = new Label(contagemTopicos.get(disc) + " tópicos");
            lblTopicos.getStyleClass().add("texto-mut");
            
            card.getChildren().addAll(lblDisc, lblTopicos);
            card.setOnMouseClicked(e -> abrirDisciplina(disc));
            gridMapas.getChildren().add(card);
        }
    }

    private int contarNomes(MapNode no) {
        if (no == null) return 0;
        int count = 1;
        for (MapNode filho : no.getChildren()) count += contarNomes(filho);
        return count;
    }

    @FXML
    public void criarMapaDaListagem() {
        String disciplina = txtNovaDisciplina.getText().trim();
        String topico = txtNovoTitulo.getText().trim();
        if (disciplina.isEmpty()) return;
        if (topico.isEmpty()) topico = "Tópico Central";

        // Verifica se a raiz já existe nesta disciplina
        boolean existe = false;
        for (MaterialDeEstudo mat : gerenciadorConteudo.obterMateriaisPorDisciplina(disciplina)) {
            if (mat instanceof MentalMap && mat.getTitulo().equalsIgnoreCase(topico)) {
                existe = true; break;
            }
        }

        if (!existe) {
            MentalMap novo = new MentalMap(topico, disciplina);
            gerenciadorConteudo.adicionarMaterial(novo);
        }

        abrirDisciplina(disciplina);
        txtNovaDisciplina.clear(); 
        txtNovoTitulo.clear();
    }

    private void abrirDisciplina(String disciplina) {
        this.disciplinaAtual = disciplina;
        this.mapasAtuais.clear();
        
        
        for (MaterialDeEstudo mat : gerenciadorConteudo.obterMateriaisPorDisciplina(disciplina)) {
            if (mat instanceof MentalMap) {
                this.mapasAtuais.add((MentalMap) mat);
            }
        }

        telaListagem.setVisible(false);
        telaMapa.setVisible(true);
        lblTituloMapaAberto.setText("Quadro de Estudos: " + disciplina);
        
        Platform.runLater(this::desenharMapaCompleto);
    }

    @FXML
    public void voltarParaListagem() {
        telaMapa.setVisible(false); 
        telaListagem.setVisible(true);
        mapasAtuais.clear(); nodeSelecionado = null; viewSelecionada = null;
        carregarListagem(); 
    }

    @FXML
    public void adicionarTopico() {
        String novoNome = txtNovoTopico.getText().trim();
        if (novoNome.isEmpty() || disciplinaAtual.isEmpty()) return;
        
        if (nodeSelecionado != null) {
        
            nodeSelecionado.addChild(novoNome);
        } else {
          
            MentalMap novo = new MentalMap(novoNome, disciplinaAtual);
            gerenciadorConteudo.adicionarMaterial(novo);
            mapasAtuais.add(novo);
        }
        txtNovoTopico.clear();
        desenharMapaCompleto(); 
    }

    @FXML
    public void reorganizarMapa() { 
        if (!mapasAtuais.isEmpty()) desenharMapaCompleto(); 
    }

    private void desenharMapaCompleto() {
        nodeViews.clear(); paneDesenho.getChildren().clear();
        deselecionarNo(); 
        
        
        paneDesenho.setOnMouseClicked(e -> {
            if (e.getTarget() == paneDesenho) {
                deselecionarNo();
            }
        });

        double cx = paneDesenho.getWidth() > 0 ? paneDesenho.getWidth() / 2.0 : 400;
        double cy = paneDesenho.getHeight() > 0 ? paneDesenho.getHeight() / 2.0 : 300;
        
        if (mapasAtuais.size() == 1) {
            
            desenharMapaDiametral(mapasAtuais.get(0).getRoot(), cx, cy, 0, 360, 0, null, 0);
        } else if (mapasAtuais.size() > 1) {

            double fatia = 360.0 / mapasAtuais.size();
            for (int i = 0; i < mapasAtuais.size(); i++) {
                desenharMapaDiametral(mapasAtuais.get(i).getRoot(), cx, cy, i * fatia, (i + 1) * fatia, 1, null, 0);
            }
        }
    }

    private void deselecionarNo() {
        if (viewSelecionada != null) {
            Circle ant = (Circle) viewSelecionada.getChildren().get(0);
            ant.getStyleClass().remove("circulo-no-sel");
        }
        nodeSelecionado = null; 
        viewSelecionada = null;
        boxEdicao.setDisable(true);
        lblNomeTopico.setText("Clique numa área vazia para adicionar uma nova raiz");
        txtConteudoTopico.clear();
        txtNovoTopico.setPromptText("Novo Tópico Central");
    }

    private StackPane desenharMapaDiametral(MapNode no, double cx, double cy, double angInicio, double angFim, int nivel, StackPane viewPai, double raioPai) {
        double x = cx, y = cy;
        
        if (nivel > 0) {
            double angMeio = angInicio + (angFim - angInicio) / 2.0;
            double raio = nivel * 130.0; 
            x = cx + raio * Math.cos(Math.toRadians(angMeio));
            y = cy + raio * Math.sin(Math.toRadians(angMeio));
        }

        // Se NÃO tem pai, é uma raiz independente!
        boolean isRaiz = (viewPai == null);
        double raioAtual = isRaiz ? 45 : 30; 

        StackPane viewAtual = renderizarNo(no, isRaiz, x, y, raioAtual);
        
        // Desenha as linhas conectando, EXCETO nas raízes (elas flutuam sozinhas)
        if (viewPai != null) {
            desenharLinhaElastica(viewPai, viewAtual, raioPai, raioAtual);
        }

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
        circulo.getStyleClass().add("circulo-no"); 
        circulo.setStrokeWidth(isRaiz ? 3 : 1.5);

        Label lbl = new Label(no.getName()); 
        lbl.getStyleClass().add("texto-padrao"); 
        lbl.setWrapText(true); 
        lbl.setMaxWidth(isRaiz ? 85 : 60); 
        lbl.setAlignment(Pos.CENTER);
        
        if (isRaiz) lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        view.getChildren().addAll(circulo, lbl);
        view.setLayoutX(x - raio); view.setLayoutY(y - raio);
        view.setCursor(javafx.scene.Cursor.HAND);

        view.setOnMousePressed(e -> {
            view.setUserData(new double[]{view.getLayoutX() - e.getSceneX(), view.getLayoutY() - e.getSceneY()});
            selecionarNo(no, view, circulo);
        });
        view.setOnMouseDragged(e -> {
            double[] offset = (double[]) view.getUserData();
            view.setLayoutX(e.getSceneX() + offset[0]); 
            view.setLayoutY(e.getSceneY() + offset[1]);
        });

        nodeViews.put(no, view); paneDesenho.getChildren().add(view); 
        return view;
    }

    private void desenharLinhaElastica(StackPane v1, StackPane v2, double r1, double r2) {
        Line linha = new Line();
        linha.getStyleClass().add("linha-mapa"); 
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
        circulo.getStyleClass().add("circulo-no-sel");

        boxEdicao.setDisable(false);
        lblNomeTopico.setText("Tópico: " + no.getName());
        txtConteudoTopico.setText(no.getDefinition() != null ? no.getDefinition() : "");
        txtNovoTopico.setPromptText("Novo Subtópico");
    }

    @FXML
    public void salvarConteudoNo() {
        if (nodeSelecionado != null) {
            nodeSelecionado.setDefinition(txtConteudoTopico.getText());
            if (estatisticas != null) {
                estatisticas.adicionarPontosBazinga(10);
                Platform.runLater(() -> telaPrincipal.atualizarNivel());
            }
        }
    }
    
    @FXML
    public void removerNo() {
        if (nodeSelecionado != null && !mapasAtuais.isEmpty()) {
            boolean isRoot = false;
            MentalMap mapaAlvo = null;

            for (MentalMap m : mapasAtuais) {
                if (m.getRoot() == nodeSelecionado) {
                    isRoot = true; mapaAlvo = m; break;
                }
            }

            if (isRoot) {
                mapasAtuais.remove(mapaAlvo);
                gerenciadorConteudo.obterMateriaisPorDisciplina(disciplinaAtual).remove(mapaAlvo);
            } else {
                
                for (MentalMap m : mapasAtuais) m.removerNo(nodeSelecionado.getName());
            }
            desenharMapaCompleto();
        }
    }
}