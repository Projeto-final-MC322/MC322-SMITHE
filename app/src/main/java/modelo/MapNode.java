package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Nó de um grafo de conhecimento.
 * Cada nó tem: nome, definição, posição visual (x,y) e arestas para filhos.
 * A estrutura é uma árvore enraizada, mas preparada para múltiplas conexões.
 */
public class MapNode {

    private String name;
    private String definition;
    private List<MapNode> children;

    // Posição visual no Pane (usada pelo controller)
    private double layoutX;
    private double layoutY;

    public MapNode(String name) {
        this.name = name;
        this.definition = "";
        this.children = new ArrayList<>();
        this.layoutX = 0;
        this.layoutY = 0;
    }

    // ── Filhos / Arestas ──────────────────────────────────────

    /** Adiciona um filho já existente (para reconstrução do grafo). */
    public void addChild(MapNode child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    /** Cria e adiciona um novo filho pelo nome, retorna o nó criado. */
    public MapNode addChild(String name) {
        MapNode child = new MapNode(name);
        children.add(child);
        return child;
    }

    /** Remove um filho pelo nome. */
    public boolean removeChild(String name) {
        return children.removeIf(c -> c.getName().equals(name));
    }

    /** Busca em profundidade por um nó com o nome dado. */
    public MapNode findByName(String name) {
        if (this.name.equals(name)) return this;
        for (MapNode child : children) {
            MapNode result = child.findByName(name);
            if (result != null) return result;
        }
        return null;
    }

    /** Retorna todos os nós do grafo (DFS), incluindo este. */
    public List<MapNode> allNodes() {
        List<MapNode> all = new ArrayList<>();
        collectNodes(this, all);
        return all;
    }

    private void collectNodes(MapNode node, List<MapNode> acc) {
        acc.add(node);
        for (MapNode child : node.children) {
            collectNodes(child, acc);
        }
    }

    // ── Getters / Setters ─────────────────────────────────────

    public String getName()                  { return name; }
    public void   setName(String name)       { this.name = name; }

    public String getDefinition()            { return definition; }
    public void   setDefinition(String def)  { this.definition = def; }

    public List<MapNode> getChildren()       { return children; }

    public double getLayoutX()               { return layoutX; }
    public void   setLayoutX(double x)       { this.layoutX = x; }

    public double getLayoutY()               { return layoutY; }
    public void   setLayoutY(double y)       { this.layoutY = y; }

    @Override
    public String toString() { return name; }
}