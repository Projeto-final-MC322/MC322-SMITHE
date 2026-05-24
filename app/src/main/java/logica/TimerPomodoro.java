package logica;
public class TimerPomodoro {
    private int tempoFoco = 25 * 60;
    private int tempoPausa = 5 * 60;
    private int ciclos = 0;
    private boolean emFoco = true;

    public void alternaCiclo(){
        if(emFoco){
            ciclos++;
            emFoco = false;
        } else{
            emFoco = true;
        }
    }
    public int getTempoAtual(){
        return emFoco ? tempoFoco : tempoPausa;
    
    }
    public int getCiclos(){
        return ciclos;
    }
    public boolean isEmfoco(){
        return emFoco;
    }
}
