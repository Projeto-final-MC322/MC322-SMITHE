import logica.GerenciadorDeRevisao;
import modelo.Flashcard;
import save.JSON;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("=== TESTE DE MOTOR JSON (GSON) ===\n");

        // 1. Instanciamos os motores
        GerenciadorDeRevisao gerenciadorOriginal = new GerenciadorDeRevisao();
        JSON motorJson = new JSON();
        String caminhoFicheiro = "banco_mental_teste.json";

        // 2. Criamos os cartões e alocamos na memória do primeiro gestor
        System.out.println("--- 1. A GERAR CARTÕES NA MEMÓRIA ---");
        gerenciadorOriginal.criarNovoFlashcard("Curiosidade", "Unicamp", "Qual é o melhor trabalho de MC322?", "SMITHE");

        // 3. Disparamos o processo de escrita no disco
        System.out.println("\n--- 2. A GUARDAR NO DISCO ---");
        motorJson.salvarDados(gerenciadorOriginal, caminhoFicheiro);

        // 4. Simulamos o fechar e abrir da aplicação (Criamos um gestor vazio)
        System.out.println("\n--- 3. A REINICIAR O SISTEMA (NOVO GESTOR) ---");
        GerenciadorDeRevisao gerenciadorNovo = new GerenciadorDeRevisao();
        
        // Verificamos que o gestor novo está realmente vazio
        System.out.println("Tamanho do deck antes do Load: " + gerenciadorNovo.obter_todos_os_cartoes().size());

        // 5. Injetamos os dados do ficheiro neste gestor limpo
        System.out.println("\n--- 4. A CARREGAR DADOS DO FICHEIRO ---");
        motorJson.carregarDados(gerenciadorNovo, caminhoFicheiro);

        // 6. Imprimimos o resultado final para validação visual
        System.out.println("\n--- 5. RESULTADO DA EXTRAÇÃO ---");
        List<Flashcard> cartoesRecuperados = gerenciadorNovo.obter_todos_os_cartoes();
        
        for (Flashcard c : cartoesRecuperados) {
            System.out.println("-> [" + c.getDisciplina() + "] " + c.getTitulo());
            System.out.println("   Q: " + c.getFrente());
            System.out.println("   R: " + c.getVerso() + "\n");
        }
        
        System.out.println("=== TESTE CONCLUÍDO ===");
        motorJson.excluirArquivo("banco_mental_teste.json");
    }
}