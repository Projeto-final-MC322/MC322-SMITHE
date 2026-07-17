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
public class PomodoroInterfaceTest {

    @Start
    public void start(Stage stage) throws Exception {
       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PomodoroView.fxml"));
        StackPane root = loader.load();
        
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront(); 
    }

    @Test
    public void deveConterValoresIniciaisCorretos(FxRobot robot) {
        
        Assertions.assertThat(robot.lookup("#lblTempo").queryLabeled()).hasText("25:00");
        
        
        Assertions.assertThat(robot.lookup("#lblSessoes").queryLabeled()).hasText("Sessões concluídas: 0");
        
        
        Assertions.assertThat(robot.lookup("#lblTipoSessao").queryLabeled()).hasText("● SESSÃO DE FOCO");
    }

    @Test
    public void botoesDevemEstarVisiveisEClicaveis(FxRobot robot) {
        
        robot.clickOn("#btnIniciar");
        robot.clickOn("#btnPausar");
        
        
        Assertions.assertThat(robot.lookup("#btnIniciar").queryButton()).hasText("Iniciar");
        Assertions.assertThat(robot.lookup("#btnPausar").queryButton()).hasText("Pausar");
    }
}