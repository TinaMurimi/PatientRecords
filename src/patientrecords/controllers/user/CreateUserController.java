package patientrecords.controllers.user;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.Scene;

import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mongodb.BasicDBObject;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.operation.OperationExecutor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import java.util.List;
// import javafx.beans.binding.Bindings;
import javax.script.Bindings;
import org.bson.BSONObject;

import patientrecords.models.User;

public class CreateUserController extends UserDashboardController implements Initializable {

    // Dashboard CSS file URL
    private final URL url = this.getClass().getResource("/patientrecords/styles/form.css");

    private UserDashboardController main;
    public Stage stage;
    // private Stage primaryStage;
    private Logger logger;

    // private MongoDatabase db;
    // private MongoCollection<Document> collection;
    private OperationExecutor executor;

    private User user;

    private AnchorPane createUserForm;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField lastNameField;

    @FXML
    private Label lastNameRulesLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private JFXTextField otherNameField;

    @FXML
    private Label otherNameLabel;

    @FXML
    private Label otherNameRulesLabel;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private Label usernameRulesLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private JFXTextField jobTitleField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label passwordRulesLabel;

    @FXML
    private JFXPasswordField confirmPwdField;

    @FXML
    private JFXComboBox<Title> uTitleComboBox;

    @FXML
    private JFXComboBox<?> uGroupComboBox;
    
    @FXML
    private Label userGroupReqLabel;

    @FXML
    private CheckBox isActiveCheckBox;

    @FXML
    private Label errorMsgLabel;

    @FXML
    private JFXButton newGroupButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    public JFXButton cancelButton;

    @FXML
    private Label confirmPwdReqLabel;

    @FXML
    private Label usernameReqLabel;

    @FXML
    private Label passwordReqLabel;

    @FXML
    private Label lastNameReqLabel;

    @FXML
    private Label otherNameReqLabel;

    /**
     * TODO: Class CreateUserController
     * 1. Link from UserDashboardController + MenuBar
     */

