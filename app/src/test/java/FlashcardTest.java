package modelo;

import java.time.LocalDate;
import logica.Revisao_Espacada;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FlashcardTest {

    @Test
    public void testCriaFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        assertEquals("titulo", fcard.getTitulo());
        assertEquals("disciplina", fcard.getDisciplina());
        assertEquals("frente", fcard.getFrente());
        assertEquals("verso", fcard.getVerso());

        assertEquals(0, fcard.getRepeticoes());
        assertEquals(LocalDate.now(), fcard.getDataProximaRevisao());

    }

    @Test
    public void testErroFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        // erro -> desempenho = 0
        assertDoesNotThrow(() -> fcard.calcularProximaRevisao(0));
        assertNotNull(fcard.getDataProximaRevisao());

    }

    @Test
    public void testAcertoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        // acerto -> desempenho = 5
        assertDoesNotThrow(() -> fcard.calcularProximaRevisao(5));
        // verifica se a data da proxima revisao nao ficou travada no mesmo dia
        assertTrue(fcard.getDataProximaRevisao().isAfter(LocalDate.now()));

    }

    @Test
    public void testEditarFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        fcard.setFrente("frente nova");
        fcard.setVerso("verso novo");

        assertEquals("frente nova", fcard.getFrente());
        assertEquals("verso novo", fcard.getVerso());
    }

    @Test
    public void testExibirConteudoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");
        assertDoesNotThrow(() -> fcard.exibirConteudo());

    }

    @Test
    public void testEditarMetricasFlashcard() {

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        fcard.setRepeticoes(10);
        fcard.setFacilidade(10);
        fcard.setIntervalos(10);

        assertEquals(10, fcard.getRepeticoes());
        assertEquals(10, fcard.getFacilidade());
        assertEquals(10, fcard.getIntervalosDias());

    }

    @Test
    public void testMudarDataRevisaoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");
        LocalDate nova_data = LocalDate.now().plusDays(2);

        fcard.setDataProximaRevisao(nova_data);
        assertEquals(nova_data, fcard.getDataProximaRevisao());

    }
}