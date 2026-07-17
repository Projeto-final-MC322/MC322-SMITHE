import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import logica.TimerPomodoro;

public class PomodoroTest {

    @Test
    public void deveConterValoresIniciaisCorretos() {
        TimerPomodoro timer = new TimerPomodoro();
        
        assertEquals(1500, timer.getTempoAtual(), "O tempo inicial de foco deve ser de 1500 segundos");
        assertEquals(0, timer.getCiclos(), "O número de ciclos iniciais deve ser 0");
        assertTrue(timer.isEmfoco(), "O timer deve inicializar em estado de foco");
    }

    @Test
    public void testarAlternanciaDeCiclos() {
        TimerPomodoro timer = new TimerPomodoro();
        timer.alternaCiclo();
        
        assertFalse(timer.isEmfoco(), "O timer deve sair do estado de foco ao alternar o ciclo");
        assertEquals(1, timer.getCiclos(), "O ciclo deve ser incrementado para 1 após finalizar o foco");
        assertEquals(300, timer.getTempoAtual(), "O tempo atual deve mudar para 300 segundos (5 min de pausa)");
        timer.alternaCiclo();
        
        assertTrue(timer.isEmfoco(), "O timer deve retornar ao estado de foco após a pausa");
        assertEquals(1, timer.getCiclos(), "O ciclo não deve ser incrementado ao sair da pausa");
        assertEquals(1500, timer.getTempoAtual(), "O tempo atual deve voltar a 1500 segundos (25 min de foco)");
    }
}