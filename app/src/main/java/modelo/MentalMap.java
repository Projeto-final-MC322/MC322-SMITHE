package modelo;

import java.time.LocalDate;

public class MentalMap extends MaterialDeEstudo implements Revisar {

    private MapNode root;

    public MapaMental(String titulo, String disciplina){
        super(titulo, disciplina);
    }

    

    public void exibirConteudo(); // Precisa -> MaterialDeEstudo 
    public void calcularProximaRevisao(); // Precisa -> Revisar


}