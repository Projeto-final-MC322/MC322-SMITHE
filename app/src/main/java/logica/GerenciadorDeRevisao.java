package logica;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modelo.Flashcard;

public class GerenciadorDeRevisao {
    private Map<String, List<Flashcard>> decks = new LinkedHashMap<>();

    public void adicionarCard(Flashcard card){
        if(card != null){
            String disciplina = card.getDisciplina();
            this.decks.putIfAbsent(disciplina, new ArrayList<>());
            this.decks.get(disciplina).add(card);
        }
    }

    public void criarNovoFlashcard(String titulo, String disciplina, String nova_frente, String novo_verso){
        Flashcard novo_cartao = new Flashcard(titulo, disciplina, nova_frente, novo_verso);
        this.adicionarCard(novo_cartao);
        System.out.println("Novo cartão registrado com sucesso!");
    }

    public List<Flashcard> obter_todos_os_cartoes(){
        List<Flashcard> todos = new ArrayList<>();
        for(List<Flashcard> lista : decks.values()){
            todos.addAll(lista);
        }
        return todos;
    }

    public List<Flashcard> obterCardsporDisciplina(String disciplina){
        return this.decks.getOrDefault(disciplina, new ArrayList<>());
    }

    public java.util.Set<String> obterDisciplinas(){
        return this.decks.keySet();
    }

    public void editarFlashcard(Flashcard cartao, String nf, String nv){
        if(cartao != null){
            cartao.setFrente(nf);
            cartao.setVerso(nv);
        }
    }

    public void excluirFlashcard(Flashcard cartao){
        if(cartao != null){
            String disciplina = cartao.getDisciplina();
            if(this.decks.containsKey(disciplina)){
                boolean removido = this.decks.get(disciplina).remove(cartao);
                if(removido){
                    System.out.println("Cartão excluído permanentemente!");
                    if(this.decks.get(disciplina).isEmpty()){
                        this.decks.remove(disciplina);
                    }
                }
            }
        }
    }

    public List<Flashcard> obtercards_hoje(){
        List<Flashcard> cartoes_filtrados = new ArrayList<>();

        for(List<Flashcard> lista : decks.values()){
            for(Flashcard card : lista) {
                // Utiliza a verificação segura de datas herdada do MaterialDeEstudo
                if(card.precisaRevisar()){
                    cartoes_filtrados.add(card);
                }
            }
        }
        return cartoes_filtrados;
    }

    public int getTamanhoDoDeck(){
        int total = 0;
        for(List<Flashcard> lista : decks.values()){
            total += lista.size();
        }
        return total;
    }

    public void avaliaFlashcard(Flashcard cartao, int nota){
        if(cartao != null){
            Revisao_Espacada algoritmo = new Revisao_Espacada();
            algoritmo.processa_revisao(cartao, nota);
            System.out.println("Revisão registrada! Próxima revisão em " + cartao.getData_proxima_revisao());
        }
    }
}