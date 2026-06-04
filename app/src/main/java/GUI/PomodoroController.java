package GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import logica.TimerPomodoro;

public class PomodoroController {
    @FXML private Label lblTempo;
    @FXML private Label lblSessoes;   // ← novo label no FXML
    @FXML private Button btnIniciar;
    @FXML private Button btnPausar;

    private TimerPomodoro timerPomodoro = new TimerPomodoro();
    private Timeline timeline;
    private int segundosRestantes;
    private boolean pausado = false;   // ← controla se está pausado

    @FXML
    public void initialize() {
        // Configura o estado inicial dos botões
        btnPausar.setDisable(true);
        segundosRestantes = timerPomodoro.getTempoAtual();
        atualizarLabel();
    }

    @FXML
    public void cliqueIniciar() {
        // Se estava pausado, apenas retoma — não cria um novo timeline
        if (pausado) {
            pausado = false;
            timeline.play();
            btnIniciar.setDisable(true);
            btnPausar.setDisable(false);
            return;
        }

        // Se já há um timer rodando, ignora
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return;
        }

        // Inicia nova sessão
        segundosRestantes = timerPomodoro.getTempoAtual();
        atualizarLabel();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            segundosRestantes--;
            atualizarLabel();

            if (segundosRestantes <= 0) {
                timeline.stop();
                sessaoConcluida();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        btnIniciar.setDisable(true);
        btnPausar.setDisable(false);
    }

    @FXML
    public void cliquePausar() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            pausado = true;
            timeline.pause();
            btnIniciar.setDisable(false);   // "Iniciar" vira o botão de Continuar
            btnIniciar.setText("Continuar");
            btnPausar.setDisable(true);
        }
    }

    private void sessaoConcluida() {
        timerPomodoro.alternaCiclo();   // alterna foco ↔ pausa e incrementa ciclos
        atualizarLabelSessoes();

        String mensagem = timerPomodoro.isEmfoco()
            ? "Pausa acabou! Hora de focar novamente. 🎯"
            : "Sessão concluída! Tire uma pausa merecida. ☕";

        String titulo = timerPomodoro.isEmfoco() ? "Fim da Pausa" : "Pomodoro Concluído!";

        // Notificação visual
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();   // show() não bloqueia; showAndWait() bloquearia o timer

        // Prepara o próximo ciclo automaticamente
        segundosRestantes = timerPomodoro.getTempoAtual();
        atualizarLabel();
        btnIniciar.setDisable(false);
        btnIniciar.setText("Iniciar");
        btnPausar.setDisable(true);
        pausado = false;
    }

    private void atualizarLabel() {
        int min = segundosRestantes / 60;
        int seg = segundosRestantes % 60;
        lblTempo.setText(String.format("%02d:%02d", min, seg));
    }

    private void atualizarLabelSessoes() {
        lblSessoes.setText("Sessões concluídas: " + timerPomodoro.getCiclos());
    }
}