import java.io.File;
import java.util.Optional;

import GUI.TelaPrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import logica.GerenciadorDeConteudo;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;
import save.JSON;
import save.JSON_Conteudo;
import save.JSON_Estatistica;

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

    private boolean deveSalvar = false;

    @Override
    public void init() {
        gerenciador = new GerenciadorDeRevisao();
        
        File fCards = new File(arquivoCards);
        if (fCards.exists()) {
            try { enginejson.carregarDados(gerenciador, arquivoCards); } catch(Exception e) {}
        } else {
            System.out.println("Primeiro uso: Banco de flashcards zerado e reiniciado.");
        }
        
        File fMapas = new File(arquivoMapas);
        if (fMapas.exists()) {
            try { engineConteudo.carregarDados(gerenciadorConteudo, arquivoMapas); } catch(Exception e) {}
        } else {
            System.out.println("Primeiro uso: Quadro de Mapas e Resumos limpo e reiniciado.");
            gerenciadorConteudo.limparMemoria();
        }
        
        File fStats = new File(arquivoStats);
        if (fStats.exists()) {
            estatisticas = enginestats.carrega_estatistica(arquivoStats);
        } else {
            System.out.println("Primeiro uso: Pontos Bazinga e Níveis zerados.");
            estatisticas = new EstatisticaDesempenho(); 
        }
        
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
        
        primary.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sair do SMITHE");
            alert.setHeaderText("Você está prestes a fechar o aplicativo.");
            alert.setContentText("Deseja SALVAR as alterações ou APAGAR a base de dados (Sair sem Salvar)?");

            ButtonType btnSalvar = new ButtonType("Salvar e Sair");
            ButtonType btnNaoSalvar = new ButtonType("Apagar Dados (Sair sem Salvar)");
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnSalvar, btnNaoSalvar, btnCancelar);

            Optional<ButtonType> resultado = alert.showAndWait();
            
            if (resultado.isPresent() && resultado.get() == btnSalvar) {
                deveSalvar = true;
            } else if (resultado.isPresent() && resultado.get() == btnNaoSalvar) {
                deveSalvar = false;
            } else {
                event.consume(); 
            }
        });

        primary.show();
    }

    @Override
    public void stop() {
        if (deveSalvar) {
            enginejson.salvarDados(gerenciador, arquivoCards);
            enginestats.salvarEstatistica(estatisticas, arquivoStats);
            engineConteudo.salvarDados(gerenciadorConteudo, arquivoMapas);
            System.out.println("Sessão encerrada. Todos os dados foram salvos com sucesso!");
        } else {
            System.out.println("Atenção: O utilizador escolheu NÃO GUARDAR. A apagar as bases de dados...");
            
            /* Apaga Flashcards */ 
            File fCards = new File(arquivoCards);
            if (fCards.exists()) fCards.delete();
            
            //*Apaga pontos bazingas */
            File fStats = new File(arquivoStats);
            if (fStats.exists()) fStats.delete();
            
            /*Apaga Mapas Mentais */
            File fMapas = new File(arquivoMapas);
            if (fMapas.exists()) fMapas.delete();

            System.out.println("Bases de dados apagadas. O próximo arranque do SMITHE será limpo.");
        }
    }
    
    public static void main(String[] args){
        launch(args);
    }
}