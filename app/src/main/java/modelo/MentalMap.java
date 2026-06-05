package modelo;

import java.time.LocalDate;

import logica.Revisao_Espacada;

public class MentalMap extends MaterialDeEstudo implements Revisar {

    private MapNode root;


    private int       repeticoes;
    private double    facilidade;
    private int       intervaloDias;
    private LocalDate dataProximaRevisao;

    public MentalMap(String titulo, String disciplina) {
        super(titulo, disciplina);
        this.root = new MapNode(titulo);

        this.repeticoes          = 0;
        this.facilidade          = 2.5;
        this.intervaloDias       = 0;
        this.dataProximaRevisao  = LocalDate.now();
    }



    public MapNode getRoot() { return root; }

    /**
     * Adiciona um filho ao nó com o nome {@code paiNome}.
     * Retorna o novo nó criado, ou null se o pai não for encontrado.
     */
    public MapNode adicionarNo(String paiNome, String novoNome) {
        MapNode pai = root.findByName(paiNome);
        if (pai == null) return null;
        return pai.addChild(novoNome);
    }

    /**
     * Remove um nó pelo nome (busca em toda a árvore).
     * Não é possível remover a raiz.
     */
    public boolean removerNo(String nome) {
        if (root.getName().equals(nome)) return false; // raiz protegida
        return removerRecursivo(root, nome);
    }

    private boolean removerRecursivo(MapNode atual, String alvo) {
        if (atual.removeChild(alvo)) return true;
        for (MapNode filho : atual.getChildren()) {
            if (removerRecursivo(filho, alvo)) return true;
        }
        return false;
    }


    /**
     * Processa uma sessão de revisão do mapa com nota de 1 a 5.
     * Usa o mesmo algoritmo SM-2 dos Flashcards.
     */
    public void revisar(int nota) {
        Revisao_Espacada engine = new Revisao_Espacada();
        engine.processa_revisao(this, nota);
    }

    /** Retorna true se o mapa está agendado para revisão hoje ou no passado. */
    public boolean precisaRevisarHoje() {
        return !dataProximaRevisao.isAfter(LocalDate.now());
    }



    @Override
    public void exibirConteudo() {
        System.out.println("=== Mapa Mental: " + getTitulo() + " ===");
        imprimirArvore(root, 0);
    }

    private void imprimirArvore(MapNode no, int nivel) {
        System.out.println("  ".repeat(nivel) + "◆ " + no.getName());
        for (MapNode filho : no.getChildren()) {
            imprimirArvore(filho, nivel + 1);
        }
    }



    @Override public int    getRepeticoes()               { return repeticoes; }
    @Override public void   setRepeticoes(int r)          { this.repeticoes = r; }
    @Override public double getFacilidade()               { return facilidade; }
    @Override public void   setFacilidade(double f)       { this.facilidade = f; }
    @Override public int    getIntervalosDias()           { return intervaloDias; }
    @Override public void   setIntervalos(int dias)       { this.intervaloDias = dias; }
    @Override public void   setDataProximaRevisao(LocalDate d) { this.dataProximaRevisao = d; }
    public    LocalDate     getDataProximaRevisao()       { return dataProximaRevisao; }
}