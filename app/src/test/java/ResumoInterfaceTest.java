import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
public class ResumoInterfaceTest {

    @Start
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResumoView.fxml"));
        StackPane root = loader.load();
        
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    public void devePreencherCamposDeResumo(FxRobot robot) {
        
        robot.clickOn("#txtTitulo").write("Pilares da POO");
        
        
        robot.clickOn("#txtDisciplina").write("Computação");
        
        
        robot.clickOn("#txtConteudo").write("Herança, Encapsulamento, Polimorfismo e Abstração.");

        
        Assertions.assertThat(robot.lookup("#txtTitulo").queryTextInputControl()).hasText("Pilares da POO");
        Assertions.assertThat(robot.lookup("#txtDisciplina").queryTextInputControl()).hasText("Computação");
        Assertions.assertThat(robot.lookup("#txtConteudo").queryTextInputControl()).hasText("Herança, Encapsulamento, Polimorfismo e Abstração.");
    }
}