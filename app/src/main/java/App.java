import GUI.TelaPrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import java.io.File;
import java.util.Optional;

import logica.GerenciadorDeRevisao;
import logica.GerenciadorDeConteudo;
import modelo.EstatisticaDesempenho;

import save.JSON;
import save.JSON_Estatistica;
import save.JSON_Conteudo;

public class App extends Application {
    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas;
    
    private JSON enginejson = new JSON();
    private JSON_Estatistica enginestats = new JSON_Estatistica();
    
    private GerenciadorDeConteudo gerenciadorConteudo = new GerenciadorDeConteudo();
    private JSON_Conteudo engineConteudo = new JSON_Conteudo();
    
    private final String arquivoCards = "banco_mental.json";
    private final String arquivoStats = "estatisticas_bazinga.json";
    private final String arquivoMapas = "banco_conteudo.json"; 

    // Variável que diz ao sistema se deve ou não gravar os dados no fim
    private boolean deveSalvar = true;

    @Override
    public void init() {
        gerenciador = new GerenciadorDeRevisao();
        
        // 1. VERIFICAÇÃO DE PRIMEIRA UTILIZAÇÃO: Flashcards
        File fCards = new File(arquivoCards);
        if (fCards.exists()) {
            try { enginejson.carregarDados(gerenciador, arquivoCards); } catch(Exception e) {}
        } else {
            System.out.println("Primeiro uso: Banco de flashcards zerado e reiniciado.");
        }
        
        // 2. VERIFICAÇÃO DE PRIMEIRA UTILIZAÇÃO: Mapas Mentais e Resumos
        File fMapas = new File(arquivoMapas);
        if (fMapas.exists()) {
            try { engineConteudo.carregarDados(gerenciadorConteudo, arquivoMapas); } catch(Exception e) {}
        } else {
            System.out.println("Primeiro uso: Quadro de Mapas e Resumos limpo e reiniciado.");
            gerenciadorConteudo.limparMemoria();
        }
        
        // 3. VERIFICAÇÃO DE PRIMEIRA UTILIZAÇÃO: Pontos Bazinga
        File fStats = new File(arquivoStats);
        if (fStats.exists()) {
            estatisticas = enginestats.carrega_estatistica(arquivoStats);
        } else {
            System.out.println("Primeiro uso: Pontos Bazinga e Níveis zerados.");
            estatisticas = new EstatisticaDesempenho(); // Nível 1, 0 BZ
        }
        
        // Prevenção extra caso o ficheiro exista mas esteja corrompido
        if (estatisticas == null) {
            estatisticas = new EstatisticaDesempenho();
        }
    }

    @Override
    public void start(Stage primary) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TelaPrincipal.fxml"));
        Parent root = loader.load();
        TelaPrincipal controller = loader.getController();
        controller.setGerenciador(gerenciador);
        controller.setEstatisticas(estatisticas);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/smithe.css").toExternalForm());
        primary.setTitle("SMITHE - Sistema de Estudos");
        primary.setScene(scene);
        
        // A MÁGICA DO POP-UP AO FECHAR A JANELA
        primary.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sair do SMITHE");
            alert.setHeaderText("Você está prestes a fechar o aplicativo.");
            alert.setContentText("Deseja salvar as suas alterações de hoje (Resumos, Mapas e Pontos Bazinga)?");

            // Criação dos Botões Personalizados
            ButtonType btnSalvar = new ButtonType("Salvar e Sair");
            ButtonType btnNaoSalvar = new ButtonType("Sair sem Salvar");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnSalvar, btnNaoSalvar, btnCancelar);

            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent()) {
                if (resultado.get() == btnSalvar) {
                    deveSalvar = true; // O stop() vai guardar tudo
                } else if (resultado.get() == btnNaoSalvar) {
                    deveSalvar = false; // O stop() não vai fazer nada
                } else {
                    event.consume(); // CANCELA o fecho da janela! O SMITHE continua aberto.
                }
            }
        });

        primary.show();
    }

    @Override
    public void stop() {
        // Só guarda os ficheiros se o utilizador tiver clicado em "Salvar e Sair"
        if (deveSalvar) {
            enginejson.salvarDados(gerenciador, arquivoCards);
            enginestats.salvarEstatistica(estatisticas, arquivoStats);
            engineConteudo.salvarDados(gerenciadorConteudo, arquivoMapas);
            System.out.println("Sessão encerrada. Todos os dados foram salvos com sucesso!");
        } else {
            System.out.println("Sessão encerrada. O utilizador escolheu NÃO GUARDAR as alterações.");
        }
    }
    
    public static void main(String[] args){
        launch(args);
    }
}