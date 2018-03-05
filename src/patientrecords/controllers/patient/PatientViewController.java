package patientrecords.controllers.patient;

import com.jfoenix.controls.JFXButton;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.bson.Document;
import org.controlsfx.control.textfield.CustomTextField;
import patientrecords.controllers.user.EditUserController;
import patientrecords.controllers.user.UserDashboardController;
import patientrecords.models.User;

import org.bson.types.ObjectId;
import patientrecords.controllers.BaseController;
import patientrecords.models.Claimant;
import patientrecords.models.Patient;

public class PatientViewController extends PatientDashboardController implements Initializable{
    public MongoDatabase db;
    public MongoCollection collection;

    // Dashboard CSS file URL
    private final URL url = this.getClass().getResource("/patientrecords/styles/form.css");
    
    private ObservableList<User> patientList;


    private EditUserController main;
    public Stage stage;
    private Logger logger;
    
    private Patient patient;
    private Claimant claimant;

    private final ObjectId id;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    
    @FXML
    private BorderPane patientViewPane;

    @FXML
    private Label insurPrinNameLabel;

    @FXML
    private Label insurPrinIDLabel;

    @FXML
    private VBox demograhics;

    @FXML
    private Label nhifPrinNameLabel;

    @FXML
    private CustomTextField searchField;

    @FXML
    private Label nhifPrinIDLabel;

    @FXML
    private Label insurPrinEmailLabel;

    @FXML
    private Label schemeLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private VBox demograhics1;

    @FXML
    private Label insurProviderLabel;

    @FXML
    private Label insurPatientNoLabel;

    @FXML
    private Label insurRltnLabel;

    @FXML
    private Label altPhoneLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label insurPrinPhoneLabel;

    @FXML
    private JFXButton appointmentButton;

    @FXML
    private Label ageLabel;

    @FXML
    private Label nhifRltnLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label bloodGroupLabel;

    @FXML
    private JFXButton newVisitButton;

    @FXML
    private Label genderLabel;

    @FXML
    private Label nhifPrinNoLabel;

    @FXML
    private Label insurPrinNoLabel;

    @FXML
    private Label insurPrinAddrLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label fileNoLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label timeLabel;

    /**
     * @param id _id field from DB
     * @param db 
     */
    public PatientViewController(String id, MongoDatabase db){
        // this.id = id;
        // this.id = new ObjectId(id)
        this.id = new ObjectId("5a8ecbfba173da01c375ac56");
        this.db = db;
        this.collection = db.getCollection("Patients");
        this.logger = Logger.getLogger(getClass().getName());
    }
    
    /**
     * Creates the stage for viewing patient details
     */
    public void patientViewLoader(){
        // this.db = db;
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/paient/patient.fxml"));
        loader.setController(this);

        try {
            patientViewPane = (BorderPane) loader.load();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            Scene scene = new Scene(patientViewPane, 450, 482, Color.TRANSPARENT);

            // Add css file
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("CSS URL not found!");
            }

            stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle("Edit User Details");
            stage.setFullScreen(false);
            // stage.initOwner(primaryStage); // UserDashBoardController.primaryStage: UserDashboard stage
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
 
    /**
     * Returns the details of a patient
     * @return patients
     */
    public List<Document> getPatient(){
        
        System.out.println("\n----------- getPatient() -------------");
        
        List<Document> patients = new ArrayList<>();
        FindIterable result = null;
        
        try {
            result = collection.find(new Document().append("_id", id));
            
        } catch (MongoException e){
            logger.log(Level.SEVERE, "Unable to fetch patient details", e);
        }
        
        if (result != null) {
            Iterator iterator = result.iterator();
            
            while(iterator.hasNext()){
                Document patient = (Document) iterator.next();
                
                System.out.println("patient");
                System.out.println(patient);
                // System.out.println(patient.get("_id"));
                
                patients.add(patient);

            
            }


            // List<Document> ratings = (List<Document>) ((Document) result.get("metadata")).get("ratings");

        }
        return patients;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // Set date and  time labels   
        // Duration.millis(200)
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {   
            LocalDateTime currDateTime = LocalDateTime.now();
            DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd MMM");
            DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("hh:mm a");
        
            dateLabel.setText(currDateTime.format(dateformatter));
            timeLabel.setText(currDateTime.format(timeformatter));
        }),
             new KeyFrame(Duration.seconds(1))
        );
        // clock.setCycleCount(Animation.INDEFINITE);
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

    }
}
