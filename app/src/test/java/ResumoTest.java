import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Resumo;

public class ResumoTest {

    @Test
    public void deveCriarResumoComCamposCorretos() {
        Resumo resumo = new Resumo(
            "Pilares da POO", 
            "Computação", 
            "Herança, Encapsulamento, Polimorfismo e Abstração."
        );

        assertEquals("Pilares da POO", resumo.getTitulo());
        assertEquals("Computação", resumo.getDisciplina());
    }
}