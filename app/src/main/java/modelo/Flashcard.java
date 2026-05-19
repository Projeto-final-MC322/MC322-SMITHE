package modelo;

public class Flashcard extends MaterialDeEstudo implements Revisar {
    private String frente;
    private String verso;
    private int facilidade;
    
    public Flashcard(String titulo, String disciplina, String frente, String verso){
        super(titulo, disciplina);
        this.frente = frente;
        this.verso = verso;
        this.facilidade = 100;
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
    public void calcularProximaRevisao(int desempenho){
        this.facilidade += (desempenho * 100);
        System.out.println("Troca equivalente realizado com sucesso! Conhecimento transmutado. Nível de facilidade: " + this.facilidade);
    }
}