    /**
     * Controller for creating a new user
     * @param db
     */
    public void createUserLoader(MongoDatabase db) {

        // this.primaryStage = primaryStage;
        this.db = db;
        this.collection = db.getCollection("Users");

        // this.username = usernameField.getText();
        // this.password = passwordField.getText();
        // this.confirmPwd = confirmPwdField.getText();

        this.logger = Logger.getLogger(getClass().getName());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/user/CreateUserForm.fxml"));
        loader.setController(this);

        try {
            createUserForm = (AnchorPane) loader.load();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            Scene scene = new Scene(createUserForm, 450, 482, Color.TRANSPARENT);

            // Add css file
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("CSS URL not found!");
            }

            stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle("Create New User");
            stage.setFullScreen(false);
            stage.initOwner(primaryStage); // UserDashBoardController.primaryStage: UserDashboeard stage
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            // stage.showAndWait();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    /**
     * Checks if a username exists/ is already in user in the database
     * @param username (String) Username to validate
     * @return userExists (Boolean)
    */
    public boolean checkUserExists(String username) {
        boolean userExists = false;
        try {
            BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
            builder.push("usersInfo").add("user", username).add("db", db.getName()).add("showPrivileges", true);

            Document result = db.runCommand((Bson) builder.get());
            ArrayList userDetails = (ArrayList) result.get("users");
            if (!userDetails.isEmpty()) {
                userExists = true;
            }
        } catch (MongoCommandException e) {
            if (e.getCode() != 13) {
                throw e;
            }
        } catch (MongoException e) {
            logger.log(Level.WARNING, null, e);
        }
        return userExists;
    }

    public void saveAction() {
        // TODO: CreateUserContoller.saveAction()
        /** Validation
         * Username unique
         * Mandatory fields are not null or " "
         * If no group inactive user (Warning)
         */
        // TODO: CreateUserContoller Update UserTableView on Sava

        user = new User();
        errorMsgLabel.setText(null);

        /** Validate lastName
         * lastName should not start with a number
         * lastName should be longer than 2 characters
         * lastName should NOT contain special characters
         */
        final String lname = lastNameField.getText();
        // if (lname != null && lname.trim().length() != 0 && lname.matches("\\b[a-zA-Z]{3,10}\\b")) {
        if (lname != null && lname.matches("\\b[a-zA-Z]{3,10}\\b")) {
            // errorMsgLabel.setText(null);
            lastNameReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setLastName(lname);
        } else {
            errorMsgLabel.setText("Invalid last name");
            lastNameReqLabel.setStyle("-fx-text-fill: RED;");
            // saveButton.setDisable(true);
        }

        /** Validate otherNames
         * otherNames should not start with a number
         * otherNames should be longer than 2 characters
         * otherNames should NOT contain special characters
         */
        final String oNames = otherNameField.getText();
        // if (oNames != null && oNames.trim().length() != 0 && (!oNames.matches("\\b[a-zA-Z]{3,20}\\b"))) {
        if (oNames != null && oNames.matches("\\b[a-zA-Z]{3,20}\\b")) {
            // errorMsgLabel.setText(null);
            otherNameReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setOtherName(oNames);
        } else {
            errorMsgLabel.setText("Invalid name");
            otherNameReqLabel.setStyle("-fx-text-fill: RED;");
            // saveButton.setDisable(true);
        }

        /** Validate username
         * Username should not start with a number
         * Username should be longer than 6 characters
         * Username should NOT contain special characters
         */
        final String username = usernameField.getText();
        if (username != null && username.matches("\\b[a-zA-Z][a-zA-Z0-9\\-._]{5,10}\\b")) {
            // Check if username exists
            if (checkUserExists(username)) {
                errorMsgLabel.setText("Username exists");
                usernameReqLabel.setStyle("-fx-text-fill: RED;");
            } else {
                // errorMsgLabel.setText(null);
                usernameReqLabel.setStyle("-fx-text-fill: WHITE;");
                user.setUsername(username);
            }
        } else {
            errorMsgLabel.setText("Invalid username");
            usernameReqLabel.setStyle("-fx-text-fill: RED;");
            // saveButton.setDisable(true);
        }

        /**
         * Validate userGroup
         */
        /*
        String userGroup = uGroupComboBox.getValue();
        if (uTitleComboBox.getValue()) {
            // errorMsgLabel.setText(null);
            userGroupReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setRole(userGroup);
        } else {
            errorMsgLabel.setText("User group required");
            userGroupReqLabel.setStyle("-fx-text-fill: RED;");
        }
        */

        /** Validate password
         * password should be longer than 6 characters
         */
        String password = passwordField.getText();
        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,15}$";
        if (password != null && password.matches(passwordPattern)) {
            // errorMsgLabel.setText(null);
            passwordReqLabel.setStyle("-fx-text-fill: WHITE;");
        } else {
            errorMsgLabel.setText("Invalid password");
            passwordReqLabel.setStyle("-fx-text-fill: RED;");
            // saveButton.setDisable(true);
        }

        /**
         * Confirm passwords match
         */
        String confirmPwd = confirmPwdField.getText();
        if (confirmPwd != null && confirmPwd.trim().length() > 0 && confirmPwd.equals(password)) {
            // errorMsgLabel.setText(null);
            confirmPwdReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setPassword(password);
        } else {
            errorMsgLabel.setText("Passwords do NOT match");

            confirmPwdField.clear();
            confirmPwdReqLabel.setStyle("-fx-text-fill: RED;");

            passwordField.clear();
            passwordReqLabel.setStyle("-fx-text-fill: RED;");
            // saveButton.setDisable(true);
        }

        // username, password, userGroup
        // title, lname, oNames, job, active

        // Get uTitle
        String uTitle;
        if (uTitleComboBox.getValue() != null) {
            uTitle = uTitleComboBox.getValue().getTitle();
            user.setTitle(uTitle);
        }

        // Get job
        String job;
        if (jobTitleField.getText() != null) {
            job = jobTitleField.getText();
            user.setJob(job);
        }

        // Get isActive
        Boolean isActive = false;
        if (isActiveCheckBox.isSelected()) {
            isActive = true;
        }
        user.setIsActive(isActive);
        
        /**
         * Insert user to DB
         */
        createUser(user);

    }

    /**Creates a new user
     * @param user
     */
    private void createUser(User user) {
        Document customData = new Document("title", user.getTitle())
                .append("lastname", user.getLastName())
                .append("otherName", user.getOtherName()).append("job", user.getJob())
                .append("active", user.getIsActive()).append("created", user.getDateCreated())
                .append("lastLogin", null);

        Document command = new Document("createUser", user.getUsername())
                .append("pwd", user.getPassword())
                .append("customData", customData)
                .append("roles", Collections.singletonList(user.getRole()))
                .append("digestPassword", true);

        try {
            db.runCommand(command);
        } catch (MongoCommandException e) {
            throw e;
        }
    }

