package GUI;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;

public class TelaPrincipal {
    @FXML private BorderPane painelPrincipal;
    @FXML private StackPane Conteudo;
    @FXML private Label lblNivel;

    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas;
    private boolean modoEscuro = false;
    
    private Parent telaPomodoroCache = null;

    public void setGerenciador(GerenciadorDeRevisao gerenciador){ this.gerenciador = gerenciador; }

    public void setEstatisticas(EstatisticaDesempenho estatisticas){
        this.estatisticas = estatisticas;
        atualizarNivel();
    }

    public void atualizarNivel() {
        if(lblNivel != null && estatisticas != null) {
            lblNivel.setText("Nível " + estatisticas.getNivel() + " | " + estatisticas.getTotalBazingas() + " BZ");
        }
    }

    @FXML
    public void alternarTema() {
        modoEscuro = !modoEscuro;
        if (modoEscuro) {
            painelPrincipal.getScene().getRoot().getStyleClass().add("dark-theme");
        } else {
            painelPrincipal.getScene().getRoot().getStyleClass().remove("dark-theme");
        }
    }

    @FXML public void abrirPomodoro() { carregarTela("PomodoroView.fxml"); }
    @FXML public void abrirFlashcards() { carregarTela("FlashcardView.fxml"); }
    @FXML public void abrirResumos() { carregarTela("ResumoView.fxml"); }
    @FXML public void abrirMapasMentais() { carregarTela("MapaMentalView.fxml"); }
    @FXML public void abrirRevisoes() { carregarTela("RevisoesView.fxml"); }

    private void carregarTela(String arquivoFxml) {
        try {
            Parent novaTela;

            if (arquivoFxml.equals("PomodoroView.fxml") && telaPomodoroCache != null) {
                novaTela = telaPomodoroCache;
            } else {
                // Caso contrário (ou se for outro ecrã), carrega normalmente
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + arquivoFxml));
                novaTela = loader.load(); 
                
            Object controlador = loader.getController();
            if(controlador instanceof FlashcardController) {
                ((FlashcardController) controlador).setGerenciador(this.gerenciador);
                ((FlashcardController) controlador).setEstatisticas(this.estatisticas, this);
            } else if(controlador instanceof MapaMentalController) {
                ((MapaMentalController) controlador).setGerenciador(this.gerenciador);
                ((MapaMentalController) controlador).setEstatisticas(this.estatisticas, this);
            } else if(controlador instanceof RevisoesController) {
                ((RevisoesController) controlador).setup(this.gerenciador, this.estatisticas, this);
            } else if(controlador instanceof PomodoroController) {
                ((PomodoroController) controlador).setEstatisticas(this.estatisticas, this);
            } else if(controlador instanceof ResumoController) {
                ((ResumoController) controlador).setEstatisticas(this.estatisticas, this);
            }

            
                if (arquivoFxml.equals("PomodoroView.fxml")) {
                    telaPomodoroCache = novaTela;
                }
            }
            
            Conteudo.getChildren().clear(); 
            Conteudo.getChildren().add(novaTela); 
        } catch (IOException e) { e.printStackTrace(); }
    }
}