package patientrecords;

//import java.io.InputStream;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javafx.fxml.Initializable;
//import javafx.fxml.JavaFXBuilderFactory;
//import javafx.fxml.FXMLLoader;

import javafx.application.Application;
import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.AnchorPane;


public class Main extends Application {
    
    // private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainController mc = new MainController();
        mc.stage = primaryStage;
        // this.stage = primaryStage;
        mc.loaderInit();
        mc.setMain(this);
   }
    

    public static void main(String[] args) {
        launch(args);
    }
}
