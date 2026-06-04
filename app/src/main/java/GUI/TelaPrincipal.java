package GUI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;

public class TelaPrincipal {
    @FXML private BorderPane painelPrincipal;
    @FXML private StackPane Conteudo;

    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas;

    // Cache: cada tela é carregada só uma vez e reutilizada
    private final Map<String, StackPane> telaCache = new HashMap<>();

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciador = gerenciador;
    }

    public void setEstatisticas(EstatisticaDesempenho estatisticas) {
        this.estatisticas = estatisticas;
    }

    @FXML public void abrirPomodoro()     { carregarTela("PomodoroView.fxml"); }
    @FXML public void abrirFlashcards()   { carregarTela("FlashcardView.fxml"); }
    @FXML public void abrirResumos()      { carregarTela("ResumoView.fxml"); }
    @FXML public void abrirMapasMentais() { carregarTela("MapaMentalView.fxml"); }

    private void carregarTela(String arquivoFxml) {
        // Se já foi carregada antes, apenas exibe — não recria
        if (telaCache.containsKey(arquivoFxml)) {
            Conteudo.getChildren().setAll(telaCache.get(arquivoFxml));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + arquivoFxml));
            StackPane novaTela = loader.load();

            Object controlador = loader.getController();
            if (controlador instanceof FlashcardController) {
                ((FlashcardController) controlador).setGerenciador(this.gerenciador);
            } else if (controlador instanceof MapaMentalController) {
                ((MapaMentalController) controlador).setGerenciador(this.gerenciador);
            }

            // Guarda no cache para não destruir ao trocar de aba
            telaCache.put(arquivoFxml, novaTela);
            Conteudo.getChildren().setAll(novaTela);

        } catch (IOException e) {
            System.err.println("Erro ao tentar carregar a tela: " + arquivoFxml);
            e.printStackTrace();
        }
    }
}