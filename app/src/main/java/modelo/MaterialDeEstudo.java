package modelo;
import java.time.LocalDate;

public abstract class MaterialDeEstudo {
    private String titulo;
    private LocalDate data_adicao;
    private String disciplina;

    public MaterialDeEstudo(String titulo, String disciplina){
        this.titulo = titulo;
        this.disciplina = disciplina;
        this.data_adicao = LocalDate.now();
    }
    public abstract void exibirConteudo();

    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
    public LocalDate getData_adicao(){
        return data_adicao;
    }
    public String getDisciplina(){
        return disciplina;
    }
    public void setDisciplina(String disciplina){
        this.disciplina = disciplina;
    }
}

