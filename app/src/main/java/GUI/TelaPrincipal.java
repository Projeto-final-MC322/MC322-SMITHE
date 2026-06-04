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
    @FXML private Label lblNivel; // Mostrador de pontos

    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas;

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

    @FXML public void abrirPomodoro() { carregarTela("PomodoroView.fxml"); }
    @FXML public void abrirFlashcards() { carregarTela("FlashcardView.fxml"); }
    @FXML public void abrirResumos() { carregarTela("ResumoView.fxml"); }
    @FXML public void abrirMapasMentais() { carregarTela("MapaMentalView.fxml"); }
    @FXML public void abrirRevisoes() { carregarTela("RevisoesView.fxml"); } // Nova tela!

    private void carregarTela(String arquivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + arquivoFxml));

            Parent novaTela = loader.load(); 

            Object controlador = loader.getController();
            
            // Injeta o gerenciador e a própria tela principal para atualizar pontos
            if(controlador instanceof FlashcardController) {
                ((FlashcardController) controlador).setGerenciador(this.gerenciador);
                ((FlashcardController) controlador).setEstatisticas(this.estatisticas, this);
            } else if(controlador instanceof MapaMentalController) {
                ((MapaMentalController) controlador).setGerenciador(this.gerenciador);
                ((MapaMentalController) controlador).setEstatisticas(this.estatisticas, this);
            } else if(controlador instanceof RevisoesController) {
                ((RevisoesController) controlador).setup(this.gerenciador, this.estatisticas, this);
            }
            
            Conteudo.getChildren().clear(); 
            Conteudo.getChildren().add(novaTela); 
        } catch (IOException e) { e.printStackTrace(); }
    }
}