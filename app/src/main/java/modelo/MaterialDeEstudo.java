package modelo;
import java.time.LocalDate;

public abstract class MaterialDeEstudo {
    protected String titulo;
    protected String disciplina;
    protected String data_adicao;
    protected String data_proxima_revisao;
    protected int num_revisoes;

    public MaterialDeEstudo(String titulo, String disciplina) {
        this.titulo = titulo;
        this.disciplina = disciplina;
        this.data_adicao = LocalDate.now().toString();
        this.data_proxima_revisao = LocalDate.now().plusDays(1).toString();
        this.num_revisoes = 0;
    }

    public String getTitulo() { return titulo; }
    public String getDisciplina() { return disciplina; }
    public String getData_adicao() { return data_adicao; }
    public String getData_proxima_revisao() { return data_proxima_revisao; }
    
    public boolean precisaRevisar() {
        if (data_proxima_revisao == null) return false;
        return !LocalDate.now().isBefore(LocalDate.parse(data_proxima_revisao));
    }

    public void registrarRevisao() {
        this.num_revisoes++;
        LocalDate hoje = LocalDate.now();
        if (num_revisoes == 1) this.data_proxima_revisao = hoje.plusDays(3).toString();
        else if (num_revisoes == 2) this.data_proxima_revisao = hoje.plusDays(7).toString();
        else if (num_revisoes == 3) this.data_proxima_revisao = hoje.plusDays(15).toString();
        else this.data_proxima_revisao = hoje.plusDays(30).toString();
    }

    public abstract void exibirConteudo();
}