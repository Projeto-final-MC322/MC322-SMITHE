package GUI;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logica.GerenciadorDeRevisao;
import modelo.EstatisticaDesempenho;
import modelo.Flashcard;

public class FlashcardController {
    @FXML private Label lblContador, lblTextoCard;
    @FXML private VBox boxCartao;
    @FXML private HBox boxNotas;
    @FXML private Button btnIniciar;
    @FXML private TextField txtRegTitulo, txtRegDisciplina, txtRegFrente, txtRegVerso;

    private GerenciadorDeRevisao gerenciador;
    private EstatisticaDesempenho estatisticas; 
    private TelaPrincipal telaPrincipal;        
    
    private List<Flashcard> cardsHoje;
    private Flashcard cardAtual;
    private boolean mostrandoVerso = false;
    private int indexAtual = 0;

    public void setGerenciador(GerenciadorDeRevisao gerenciador) {
        this.gerenciador = gerenciador;
    }

    public void setEstatisticas(EstatisticaDesempenho est, TelaPrincipal tela) {
        this.estatisticas = est;
        this.telaPrincipal = tela;
    }

    @FXML
    public void iniciarEstudo() {
        if(gerenciador != null) {
            cardsHoje = gerenciador.obtercards_hoje();
            indexAtual = 0;
            exibirProximoCard();
        }
    }

    private void exibirProximoCard() {
        if (cardsHoje != null && indexAtual < cardsHoje.size()) {
            cardAtual = cardsHoje.get(indexAtual);
            lblContador.setText("Cartões restantes: " + (cardsHoje.size() - indexAtual));
            lblTextoCard.setText(cardAtual.getFrente());
            mostrandoVerso = false;
            boxNotas.setVisible(false);
            btnIniciar.setVisible(false);
        } else {
            lblTextoCard.setText("Parabéns! Concluiu as revisões de hoje! 🎉");
            lblContador.setText("Cartões restantes: 0");
            boxNotas.setVisible(false);
            btnIniciar.setVisible(true);
        }
    }

    @FXML
    public void virarCartao() {
        if (cardAtual != null && !mostrandoVerso) {
            lblTextoCard.setText(cardAtual.getVerso());
            mostrandoVerso = true;
            boxNotas.setVisible(true);
        }
    }

    @FXML public void darNota1() { processarNota(1); }
    @FXML public void darNota2() { processarNota(2); }
    @FXML public void darNota3() { processarNota(3); }
    @FXML public void darNota4() { processarNota(4); }
    @FXML public void darNota5() { processarNota(5); }

    private void processarNota(int nota) {
        gerenciador.avaliaFlashcard(cardAtual, nota);
        
        // Atribui pontos ao estudar!
        if(estatisticas != null) {
            estatisticas.adicionarPontosBazinga(5); 
            telaPrincipal.atualizarNivel(); 
        }
        
        indexAtual++;
        exibirProximoCard();
    }

    @FXML
    public void cadastrarNovoCard() {
        String t = txtRegTitulo.getText();
        String d = txtRegDisciplina.getText();
        String f = txtRegFrente.getText();
        String v = txtRegVerso.getText();

        if(!t.isEmpty() && !d.isEmpty() && !f.isEmpty() && !v.isEmpty()) {
            gerenciador.criarNovoFlashcard(t, d, f, v);
            txtRegTitulo.clear(); txtRegDisciplina.clear(); txtRegFrente.clear(); txtRegVerso.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Flashcard criado com sucesso!");
            alert.showAndWait();

            iniciarEstudo();
        }
    }
}