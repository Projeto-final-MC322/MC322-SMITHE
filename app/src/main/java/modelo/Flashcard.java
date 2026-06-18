package modelo;

import java.time.LocalDate;

import logica.Revisao_Espacada;

public class Flashcard extends MaterialDeEstudo implements Revisar {
    private String frente;
    private String verso;
    private double facilidade;
    private int repeticoes;
    private int intervalos;
    private LocalDate DataProximaRevisao;
    
    public Flashcard(String titulo, String disciplina, String frente, String verso){
        super(titulo, disciplina);
        this.frente = frente;
        this.verso = verso;

        this.repeticoes = 0;
        this.facilidade = 2.5;
        this.intervalos = 0;
        this.DataProximaRevisao = LocalDate.now();
        this.data_proxima_revisao = this.DataProximaRevisao.toString();
    }
    @Override
    public boolean precisaRevisar() {
        if (this.DataProximaRevisao == null) return false;
        return !LocalDate.now().isBefore(this.DataProximaRevisao);
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
    public int getRepeticoes(){
        return repeticoes;
    }
    public void setRepeticoes(int repeticoes){
        this.repeticoes = repeticoes;
    }
    public double getFacilidade(){
        return facilidade;
    }
    public void setFacilidade(double facilidade){
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
    
    public void calcularProximaRevisao(int desempenho){
        Revisao_Espacada engine = new Revisao_Espacada();
        engine.processa_revisao(this, desempenho);
    }
    public void setDataProximaRevisao(LocalDate dataProximaRevisao){
        this.DataProximaRevisao = dataProximaRevisao;
        this.data_proxima_revisao = dataProximaRevisao.toString();
    }


}
