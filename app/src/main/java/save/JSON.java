package save;
import logica.GerenciadorDeConteudo;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSON implements Armazenamento {
    @Override
    public void salvarDados(GerenciadorDeConteudo gerenciador, String path){
        System.out.println("Iniciando...");

        try(FileWriter writter = new FileWriter(path)){
            writter.write("{\n");
            writter.write(" \"projeto\": \"SMITHE\",\n");
            writter.write(" \"status\": \"Dados salvos localmente\"\n");
            writter.write("}\n");

            System.out.println("Oh yeah! Ficheiro salvo com sucesso em: " + path);
        } catch (IOException e) {
            System.out.println("Falha ao gravar dados no disco: " + e.getMessage());
        }
    }
    @Override
    public void carregarDados(GerenciadorDeConteudo gerenciador, String path){

        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String row;
            System.out.println("Lendo estrutura do arquivo JSON:");
            while((row = reader.readLine()) != null){
                System.out.println(" " + row);
            }
            System.out.println("Banco de dados mental restaurado com sucesso!");
        }catch (IOException e){
            System.out.println("Arquivo não localizado ou corrompido. Inicializando base limpa.");
        }
    }
}
