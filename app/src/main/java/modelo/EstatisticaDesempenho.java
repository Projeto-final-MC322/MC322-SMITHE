package modelo;

public class EstatisticaDesempenho extends Estatistica {
    private int totalCardEstudados;
    private int Bazingastotais;

    public EstatisticaDesempenho(){
        super();
        this.totalCardEstudados = 0;
        this.Bazingastotais = 0;
    }

    @Override
    public String gerarRelatorio(){
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("\n======================================\n");
        relatorio.append("\n    SISTEMA SMITHE - CONSOLE LOG      \n");
        relatorio.append("========================================\n");
        relatorio.append("Sessões de Pomodoro Concluídas: ").append(this.getSessoesConcluidas()).append("\n");
        relatorio.append("Totais de Cards Analisados: ").append(this.totalCardEstudados).append("\n");
        relatorio.append("Bazinga Acumulado: ").append(this.Bazingastotais).append(" Bazingas\n");
        relatorio.append("-------------------------------------------\n");

        if(this.Bazingastotais >= 1000){
            relatorio.append("Classificação Mental: Nível Sheldon Cooper (Gênio)");
        }
        else{
            relatorio.append("Classificação Mental: Howard Wolowitz (Engenheiro em evolução)");
        }
        relatorio.append("=========================================");

        return relatorio.toString();
    }

    public void computarCards(int pontos_ganhos){
        this.totalCardEstudados += 1;
        this.Bazingastotais += pontos_ganhos;
    }

    public int getTotalCardEstudados(){
        return totalCardEstudados;
    }

    public int getTotalBazingas(){
        return Bazingastotais;
    }
}
