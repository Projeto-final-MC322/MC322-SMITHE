package logica;
import modelo.MaterialDeEstudo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import save.Armazenamento;

public class GerenciadorDeConteudo {
    private Map<String, List<MaterialDeEstudo>> materiais_das_disciplinas;

    public GerenciadorDeConteudo(){
        this.materiais_das_disciplinas = new LinkedHashMap<>();
    }

    public void adicionarMaterial(MaterialDeEstudo mat){
        if(mat != null){
            String disciplina = mat.getDisciplina();
            this.materiais_das_disciplinas.putIfAbsent(disciplina, new ArrayList<>());
            this.materiais_das_disciplinas.get(disciplina).add(mat);
            System.out.println("Material " + mat.getTitulo() + "adicionado com sucesso ao deck!");
        }
    }
    
    public void listarMateriais(){
        System.out.println("\n--- SUA FORTALEZA DO CONHECIMENTO ---");
        int contador = 1;
        for(Map.Entry<String, List<MaterialDeEstudo>> input : materiais_das_disciplinas.entrySet()){
            System.out.println("Disciplina: " + entrada.getKey());
            for(MaterialDeEstudo mat : entrada.getValue()){
                System.out.println(" " + contador + " - " +  mat.getTitulo());
                contador++;
            }
        }
        System.out.println("---------------------");
    }
    public List<MaterialDeEstudo>   obterTodososMateriais(){
        List<MaterialDeEstudo> todos = new ArrayList<>();
        for(List<MaterialDeEstudo> lista : materiais_das_disciplinas.values()){
            all.addAll(lista);
        }
        return todos;
    }
}
