package logica;
import modelo.Flashcard;
import java.time.LocalDate;

public class Revisao_Espacada {
    public void processa_revisao(Flashcard card, int nota){
        int repeticoes = card.getRepeticoes();
        double facilidade = card.getFacilidade();
        int intervalo = card.getIntervalosDias();

        if(nota >= 3){
            if(repeticoes == 0){
                intervalo = 1;
            }else if(repeticoes == 1){
                intervalo = 6;
            } else{
                intervalo = (int) Math.round(intervalo * facilidade);
            }
            repeticoes++;
        }else{
            repeticoes = 0;
            intervalo = 1;
        }
        facilidade = facilidade + (0.1 - (5 - nota) *(0.08 + (5 - nota) *0.02));

        if(facilidade < 1.3){
            facilidade = 1.3;
        }

        card.setRepeticoes(repeticoes);
        card.setFacilidade(facilidade);
        card.setIntervalos(intervalo);
        card.setDataProximaRevisao(LocalDate.now().plusDays(intervalo));
    }

}
