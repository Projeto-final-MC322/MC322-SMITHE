package modelo;

import modelo.Exportar;

public class Resumo extends MaterialDeEstudo implements Exportar {
    private String conteudo;

    public Resumo(String titulo, String disciplina, String conteudo){
        super(titulo, disciplina);
        this.conteudo = conteudo;
    }
    @Override
    public void exibirConteudo(){
        System.out.println("=== RESUMO: " + this.getTitulo() + " ===");
        System.out.println("Disciplina: " + this.getDisciplina());
        System.out.println("-------------------------------");
        System.out.println(" ");
        System.out.println((this.conteudo));
    }
    public String getConteudo(){
        return conteudo;
    }
    public void setConteudo(String novo_conteudo){
        this.conteudo = novo_conteudo;
    }
    @Override
    public void exportarArquivo(String path){
        System.out.println("Iniciando varredura e exportação do resumo...");
        System.out.println("Salvando em: " + path + "/" + this.getTitulo() + ".txt");
        System.out.println("Bazinga! Conhecimento salvo em disco com sucesso.");
    }

}
