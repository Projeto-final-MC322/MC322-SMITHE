package save;
import logica.GerenciadorDeRevisao;

import modelo.Flashcard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JSON implements Armazenamento {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @Override
    public void salvarDados(GerenciadorDeRevisao gerenciador, String path){
        System.out.println("Iniciando...");

        try(FileWriter writter = new FileWriter(path)){
            List<Flashcard> todososCartoes = gerenciador.obter_todos_os_cartoes();

            gson.toJson(todososCartoes, writter);

            System.out.println("Oh yeah! Ficheiro salvo com sucesso em: " + path);
        } catch (IOException e) {
            System.out.println("Falha ao gravar dados no disco: " + e.getMessage());
        }
    }

    @Override
    public void carregarDados(GerenciadorDeRevisao gerenciador, String path){
        try(FileReader reader = new FileReader(path)) {
            Type tipo = new TypeToken<List<Flashcard>>(){}.getType();
            List<Flashcard> cartoesRecuperados = gson.fromJson(reader, tipo);

            if(cartoesRecuperados != null){
                for(Flashcard card : cartoesRecuperados){
                    gerenciador.adicionarCard(card);
                }
            }
            System.out.println("Banco de dados mental restaurado com sucesso!");
        }catch (IOException e){
            System.out.println("Arquivo não localizado ou corrompido. Inicializando base limpa.");
        }
    }
    public void excluirArquivo(String path){
        File arquivo = new File(path);

        if(arquivo.exists()){
            if(arquivo.delete()){
                System.out.println("Arquivo '" + path + "' deletado com sucesso!");
            } else{
                System.out.println("Erro");
            }
        }else{
            System.out.println("Arquivo procurado não está disponível para a exclusão");
        }
    }
}
