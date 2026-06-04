package modelo;

public class EstatisticaDesempenho extends Estatistica {
    private int totalCardEstudados;
    private int Bazingastotais;
    private int sessoes_pomodoro_concluidas;

    public EstatisticaDesempenho(){
        super();
        this.totalCardEstudados = 0;
        this.Bazingastotais = 0;
        this.sessoes_pomodoro_concluidas = 0;
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
            relatorio.append("Classificação Mental: Nível Sheldon Cooper (Gênio do Estudo)");
        }
        else{
            relatorio.append("Classificação Mental: Howard Wolowitz (Apenas um engenheiro, sem Doutorado)");
        }
        relatorio.append("=========================================");

        return relatorio.toString();
    }
    public void registarsessãoPomodoro(){
        this.sessoes_pomodoro_concluidas++;
        this.Bazingastotais += 25;
        System.out.println("Bazinga! +25 pontos pela sessão Pomodoro concluída!");
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
    public void adicionarPontosBazinga(int pontos) {
        this.Bazingastotais += pontos;
    }
    public int getNivel() {
        return 1 + (this.Bazingastotais / 100);
    }
}
