import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Presentation/mainWindow.fxml"));
        primaryStage.setTitle("Mongodb Management Studio");
        Scene scene =  new Scene(root, 800, 700);
        scene.getStylesheets().add("/Resources/stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MM Studio");
        launch(args);

    }
}
