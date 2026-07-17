import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;

// Importações baseadas na estrutura do seu projeto
import logica.GerenciadorDeConteudo;
import modelo.MaterialDeEstudo;
import modelo.Resumo;
import save.JSON_Conteudo;

public class GerenciadorIntegracaoTest {

    @TempDir
    Path pastaTemporaria;
    
    private String caminhoArquivoTemp;

    @BeforeEach
    public void setup() {
        caminhoArquivoTemp = pastaTemporaria.resolve("banco_teste.json").toString();
    }

    @Test
    public void deveSalvarERecuperarResumoComSucesso() {
        JSON_Conteudo jsonConteudo = new JSON_Conteudo();

        
        GerenciadorDeConteudo gerenciadorEscrita = new GerenciadorDeConteudo();
        
        Resumo resumoOriginal = new Resumo("Pilares da POO", "Computação", "Texto do resumo...");
        
        gerenciadorEscrita.adicionarMaterial(resumoOriginal);
        
        jsonConteudo.salvarDados(gerenciadorEscrita, caminhoArquivoTemp);

        
        
        GerenciadorDeConteudo gerenciadorLeitura = new GerenciadorDeConteudo();
        
        jsonConteudo.carregarDados(gerenciadorLeitura, caminhoArquivoTemp);
        
        
        List<MaterialDeEstudo> conteudosCarregados = (List<MaterialDeEstudo>) gerenciadorLeitura.obterTodososMateriais();
        
        
        assertFalse(conteudosCarregados.isEmpty(), "A lista não deveria estar vazia após carregar o arquivo.");
        
        
        Resumo resumoRecuperado = (Resumo) conteudosCarregados.get(0);
        assertEquals("Pilares da POO", resumoRecuperado.getTitulo(), "O título do resumo recuperado deve ser idêntico ao salvo.");
        assertEquals("Computação", resumoRecuperado.getDisciplina()); 
    }
}