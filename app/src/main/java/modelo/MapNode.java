package modelo;
import java.util.ArrayList;
import java.util.List;


public class MapNode {

    public String name; // nome do no
    public String definition; // definição do no
    public List<MapNode> children; // lista com os nos filhos desse nó

    public MapNode(String name) {
        this.name = name;
        this.definition = "";
        this.children = new ArrayList<>();
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    // Getters
    public String getName() {
        return this.name;
    }
    
    public String getDefinition() {
        return this.definition;
    }

    public List<MapNode> getChildren() {
        return this.children;
    }

    // Outros métodos
    public void addChild(String name) {
        MapNode child = new MapNode(name);
        this.children.add(child);
    }

    

}
