package save;
import logica.GerenciadorDeConteudo;
public interface Armazenamento {
    void salvarDados(GerenciadorDeConteudo gerenciador, String path);
    void carregarDados(GerenciadorDeConteudo gerenciado, String path);
}
