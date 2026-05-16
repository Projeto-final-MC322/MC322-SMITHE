package modelo;
import java.util.ArrayList;
import java.util.List;

import modelo.MaterialDeEstudo;



public class GerenciadorDeConteudo {
    private List<MaterialDeEstudo> materiais;
    public GerenciadorDeConteudo(){
        this.materiais = new ArrayList<>();
    }
    public void adicionarMaterial(MaterialDeEstudo mat){
        this.materiais.add(mat);
        System.out.println("Material '" + mat.getTitulo() + "' adicionado com sucesso ao deck!");
    }
    public void listarMateriais(){
        System.out.println("\n--- SUA FORTALEZA DO CONHECIMENTO ---");
        int contador = 1;
        for(MaterialDeEstudo mat : materiais){
            System.out.println(contador + " - " + mat.getTitulo() + " (" + material.getDisciplina() + ")");
            contador++;
        }
        System.out.println("---------------------");
    }
}
