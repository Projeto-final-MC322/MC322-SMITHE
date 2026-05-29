import logica.GerenciadorDeRevisao;
import modelo.Flashcard;
import save.JSON;
import save.JSON_Estatistica;
import modelo.EstatisticaDesempenho;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path_cards = "banco_mental_teste.json";
        String path_estatisticas = "estatisticas_bazinga.json";
        JSON_Estatistica Stats = new JSON_Estatistica();

        EstatisticaDesempenho estatisticas = Stats.carrega_estatistica(path_estatisticas);

        System.out.println("Bem-vindo de volta! Já tens " + estatisticas.getTotalCardEstudados() + " cartões estudados.");


        GerenciadorDeRevisao gerenciadorOriginal = new GerenciadorDeRevisao();
        JSON motorJson = new JSON();
        gerenciadorOriginal.criarNovoFlashcard("Curiosidade", "Unicamp", "Qual é o melhor trabalho de MC322?", "SMITHE");
        try {
            motorJson.carregarDados(gerenciadorOriginal, path_cards);
        }catch(Exception e){
            System.out.println("[Aviso] Nenhum banco de flashcards encontrado. Iniciando um novo...");
        }
        

        // 4. Simulamos o fechar e abrir da aplicação (Criamos um gestor vazio)
        System.out.println("\n--- 3. A REINICIAR O SISTEMA (NOVO GESTOR) ---");
        GerenciadorDeRevisao gerenciadorNovo = new GerenciadorDeRevisao();
        
        System.out.println("========================================");
        System.out.println("       SISTEMA SMITHE INICIADO          ");
        System.out.println("========================================");

        boolean executar = true;
        while(executar){
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Revisar flashcards de hoje");
            System.out.println("2. Cadastrar Novo Flashcard");
            System.out.println("3. Ver Relatório de Desempenho");
            System.out.println("4. Salvar e Sair");
            System.out.println("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    executarRe
            }
        }
        Stats.salvarEstatistica(estatisticas, path_estatisticas);
        System.out.println(estatisticas.gerarRelatorio());
        motorJson.excluirArquivo("banco_mental_teste.json");

    }
}