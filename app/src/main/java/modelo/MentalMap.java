package modelo;

import java.time.LocalDate;

public class MentalMap extends MaterialDeEstudo implements Revisar {

    private MapNode root; // no raiz

    public MentalMap(String titulo, String disciplina){
        super(titulo, disciplina);
        this.root = new MapNode(this.titulo);
    }

    

    public void exibirConteudo(); // Precisa -> MaterialDeEstudo 
    public void calcularProximaRevisao(); // Precisa -> Revisar
}