    public void newGroupAction() {
        // TODO: CreateUserContoller.newGroupAction()
    }

    public void cancelAction() {
        // TODO: CreateUserContoller.cancelAction()
        stage.close();
    }

    public void setMain(CreateUserController main) {
        this.main = main;
    }

    public enum Title {
        None(null), Dr("Dr."), Mr("Mr."), Ms("Ms."), Miss("Miss"), Prof("Prof."), Mx("Mx."), Other("Other");

        private final String title;

        Title(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsgLabel.setText(null);

        // Populate the UTitle JFXComboBox
        uTitleComboBox.getItems().setAll(Title.values());
        // ObservableList<Title> options = FXCollections.observableArrayList(Title.values());
        // uTitle.setItems(options);
        uTitleComboBox.setVisibleRowCount(3);
        uTitleComboBox.setValue(null);

        // Show hint for lastName rules
        final String lastNameRules = "Last Name requirements:\n" + " 1. Last name is mandatory\n"
                + "  ... and should be/have:\n" + " 2. 3-10 characters\n" + " 3. allowed alphabets a-zA-Z\n";

        lastNameRulesLabel.setText(lastNameRules);
        lastNameLabel.setOnMouseEntered(event -> lastNameRulesLabel.setVisible(true));

        lastNameLabel.setOnMouseExited((MouseEvent e) -> {
            lastNameRulesLabel.setVisible(false);
        });

        // Show hint for otherName rules
        final String otherNameRules = "Other Name requirements:\n" + " 1. Other name is mandatory\n"
                + "  ... and should be/have:\n" + " 2. 3-15 characters\n" + " 3. allowed alphabets a-zA-Z\n";

        otherNameRulesLabel.setText(otherNameRules);
        otherNameLabel.setOnMouseEntered(event -> otherNameRulesLabel.setVisible(true));

        otherNameLabel.setOnMouseExited((MouseEvent e) -> {
            otherNameRulesLabel.setVisible(false);
        });

        // Show hint for username rules
        final String usernameRules = "Username requirements:\n" + " 1. Username is mandatory\n"
                + "  ... and should (be/have):\n" + " 2. 5-10 characters\n" + " 3. start with a letter\n"
                + " 4. Allowed a-z, A-Z, 0-9, points, dashes and underscores\n";

        usernameRulesLabel.setText(usernameRules);
        usernameLabel.setOnMouseEntered(event -> usernameRulesLabel.setVisible(true));

        usernameLabel.setOnMouseExited((MouseEvent e) -> {
            usernameRulesLabel.setVisible(false);
        });

        // Show hint for password rules
        final String passwordRules = "Password requirements:\n" + " 1. Password is mandatory\n"
                + "\t... and should be/have:\n" + " 2. 6-15 characters\n" + " 3. a digit must occur at least once\n"
                + " 4. a lower case letter must occur at least once\n"
                + " 5. an upper case letter must occur at least once\n"
                + " 6. no whitespace allowed in the entire string";

        passwordRulesLabel.setText(passwordRules);
        passwordLabel.setOnMouseEntered(event -> passwordRulesLabel.setVisible(true));

        passwordLabel.setOnMouseExited((MouseEvent e) -> {
            passwordRulesLabel.setVisible(false);
        });

        /**
        // Disable saveButton if all mandatory fields do NOT satisfy constraints
        if (lnameValid.get() && oNameValid.get() && usernameValid.get() && passwordValid.get()
                && confirmPwdValid.get()) {
            saveButton.setDisable(false);
           errorMsgLabel.setText(null);
        } else {
            saveButton.setDisable(true);
        
            if (!lnameValid.get()) {
                lastNameReqLabel.setStyle("-fx-text-fill: red;");
            }
        
            if (!oNameValid.get()) {
                otherNameReqLabel.setStyle("-fx-text-fill: red;");
            }
        
            if (!usernameValid.get()) {
                usernameReqLabel.setStyle("-fx-text-fill: red;");
            }
        
            if (!passwordValid.get() || !confirmPwdValid.get()) {
                passwordReqLabel.setStyle("-fx-text-fill: red;");
                confirmPwdReqLabel.setStyle("-fx-text-fill: red;");
            }
        }
        */

    }
}