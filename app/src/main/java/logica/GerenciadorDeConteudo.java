package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.MaterialDeEstudo;
import modelo.MentalMap;

public class GerenciadorDeConteudo {
    private Map<String, List<MaterialDeEstudo>> materiais_das_disciplinas;

    public GerenciadorDeConteudo() {
        this.materiais_das_disciplinas = new LinkedHashMap<>();
    }

    public void adicionarMaterial(MaterialDeEstudo mat) {
        if (mat != null) {
            String disciplina = mat.getDisciplina();
            this.materiais_das_disciplinas.putIfAbsent(disciplina, new ArrayList<>());
            this.materiais_das_disciplinas.get(disciplina).add(mat);
            System.out.println("Material '" + mat.getTitulo() + "' salvo em " + disciplina);
        }
    }

    // Retorna todos os materiais de uma disciplina ordenados do mais NOVO para o mais VELHO
    public List<MaterialDeEstudo> obterMateriaisPorDisciplina(String disciplina) {
        List<MaterialDeEstudo> lista = materiais_das_disciplinas.getOrDefault(disciplina, new ArrayList<>());
        
        // Ordena pela data de adição (Decrescente)
        Collections.sort(lista, (m1, m2) -> m2.getData_adicao().compareTo(m1.getData_adicao()));
        return lista;
    }

    // A MÁGICA DA INTERCONEXÃO: Busca se já existe um Mapa Mental para esta disciplina
    public MentalMap obterMapaMentalDaDisciplina(String disciplina) {
        List<MaterialDeEstudo> lista = materiais_das_disciplinas.getOrDefault(disciplina, new ArrayList<>());
        for (MaterialDeEstudo mat : lista) {
            if (mat instanceof MentalMap) {
                return (MentalMap) mat; // Retorna o mapa gigante já existente
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