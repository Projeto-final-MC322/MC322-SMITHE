package logica;
import modelo.Flashcard;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class GerenciadorDeRevisao {
    private List<Flashcard> decks = new ArrayList<>();

    public void adicionarCard(Flashcard card){
        this.decks.add(card);
    }

    public void criarNovoFlashcard(String titulo, String disciplina, String nova_frente, String novo_verso){
        Flashcard novo_cartao = new Flashcard(titulo, disciplina, nova_frente, novo_verso);
        this.decks.add(novo_cartao);
        System.out.println("Novo cartão registrado com sucesso!");

    }
    public List<Flashcard> obter_todos_os_cartoes(){
        return this.decks;
    }
    public void editarFlashcard(Flashcard cartao, String nova_frente, String novo_verso){
        if(cartao != null){
            cartao.setFrente(nova_frente);
            cartao.setVerso(novo_verso);
            System.out.println("Cartão atualizado na memória!");
        }
    }
    public void excluirFlashcard(Flashcard cartao){
        if(this.decks.remove(cartao){
            System.out.println("Cartão excluído permanentemente!");
        })
    }
    public List<Flashcard> obtercards_hoje(){
        List<Flashcard> cartoes_filtrados = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for(Flashcard card : decks){
            if(!card.getDataProximaRevisao().isAfter(hoje)){
                cartoes_filtrados.add(card);
            }
        }
        return cartoes_filtrados;
    }
    public int getTamanhoDoDeck(){
        return this.decks.size();
    }
    public void adicionarCartao(Flashcard card){
        this.decks.add(card);
    }
}
