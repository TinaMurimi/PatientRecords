package patientrecords.controllers.patient;

import patientrecords.controllers.BaseController;
import patientrecords.models.Patient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.ResourceBundle;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.beans.InvalidationListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ListChangeListener;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoCommandException;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;

import com.mongodb.client.model.Projections;
import java.util.*;
import javafx.beans.Observable;
import javafx.beans.binding.ListBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.SelectionMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class PatientDashboardController extends BaseController implements Initializable {

    private PatientDashboardController main;
    public Stage primaryStage;
    private final Logger logger;

    public MongoDatabase db;
    public MongoCollection<Document> collection;
    private ObservableList<Patient> patientList;

    // Dashboard CSS file URL
    private final URL url;

    @FXML
    private TextField searchField;

    @FXML
    public VBox patientDashboardPane;

    @FXML
    TableView<Patient> patientsTableView;

    public PatientDashboardController() {
        this.logger = Logger.getLogger(getClass().getName());
        this.url = this.getClass().getResource("/patientrecords/styles/dashboard.css");
    }

    public void patientDashboardLoader(Stage primaryStage, MongoDatabase db) {
        this.primaryStage = primaryStage;
        this.db = db;
        this.collection = db.getCollection("Patients");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/patient/PatientsDashboard.fxml"));
        loader.setController(this);

        try {
            patientDashboardPane = (VBox) loader.load();
            patientDashboardPane.setAlignment(Pos.CENTER);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            // Scene scene = new Scene(userDashboardPane, 720, 745);
            Scene scene = new Scene(patientDashboardPane, 1420, 845);

            // Add css file
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("CSS URL not found!");
            }

            // stage.initModality(Modality.WINDOW_MODAL);
            primaryStage.setTitle("Users");
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

        // ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void setMain(PatientDashboardController main) {
        this.main = main;
    }

    @FXML
    public void populatePatients(FindIterable<Document> patientData) throws ClassNotFoundException {

        // Display row data usersTableView
        patientsTableView.setItems(parsePatientList(patientData));
    }

    /** TODO: PatientDashboardController Pagination
     * 1. Pagination in Java (Show first x records, next + previous page)
    
     */

    private ObservableList<Patient> parsePatientList(FindIterable<Document> result) {
        patientList = FXCollections.observableArrayList();

        // TODO: parseUserList()- Dialogue box if user has not permission to perform CRUD action
        // Throw error if user has no access to User collection
        try {

            // Iterate throught the result
            if (result == null) {
                System.out.println("No results found");

            } else {

                MongoCursor<Document> cursor = result.iterator();
                while (cursor.hasNext()) {
                    Patient patient = new Patient();
                    Document doc = cursor.next();

                    patient.setID(doc.get("_id").toString());
                    patient.setFileNo(doc.get("file").toString());

                    String identification = doc.get("identification") == null ? null
                            : doc.get("identification").toString();
                    patient.setIdentification(identification);

                    patient.setLastName(doc.get("lastname").toString());
                    patient.setGivenName(doc.get("givenName").toString());

                    List<Document> address = doc.get("address") == null ? null : (ArrayList) doc.get("address");
                    ObservableList<Document> addr = FXCollections.observableArrayList(address);
                    patient.setAddress(addr);

                    LocalDateTime dob = mongoDateToLDT(doc.get("dob").toString());
                    patient.setDoB(dob);

                    String phoneno = doc.get("phoneno") == null ? null : doc.get("phoneno").toString();
                    patient.setPhoneNo(phoneno);

                    String altphoneno = doc.get("altphoneno") == null ? null : doc.get("altphoneno").toString();
                    patient.setAltPhoneNo(altphoneno);

                    String email = doc.get("email") == null ? null : doc.get("email").toString();
                    patient.setEmail(email);

                    String bloodgroup = doc.get("bloodgroup") == null ? null : doc.get("bloodgroup").toString();
                    patient.setBloodGroup(bloodgroup);

                    String rh = doc.get("rh") == null ? null : doc.get("rh").toString();
                    patient.setRH(rh);

                    ObservableList<Document> allergies = doc.get("allergies") == null ? null
                            : (ObservableList) doc.get("allergies");
                    patient.setAllergies(allergies);

                    ObservableList<Document> chronic = doc.get("chronicdiseases") == null ? null
                            : (ObservableList) doc.get("chronicdiseases");
                    patient.setChronic(chronic);

                    ObservableList<Document> vaccinations = doc.get("vaccinations") == null ? null
                            : (ObservableList) doc.get("vaccinations");
                    patient.setVaccine(vaccinations);

                    LocalDateTime created = mongoDateToLDT(doc.get("datecreated").toString());
                    patient.setDateCreated(created);

                    // Add user to the ObservableList, userList
                    patientList.add(patient);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to parse patient-list successfully", e);
        }

        return patientList;
    }

    /**
    * Returns details on patients that matches the searchText (String)
    * @param searchText String: Search by name, ID No., Passport No., or file number
    * @return result FindIterable<Document>
    */
    private FindIterable<Document> searchPatient(String searchText) {
        FindIterable<Document> result = null;

        try {
            if (searchText != null && !searchText.equals(" ")) {
                searchText = searchText.trim();

                /**
                Bson query = or(regex("username", searchText), regex("lastname", searchText),
                    regex("firstname", searchText));
                result = collection.find(query);
                 */

                Document regQuery = new Document();
                regQuery.append("$regex", "^(?)" + Pattern.quote(searchText));
                regQuery.append("$options", "i");

                Document findQuery = new Document();
                findQuery.append("lastName", regQuery);
                findQuery.append("otherName", regQuery);
                findQuery.append("fileNo", regQuery);
                findQuery.append("idNo", regQuery); // National Identification Number
                findQuery.append("ppNo", regQuery); // Passport Number

                result = collection.find(findQuery);
            } else {
                result = listAllItems(collection);
            }

        } catch (MongoException e) {
            logger.log(Level.SEVERE, null, e);
        }

        return result;
    }

    @FXML
    @Override
    public void searchAction() {
        try {
            FindIterable<Document> result = searchPatient(searchField.getText());
            populatePatients(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Search action failed", e);
        }
    }

    @FXML
    @Override
    public void undoAction(ActionEvent event) {
        try {
            FindIterable<Document> result = listAllItems(collection);
            populatePatients(result);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @FXML
    @Override
    public void addAction(ActionEvent event) {
        // CreateUserController cuc = new CreateUserController(primaryStage, db);
        // cuc.userDashboardLoader(stage, db);
    }

    @FXML
    @Override
    public void viewAction(ActionEvent event) {
        //TODO
        /* TODO: Use in viewUser details
            Iterator it = userDetails.iterator();
            
            while (it.hasNext()) {
                System.out.println("\n--------- LOOP: it.hasNext() ----------");
                Document next = (Document) it.next();
                System.out.println(next.get("user"));
                System.out.println();
            }
            */
    }

    @FXML
    @Override
    public void updateAction(ActionEvent event) {
        // Pass
    }

    /**
     * Delete a document or multiple selected documents from Users COllection
    */
    @FXML
    @Override
    public void deleteAction(ActionEvent event) {
        // ObservableList<User> delList = FXCollections.observableArrayList();
        final List<ObjectId> delList = new ArrayList<>();

        patientsTableView.getItems().stream().filter((patient) -> (patient.getIsSelected())).forEachOrdered((patient) -> {
            delList.add(new ObjectId(patient.getID()));
        });

        // Require confirmation before deleting
        if (deleteConfirmation()) {

            // Throw exception if user does not have access permission to Collection, "User"
            try {
                Bson condition = new Document("$in", delList);
                Bson filter = new Document("_id", condition);

                //Removes the selected documents in the User collection
                collection.deleteMany(filter);

                // Refresh usersTableView
                FindIterable<Document> result = listAllItems(collection);
                populatePatients(result);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Patient not deleted successfully", e);
            }

        } else {
            System.out.println("-------------- deletePatient NOT confirmed --------------");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
