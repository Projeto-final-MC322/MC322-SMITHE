package GUI;
import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import modelo.Resumo;

public class ResumoController {
    @FXML private TextField txtTitulo;
    @FXML private TextField txtDisciplina;
    @FXML private TextArea txtConteudo;

    @FXML
    public void salvarEExportar() {
        String titulo = txtTitulo.getText();
        String disciplina = txtDisciplina.getText();
        String conteudo = txtConteudo.getText();

        if(!titulo.isEmpty() && !conteudo.isEmpty()) {
            Resumo novoResumo = new Resumo(titulo, disciplina, conteudo);
            
            
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Escolha a pasta para salvar o resumo");
            File pastaSelecionada = directoryChooser.showDialog(new Stage());
            
            if (pastaSelecionada != null) {
                novoResumo.exportarArquivo(pastaSelecionada.getAbsolutePath());
                
                // Limpa o formulário
                txtTitulo.clear();
                txtDisciplina.clear();
                txtConteudo.clear();
            }
        }
    }
}
    

