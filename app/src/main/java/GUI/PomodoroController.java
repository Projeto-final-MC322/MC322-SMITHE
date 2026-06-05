package GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import modelo.EstatisticaDesempenho;

public class PomodoroController {

    @FXML private Label lblTipoSessao;
    @FXML private Label lblTempo;
    @FXML private ProgressBar progressTimer;
    @FXML private Label lblSessoes;
    @FXML private Button btnIniciar;
    @FXML private Button btnPausar;

    private Timeline timeline;
    
    private int tempoTotal = 25 * 60; 
    private int tempoRestante = 25 * 60; 
    private int sessoesConcluidas = 0;
    private boolean isFoco = true; 

    private EstatisticaDesempenho estatisticas;
    private TelaPrincipal telaPrincipal;

    public void setEstatisticas(EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
    }

    @FXML
    public void initialize() {
        atualizarLabels();
        btnPausar.setDisable(true);
    }

    @FXML
    public void cliqueIniciar() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) return;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoRestante--;
            atualizarLabels();

            if (tempoRestante <= 0) {
                finalizarCiclo();
            }
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        btnIniciar.setDisable(true);
        btnPausar.setDisable(false);
    }

    @FXML
    public void cliquePausar() {
        if (timeline != null) {
            timeline.pause();
        }
        btnIniciar.setDisable(false);
        btnPausar.setDisable(true);
    }

    private void finalizarCiclo() {
        timeline.stop();
        btnIniciar.setDisable(false);
        btnPausar.setDisable(true);

        if (isFoco) {
            sessoesConcluidas++;
            lblSessoes.setText("Sessões concluídas: " + sessoesConcluidas);
            
            if (estatisticas != null) {
                estatisticas.adicionarPontosBazinga(20); 
                Platform.runLater(() -> telaPrincipal.atualizarNivel());
            }

            mostrarAviso("Sessão Concluída! 🎉", "Foco incrível! Você completou uma sessão e ganhou +20 Bazingas. Hora de relaxar.");
            
            
            isFoco = false;
            tempoTotal = 5 * 60;
            tempoRestante = tempoTotal;
            lblTipoSessao.setText("○ SESSÃO DE DESCANSO");
        } else {
            mostrarAviso("Descanso Terminado! 🚀", "As baterias estão recarregadas. Vamos voltar aos estudos?");
            
            isFoco = true;
            tempoTotal = 25 * 60;
            tempoRestante = tempoTotal;
            lblTipoSessao.setText("● SESSÃO DE FOCO");
        }
        atualizarLabels();
    }

    private void atualizarLabels() {
        int minutos = tempoRestante / 60;
        int segundos = tempoRestante % 60;
        lblTempo.setText(String.format("%02d:%02d", minutos, segundos));
        
        double progresso = 1.0 - ((double) tempoRestante / tempoTotal);
        progressTimer.setProgress(progresso);
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }
}