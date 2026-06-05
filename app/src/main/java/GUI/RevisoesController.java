package GUI;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import logica.GerenciadorDeConteudo;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;
import modelo.MaterialDeEstudo;

public class RevisoesController {
    @FXML private ListView<String> listaRevisoes;
    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private EstatisticaDesempenho estatisticas;
    private TelaPrincipal telaPrincipal;
    private List<MaterialDeEstudo> todosMateriais;

    public void setup(GerenciadorDeRevisao ger, EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
        carregarPendentes();
    }

    private void carregarPendentes() {
        listaRevisoes.getItems().clear();
        todosMateriais = gerenciadorConteudo.obterTodososMateriais();
        for (MaterialDeEstudo mat : todosMateriais) {
            if (mat.precisaRevisar()) {
                listaRevisoes.getItems().add(mat.getDisciplina() + " - " + mat.getTitulo() + " (Agendado: " + mat.getData_proxima_revisao() + ")");
            }
        }
        if(listaRevisoes.getItems().isEmpty()) listaRevisoes.getItems().add("🎉 Tudo em dia! Sem revisões pendentes.");
    }

    @FXML
    public void marcarComoRevisado() {
        int index = listaRevisoes.getSelectionModel().getSelectedIndex();
        if(index >= 0 && !listaRevisoes.getItems().get(0).contains("Tudo em dia")) {
            int contador = 0;
            for (MaterialDeEstudo mat : todosMateriais) {
                if (mat.precisaRevisar()) {
                    if (contador == index) {
                        mat.registrarRevisao(); // Avança para 3, 7, 15 ou 30 dias!
                        estatisticas.adicionarPontosBazinga(15); // +15 Pontos!
                        telaPrincipal.atualizarNivel(); // Atualiza a tela na hora!
                        break;
                    }
                    contador++;
                }
            }
            carregarPendentes();
        }
    }
}