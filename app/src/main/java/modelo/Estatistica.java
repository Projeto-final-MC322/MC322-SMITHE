package modelo;
public abstract class Estatistica {
    private int sessoesConcluidas;

    public Estatistica(){
        this.sessoesConcluidas = 0;
    }

    public abstract String gerarRelatorio();

    public int getSessoesConcluidas(){
        return sessoesConcluidas;
    }
    public void registrarSessoesConcluidas(){
        this.sessoesConcluidas += 1;
    }
}
