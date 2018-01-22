package patientrecords.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Hyperlink;

import com.mongodb.DB;

import patientrecords.Main;

public class DashboardController {

    private Main main;
    private Stage stage;
    private DB db;

    @FXML
    public VBox dashboardPane;

    @FXML
    private Hyperlink userlink;

    public void dashboardLoader(Stage stage, DB db) {
        this.stage = stage;
        this.db = db;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/Dashboard.fxml"));
        loader.setController(this);

        try {
            dashboardPane = (VBox) loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            Scene scene = new Scene(dashboardPane, 820, 500);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.setFullScreen(false);
            stage.show();
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

        // ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void userLinkAction(ActionEvent event) throws IOException {

        UserDashboardController uc = new UserDashboardController();
        uc.userDashboardLoader(stage, db);

    }
}
