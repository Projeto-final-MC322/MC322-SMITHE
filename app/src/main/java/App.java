import logica.GerenciadorDeRevisao;
import save.JSON;
import save.JSON_Estatistica;
import modelo.EstatisticaDesempenho;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class App extends Application {
    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas;
    private JSON enginejson = new JSON();
    private JSON_Estatistica enginestats = new JSON_Estatistica();
    private final String arquivoCards = "banco_mental.json";
    private final String arquivoStats = "estatisticas_bazinga.json";

    @Override
    public void init(){
        gerenciador = new GerenciadorDeRevisao();
        try{
            enginejson.carregarDados(gerenciador, arquivoCards);
        }catch(Exception e){
            System.out.println("Iniciando novo banco de dados...");
        }
        estatisticas = enginestats.carrega_estatistica(arquivoStats);
    }

    @Override
    public void start(Stage primary){
        /*Arquivo fxml */
        Scene scene = new Scene(root, 800, 600);
        primary.setTitle("SMITHE - Sistema de Estudos");
        primary.newScene(scene);
        primary.show();
    }

    @Override
    public void stop(){
        enginejson.salvarDados(gerenciador, arquivoCards);
        enginestats.salvarEstatistica(estatisticas, arquivoStats);
        System.out.println("Sessão encerrada. Todos os dados foram salvo com sucesso!");
    }
    public static void main(String[] args){
        launch(args);

    }
}


//     public static void main(String[] args) {

//         Scanner scanner = new Scanner(System.in);
//         String path_cards = "banco_mental_teste.json";
//         String path_estatisticas = "estatisticas_bazinga.json";
//         JSON_Estatistica Stats = new JSON_Estatistica();

//         EstatisticaDesempenho estatisticas = Stats.carrega_estatistica(path_estatisticas);

//         System.out.println("Bem-vindo de volta! Já tens " + estatisticas.getTotalCardEstudados() + " cartões estudados.");


//         GerenciadorDeRevisao gerenciadorOriginal = new GerenciadorDeRevisao();
//         JSON motorJson = new JSON();
//         gerenciadorOriginal.criarNovoFlashcard("Curiosidade", "Unicamp", "Qual é o melhor trabalho de MC322?", "SMITHE");
//         try {
//             motorJson.carregarDados(gerenciadorOriginal, path_cards);
//         }catch(Exception e){
//             System.out.println("[Aviso] Nenhum banco de flashcards encontrado. Iniciando um novo...");
//         }
        

//         // 4. Simulamos o fechar e abrir da aplicação (Criamos um gestor vazio)
//         System.out.println("\n--- 3. A REINICIAR O SISTEMA (NOVO GESTOR) ---");
//         GerenciadorDeRevisao gerenciadorNovo = new GerenciadorDeRevisao();
        
//         System.out.println("========================================");
//         System.out.println("       SISTEMA SMITHE INICIADO          ");
//         System.out.println("========================================");

//         boolean executar = true;
//         while(executar){
//             System.out.println("\n--- MENU PRINCIPAL ---");
//             System.out.println("1. Revisar flashcards de hoje");
//             System.out.println("2. Cadastrar Novo Flashcard");
//             System.out.println("3. Ver Relatório de Desempenho");
//             System.out.println("4. Salvar e Sair");
//             System.out.println("Escolha uma opção: ");

//             int opcao = scanner.nextInt();
//             scanner.nextLine();

//             switch(opcao){
//                 case 1:
//                     executarRe
//             }
//         }
//         Stats.salvarEstatistica(estatisticas, path_estatisticas);
//         System.out.println(estatisticas.gerarRelatorio());
//         motorJson.excluirArquivo("banco_mental_teste.json");

//     }
// }