package modelo;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MentalMapTest {

    @Test
    public void testCriaMentalMap() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        assertEquals("titulo", map.getTitulo());
        assertEquals("disciplina", map.getDisciplina());
        assertEquals("titulo", map.getRoot().getName());

        assertEquals(0, map.getRepeticoes());
        assertEquals(2.5, map.getFacilidade());
        assertEquals(0, map.getIntervalosDias());
        assertEquals(LocalDate.now(), map.getDataProximaRevisao());
    }

    @Test
    public void testAdicionarNo() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        MapNode novo = map.adicionarNo("titulo", "filho1");

        assertNotNull(novo);
        assertEquals("filho1", novo.getName());
        assertNotNull(map.getRoot().findByName("filho1"));
    }

    @Test
    public void testAdicionarNoComPaiInexistente() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        MapNode novo = map.adicionarNo("naoExiste", "filho1");

        assertNull(novo);
    }

    @Test
    public void testRemoverNo() {
        MentalMap map = new MentalMap("titulo", "disciplina");
        map.adicionarNo("titulo", "filho1");

        boolean removido = map.removerNo("filho1");

        assertTrue(removido);
        assertNull(map.getRoot().findByName("filho1"));
    }

    @Test
    public void testRemoverNoAninhado() {
        MentalMap map = new MentalMap("titulo", "disciplina");
        map.adicionarNo("titulo", "filho1");
        map.adicionarNo("filho1", "neto1");

        boolean removido = map.removerNo("neto1");

        assertTrue(removido);
        assertNull(map.getRoot().findByName("neto1"));
    }

    @Test
    public void testNaoRemoverRaiz() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        boolean removido = map.removerNo("titulo");

        assertFalse(removido);
        assertNotNull(map.getRoot());
    }

    @Test
    public void testRemoverNoInexistente() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        boolean removido = map.removerNo("naoExiste");

        assertFalse(removido);
    }

    @Test
    public void testPrecisaRevisarHojeQuandoDataEhHoje() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        assertTrue(map.precisaRevisarHoje());
    }

    @Test
    public void testNaoPrecisaRevisarHojeQuandoDataEhFutura() {
        MentalMap map = new MentalMap("titulo", "disciplina");
        map.setDataProximaRevisao(LocalDate.now().plusDays(3));

        assertFalse(map.precisaRevisarHoje());
    }

    @Test
    public void testRevisarErro() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        // erro -> nota = 0
        assertDoesNotThrow(() -> map.revisar(0));
        assertNotNull(map.getDataProximaRevisao());
    }

    @Test
    public void testRevisarAcerto() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        // acerto -> nota = 5
        assertDoesNotThrow(() -> map.revisar(5));
        // verifica se a data da proxima revisao nao ficou travada no mesmo dia
        assertTrue(map.getDataProximaRevisao().isAfter(LocalDate.now()));
    }

    @Test
    public void testEditarMetricas() {
        MentalMap map = new MentalMap("titulo", "disciplina");

        map.setRepeticoes(10);
        map.setFacilidade(3.2);
        map.setIntervalos(10);

        assertEquals(10, map.getRepeticoes());
        assertEquals(3.2, map.getFacilidade());
        assertEquals(10, map.getIntervalosDias());
    }

    @Test
    public void testMudarDataRevisao() {
        MentalMap map = new MentalMap("titulo", "disciplina");
        LocalDate nova_data = LocalDate.now().plusDays(2);

        map.setDataProximaRevisao(nova_data);

        assertEquals(nova_data, map.getDataProximaRevisao());
    }

    @Test
    public void testExibirConteudo() {
        MentalMap map = new MentalMap("titulo", "disciplina");
        map.adicionarNo("titulo", "filho1");

        assertDoesNotThrow(() -> map.exibirConteudo());
    }
}