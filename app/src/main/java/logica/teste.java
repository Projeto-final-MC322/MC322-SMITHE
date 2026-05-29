package logica;
import modelo.Flashcard;

import java.time.LocalDate;
import java.util.List;


public class teste {
    public static void main(String[] args){
        System.out.println("Teste de Revisão...");

        GerenciadorDeRevisao gerenciador = new GerenciadorDeRevisao();

        Flashcard card1 = new Flashcard("Unicamp", "Disciplinas", "Qual é o pior instituto da Unicamp?", "IFGW");

        card1.setDataProximaRevisao(LocalDate.now().minusDays(2));

        gerenciador.adicionarCartao(card1);

        System.out.println("Tamanho total de alocação do Deck: " + gerenciador.getTamanhoDoDeck() + " cartões.");
        List<Flashcard> cartoesdehoje = gerenciador.obtercards_hoje();

        System.out.println("\n=== CARTÕES SELECIONADOS PARA HOJE ===");
        System.out.println("Quantidade filtrada: " + cartoesdehoje.size() + "\n");

        for(Flashcard c : cartoesdehoje){
            System.out.println("-> Disciplina: " + c.getDisciplina() + " | Título: " + c.getTitulo());
            c.exibirConteudo(); 
            System.out.println("-----------------------------------");
        }
    }
}
