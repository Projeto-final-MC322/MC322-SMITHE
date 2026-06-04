package save;

import com.google.gson.Gson;
import modelo.EstatisticaDesempenho;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class JSON_Estatistica {
    private Gson gson;

    public JSON_Estatistica(){
        this.gson = new Gson();
    }

    public void salvarEstatistica(EstatisticaDesempenho estatisticas, String path){
        try(FileWriter writter = new FileWriter(path)){
            gson.toJson(estatisticas, writter);
            System.out.println("Estatística gravadas com sucesso!");
        }catch (IOException e){
            System.out.println("Erro ao guardar estatísticas: " + e.getMessage());
        }
    }
    public EstatisticaDesempenho carrega_estatistica(String path){
        File ficheiro = new File(path);

        if(!ficheiro.exists()){
            return new EstatisticaDesempenho();
        }
        try(FileReader reader = new FileReader(ficheiro)){
            return gson.fromJson(reader, EstatisticaDesempenho.class);
        }catch(IOException e){
            System.out.println("Erro ao carregar as estatísticas. A iniciar do zero.");
            return new EstatisticaDesempenho();
        }
    }
}
