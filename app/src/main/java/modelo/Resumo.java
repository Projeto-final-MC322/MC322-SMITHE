package modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Resumo extends MaterialDeEstudo {
    private String conteudo;

    public Resumo(String titulo, String disciplina, String conteudo) {
        super(titulo, disciplina); 
        this.conteudo = conteudo;
    }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    @Override
    public void exibirConteudo() {
        System.out.println("Resumo: " + titulo + "\nConteúdo: " + conteudo);
    }

    // XPORTAÇÃO PARA .TXT
    public void exportarParaTXT() {
        try {
            // Cria uma pasta chamada "Exportacoes" se ela ainda não existir
            File diretorio = new File("Exportacoes");
            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            // Limpa o nome do ficheiro 
            String nomeSeguro = this.titulo.replaceAll("\\s+", "_");
            String caminhoArquivo = "Exportacoes/" + this.disciplina + "_" + nomeSeguro + ".txt";
            
            // Escreve os dados no ficheiro físico
            FileWriter writer = new FileWriter(caminhoArquivo);
            writer.write("==================================================\n");
            writer.write(" DISCIPLINA: " + this.disciplina + "\n");
            writer.write(" TÍTULO: " + this.titulo + "\n");
            writer.write(" DATA DE CRIAÇÃO: " + this.data_adicao + "\n");
            writer.write("==================================================\n\n");
            writer.write(this.conteudo);
            writer.close();

            System.out.println("Sucesso! Resumo gerado em: " + caminhoArquivo);
            
        } catch (IOException e) {
            System.err.println("Erro ao tentar gerar o ficheiro TXT.");
            e.printStackTrace();
        }
    }
}