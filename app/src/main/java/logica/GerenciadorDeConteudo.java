package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.MaterialDeEstudo;
import modelo.MentalMap;

public class GerenciadorDeConteudo {
    
    // IMPORTANTE: O 'static' garante que a memória não se perde ao trocar de ecrã
    private static Map<String, List<MaterialDeEstudo>> materiais_das_disciplinas = new LinkedHashMap<>();

    public GerenciadorDeConteudo() {}

    // ESTE É O MÉTODO QUE O JAVA ESTAVA A PEDIR:
    public void limparMemoria() {
        materiais_das_disciplinas.clear();
    }

    public void adicionarMaterial(MaterialDeEstudo mat) {
        if (mat != null) {
            String disciplina = mat.getDisciplina();
            materiais_das_disciplinas.putIfAbsent(disciplina, new ArrayList<>());
            materiais_das_disciplinas.get(disciplina).add(mat);
            System.out.println("Material '" + mat.getTitulo() + "' salvo em " + disciplina);
        }
    }

    public List<MaterialDeEstudo> obterMateriaisPorDisciplina(String disciplina) {
        List<MaterialDeEstudo> lista = materiais_das_disciplinas.getOrDefault(disciplina, new ArrayList<>());
        Collections.sort(lista, (m1, m2) -> m2.getData_adicao().compareTo(m1.getData_adicao()));
        return lista;
    }

    public MentalMap obterMapaMentalDaDisciplina(String disciplina) {
        List<MaterialDeEstudo> lista = materiais_das_disciplinas.getOrDefault(disciplina, new ArrayList<>());
        for (MaterialDeEstudo mat : lista) {
            if (mat instanceof MentalMap) {
                return (MentalMap) mat;
            }
        }
        return null;
    }

    public List<MaterialDeEstudo> obterTodososMateriais() {
        List<MaterialDeEstudo> todos = new ArrayList<>();
        for (List<MaterialDeEstudo> lista : materiais_das_disciplinas.values()) {
            todos.addAll(lista);
        }
        return todos;
    }
}