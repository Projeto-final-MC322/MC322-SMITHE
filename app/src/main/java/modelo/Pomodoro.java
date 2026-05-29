package modelo;
public class Pomodoro implements Timer {
    private int minutos;
    private boolean emExecucao;


    public Pomodoro(){
        this.minutos = 25;
        this.emExecucao = false;
    }
    @Override
    public void iniciarTimer(){
        if(!this.emExecucao){
            this.emExecucao = true;
            System.out.println("\n Cronômetro Pomodoro iniciado: " + this.minutos + " minutos restantes.");
            System.out.println("Foco absoluto! Que a força esteja com você!");
        }else{
            System.out.println("Timer já em execução!");
        }
    }
    public void pausarTimer(){
        if(this.emExecucao){
            this.emExecucao = false;
            System.out.println("Pomodoro pausado. Hora de dar um pause!");
        }else{
            System.out.println("Não é possível pausar um timer que não foi iniciado.");
        }
    }
    public int getMinutos(){
        return minutos;
    }
    public void setMinutos(int minutos){
        this.minutos = minutos;
    }
    public boolean isemExecucao(){
        return emExecucao;
    }
}
