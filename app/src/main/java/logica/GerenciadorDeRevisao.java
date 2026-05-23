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
    public getTamanhodoDeck(){
        return this.decks.size();
    }
}
