package save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logica.GerenciadorDeConteudo;
import modelo.MaterialDeEstudo;
import modelo.MentalMap;
import modelo.Resumo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSON_Conteudo {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private class Wrapper {
        List<MentalMap> mapas = new ArrayList<>();
        List<Resumo> resumos = new ArrayList<>();
    }

    public void salvarDados(GerenciadorDeConteudo gerenciador, String path) {
        Wrapper wrapper = new Wrapper();
        
        for (MaterialDeEstudo mat : gerenciador.obterTodososMateriais()) {
            if (mat instanceof MentalMap) {
                wrapper.mapas.add((MentalMap) mat);
            } else if (mat instanceof Resumo) {
                wrapper.resumos.add((Resumo) mat);
            }
        }

        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(wrapper, writer);
            System.out.println("Ficheiro de Mapas/Resumos atualizado em: " + path);
        } catch (IOException e) {
            System.out.println("Erro ao gravar conteúdo: " + e.getMessage());
        }
    }

    public void carregarDados(GerenciadorDeConteudo gerenciador, String path) {
        try (FileReader reader = new FileReader(path)) {
            Wrapper wrapper = gson.fromJson(reader, Wrapper.class);
            if (wrapper != null) {
                gerenciador.limparMemoria();
                
                if (wrapper.mapas != null) {
                    for (MentalMap m : wrapper.mapas) gerenciador.adicionarMaterial(m);
                }
                if (wrapper.resumos != null) {
                    for (Resumo r : wrapper.resumos) gerenciador.adicionarMaterial(r);
                }
            }
            System.out.println("Árvores de Mapas Mentais restauradas com sucesso!");
        } catch (Exception e) {
            System.out.println("A preparar um Quadro Branco (nenhum mapa anterior encontrado).");
        }
    }
}
