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
public class MapaMentalInterfaceTest {

    @Start
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MapaMentalView.fxml"));
        StackPane root = loader.load();
        
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    public void devePreencherCamposDeCriacaoDeMapa(FxRobot robot) {
        
        robot.clickOn("#txtNovaDisciplina").write("Física");
        robot.clickOn("#txtNovoTitulo").write("Leis de Newton");

        
        Assertions.assertThat(robot.lookup("#txtNovaDisciplina").queryTextInputControl()).hasText("Física");
        Assertions.assertThat(robot.lookup("#txtNovoTitulo").queryTextInputControl()).hasText("Leis de Newton");
        
        
    }
    
    @Test
    public void telaDeEdicaoDeLadoDeveEstarDesabilitadaNoInicio(FxRobot robot) {

        
        Assertions.assertThat((javafx.scene.Node) robot.lookup("#boxEdicao").query()).isDisabled();
    }
}