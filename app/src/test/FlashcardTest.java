package modelo;

import java.time.LocalDate;
import logica.Revisao_Espacada;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FlashcardTest {

    @Test
    public void testCriaFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        assertEquals("titulo", fcard.getTitulo);
        assertEquals("disciplina", fcard.getDisciplina);
        assertEquals("frente", fcard.getFrente);
        assertEquals("verso", fcard.getVerso);

        assertEquals(0, card.getRepeticoes());
        assertEquals(LocalDate.now(), card.getDataProximaRevisao());

    }

    @Test
    public void testErroFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        // erro -> desempenho = 0
        assertDoesNotThrow(() -> card.calcularProximaRevisao(0));
        assertNotNull(card.getDataProximaRevisao());

    }

    @Test
    public void testAcertoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        // acerto -> desempenho = 5
        assertDoesNotThrow(() -> card.calcularProximaRevisao(5));
        // verifica se a data da proxima revisao nao ficou travada no mesmo dia
        assertTrue(card.getDataProximaRevisao().isAfter(LocalDate.now()));

    }

    @Test
    public void testEditarFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        card.setFrente("frente nova");
        card.setVerso("verso novo");

        assertEquals("frente nova", card.getFrente());
        assertEquals("verso novo", card.getVerso());
    }

    @Test
    public void testExibirConteudoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");
        assertDoesNotThrow(() -> card.exibirConteudo());

    }

    @Test
    public void testEditarMetricasFlashcard() {

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");

        card.setRepeticoes(10);
        card.setFacilidade(10);
        card.setIntervalos(10);

        assertEquals(10, card.getRepeticoes());
        assertEquals(10, card.getFacilidade());
        assertEquals(10, card.getIntervalosDias());

    }

    @Test
    public void testMudarDataRevisaoFlashcard(){

        Flashcard fcard = new Flashcard("titulo", "disciplina", "frente", "verso");
        LocalDate nova_data = LocalDate.now().plusDays(2);

        card.setDataProximaRevisao(nova_data);
        assertEquals(nova_data, card.getDataProximaRevisao());

    }
}