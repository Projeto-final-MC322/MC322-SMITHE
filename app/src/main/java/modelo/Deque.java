package modelo;
import java.util.ArrayList;

public class Deque {
    private ArrayList<Flashcard> flashcards;

    // inicializa o deque de flashcards
    public void setDeque(){
        flashcards = new ArrayList<Flashcard>();
    }

    // aidiciona no final do deque
    public void addFlashcard(Flashcard flashcard){
        this.flashcards.add(flashcard);
    }

    // retorna o flashcard de índice i
    public Flashcard getFlashcard(int i){
        return this.flashcards.get(i);
    }

    // remove por inidice
    public void removeFlashcard(int i){
        this.flashcards.remove(i);
    }

    // ordena o deque de flashcards por facilidade
    public void sortFlashcards(){
        this.flashcards.sort((a, b) -> Integer.compare(b.getFacilidade(), a.getFacilidade()));
    }

}
