package save;
import logica.GerenciadorDeRevisao;
public interface Armazenamento {
    void salvarDados(GerenciadorDeRevisao gerenciador, String path);
    void carregarDados(GerenciadorDeRevisao gerenciado, String path);
    void excluirArquivo(String path);
}
