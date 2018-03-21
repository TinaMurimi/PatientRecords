package patientrecords.controllers;

import patientrecords.controllers.patient.CreatePatient;
import patientrecords.controllers.user.UserDashboardController;
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

import com.mongodb.client.MongoDatabase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import patientrecords.Main;
import patientrecords.controllers.patient.PatientDashboardController;
import patientrecords.controllers.patient.PatientViewController;

public class DashboardController implements Initializable {

    private Main main;
    private Stage stage;
    private MongoDatabase db;

    @FXML
    public VBox dashboardPane;

    @FXML
    private Hyperlink userlink;
    
    @FXML
    private Hyperlink medicalRecordsLink;

    @FXML
    private Hyperlink patientsLink;

    @FXML
    private Hyperlink paymentsLink;

    @FXML
    private Hyperlink reportsLink;

    @FXML
    private Hyperlink appointmentsLink;


    public void dashboardLoader(Stage stage, MongoDatabase db) {
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb){

        patientsLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    CreatePatient cpc = new CreatePatient(db);
                    cpc.createPatientLoader();
                }
            });

    }
}
