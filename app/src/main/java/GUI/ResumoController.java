package GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logica.GerenciadorDeConteudo;
import modelo.EstatisticaDesempenho;
import modelo.Resumo;

public class ResumoController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDisciplina;
    @FXML private TextArea txtConteudo;

    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private EstatisticaDesempenho estatisticas;
    private TelaPrincipal telaPrincipal;

    // Recebe o sistema de pontos da Tela Principal
    public void setEstatisticas(EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
    }

    @FXML
    public void salvarEExportar() {
        String titulo = txtTitulo.getText().trim();
        String disciplina = txtDisciplina.getText().trim();
        String conteudo = txtConteudo.getText().trim();

        // 1. Validação de Segurança
        if (titulo.isEmpty() || disciplina.isEmpty() || conteudo.isEmpty()) {
            mostrarAviso(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, preencha o Título, Disciplina e o Conteúdo do seu resumo antes de salvar.");
            return;
        }

        // 2. Cria o objeto Resumo
        Resumo novoResumo = new Resumo(titulo, disciplina, conteudo);

        // 3. Adiciona à base de dados para aparecer na aba de Revisões Espaçadas
        gerenciadorConteudo.adicionarMaterial(novoResumo);

        // 4. Executa a exportação física (.txt)
        novoResumo.exportarParaTXT();

        // 5. Integração Bazinga! Ganha +15 Pontos por criar um resumo
        if (estatisticas != null) {
            estatisticas.adicionarPontosBazinga(15);
            Platform.runLater(() -> telaPrincipal.atualizarNivel());
        }

        // 6. Confirmação Visual
        mostrarAviso(Alert.AlertType.INFORMATION, "Resumo Exportado!", 
            "O resumo '" + titulo + "' foi exportado com sucesso para a pasta 'Exportacoes' no seu computador!\n\nGanhou +15 Pontos Bazinga!");

        // 7. Limpa a tela para o próximo resumo
        txtTitulo.clear();
        txtDisciplina.clear();
        txtConteudo.clear();
    }

    private void mostrarAviso(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}