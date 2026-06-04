package modelo;

import java.time.LocalDate;

public interface Revisar {

    int getRepeticoes();
    void setRepeticoes(int repeticoes);

    double getFacilidade();
    void setFacilidade(double facilidade);

    int getIntervalosDias();
    void setIntervalos(int dias);

    void setDataProximaRevisao(LocalDate data);

}
