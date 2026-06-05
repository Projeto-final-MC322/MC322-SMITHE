package save;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import logica.GerenciadorDeRevisao;
import modelo.Flashcard;

public class JSON {
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    if (value == null) out.nullValue();
                    else out.value(value.toString());
                }
                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return LocalDate.parse(in.nextString());
                }
            })
            .create();

    public void salvarDados(GerenciadorDeRevisao gerenciador, String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(gerenciador.obter_todos_os_cartoes(), writer);
            System.out.println("Oh yeah! Ficheiro salvo com sucesso em: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    public void carregarDados(GerenciadorDeRevisao gerenciador, String caminhoArquivo) {
        try (FileReader reader = new FileReader(caminhoArquivo)) {
            Type tipoLista = new TypeToken<List<Flashcard>>() {}.getType();
            List<Flashcard> flashcards = gson.fromJson(reader, tipoLista);
            if (flashcards != null) {
                for (Flashcard f : flashcards) {
                    gerenciador.adicionarCard(f);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
        }
    }
}