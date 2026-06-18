package logica;
import java.time.LocalDate;

import modelo.Revisar;

public class Revisao_Espacada {
    public void processa_revisao(Revisar item ,int nota){
        int repeticoes = item.getRepeticoes();
        double facilidade = item.getFacilidade();
        int intervalo = item.getIntervalosDias();

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

        item.setRepeticoes(repeticoes);
        item.setFacilidade(facilidade);
        item.setIntervalos(intervalo);
        item.setDataProximaRevisao(LocalDate.now().plusDays(intervalo));
    }

}
