import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class TelaPrincipal {
    @FXML
    private TextField Materia;

    @FXML
    public void botaoCadastrar(){
        String materia = Materia.getText();

        System.out.println("Usuário quer cadastrar: " + materia);
    }
}
