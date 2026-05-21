package modelo;

import java.time.LocalDate;

public class Flashcard extends MaterialDeEstudo implements Revisar {
    private String frente;
    private String verso;
    private int facilidade;
    private int repeticoes;
    private int intervalos;
    private LocalData DataProximaRevisao;
    
    public Flashcard(String titulo, String disciplina, String frente, String verso){
        super(titulo, disciplina);
        this.frente = frente;
        this.verso = verso;
        this.facilidade = 100;

        this.repeticoes = 0;
        this.facilidade = 2.5;
        this.intervalos = 0;
        this.DataProximaRevisao = LocalDate.now();
    }
    
    @Override
    public void exibirConteudo(){
        System.out.println("Frente: " + this.frente);
        System.out.println("Verso: " + this.verso);
    }
    public String getFrente(){
        return this.frente;
    }
    public String getVerso(){
        return this.verso;
    }

    public void setFrente(String nova_frente){
        this.frente = nova_frente;
    }
    public void setVerso(String novo_verso){
        this.verso = novo_verso;
    }
    public String getRepeticoes(){
        return repeticoes;
    }
    public void setRepeticoes(int repeticoes){
        this.repeticoes = repeticoes;
    }
    public double getFacilidade(){
        return facilidade;
    }
    public void setFacilidade(int facilidade){
        this.facilidade = facilidade;
    }
    public int getIntervalosDias(){
        return intervalos;
    }
    public void setIntervalos(int intervalos){
        this.intervalos = intervalos;
    }
    public LocalDate getDataProximaRevisao(){
        return DataProximaRevisao;
    }
    public void setDataProximaRevisao(LocalDate dataProximaRevisao){
        this.DataProximaRevisao = dataProximaRevisao;
    }
}
