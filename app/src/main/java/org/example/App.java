package org.example;

import logica.GerenciadorDeConteudo;
import modelo.Flashcard;
import modelo.Resumo;

public class App {
    public static void main(String[] args){
        System.out.println("================================");
        System.out.println("      INICIALIZANDO SMITHE      ");
        System.out.println("================================");

        GerenciadorDeConteudo gerenciador = new GerenciadorDeConteudo();

        // Flashcard sistema_card = new Flashcard(); // passar os parametros

        System.out.println("Carregando meus materiais de estudo...");
        gerenciador.listarMateriais();
    }
}
