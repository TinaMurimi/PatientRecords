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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
// import javafx.beans.binding.Bindings;
import javax.script.Bindings;
import org.bson.BSONObject;

import patientrecords.models.User;
import patientrecords.controllers.role.RoleDashboardController;
import patientrecords.models.Role;

public class EditUserController extends UserDashboardController implements Initializable {

    // Dashboard CSS file URL
    FXMLLoader loader = new FXMLLoader();
    
    private final URL url = this.getClass().getResource("/patientrecords/styles/form.css");
    private ObservableList<User> userDetails;

    private UserDashboardController main;
    public Stage stage;
    private Logger logger;
    private User user;
    private final String username;
    private Title t;

    @FXML
    private AnchorPane editUserForm;

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
    private JFXComboBox<Role> uGroupComboBox;

    @FXML
    private CheckBox isActiveCheckBox;

    @FXML
    private Label errorMsgLabel;

    @FXML
    private JFXButton newGroupButton; // TODO newGroupButton

    @FXML
    private JFXButton updateButton;

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

    @FXML
    private Label userGroupReqLabel;
    
    @FXML
    private Hyperlink resetPwdLink;

    /**
     * TODO: Class EditUserController
     * 1. Link from UserDashboardController + MenuBar
     */
    
    public EditUserController(String username){
        this.username = username;
    }

    /**
     * Controller for creating a new user
     * @param db
     */
    public void editUserLoader(MongoDatabase db) {
        this.db = db;
        this.logger = Logger.getLogger(getClass().getName());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/user/EditUserForm.fxml"));
        loader.setController(this);

        try {
            editUserForm = (AnchorPane) loader.load();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            Scene scene = new Scene(editUserForm, 450, 482, Color.TRANSPARENT);

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
            stage.initOwner(primaryStage); // UserDashBoardController.primaryStage: UserDashboard stage
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    
    private ObservableList<User> parseUserDetails(List<Document> result) {
        
        System.out.println();
        System.out.println("----------- parseUserDetails -----------");
        
        
        userDetails = FXCollections.observableArrayList();
        
        // TODO: parseUserList()- Dialogue box if user has not permission to perform CRUD action
        // Throw error if user has no access to system.users
        try {

            // Iterate throught the result
            if (result.isEmpty()) {
                System.out.println("No results found");

            } else {
                for (Document doc : result){
                    this.user = new User();
                   
                    user.setUserID(doc.get("_id").toString());
                    
                    // Username
                    user.setUsername(doc.get("user").toString());
                    usernameField.setText(doc.get("user").toString());

                    // Extract customData
                    Document customData = (Document) doc.get("customData");
                    if (customData != null){
                        String title = customData.get("title") == null ? null : customData.get("title").toString();
                        if (title == null) {
                            uTitleComboBox.setValue(null);
                        } else {
                            user.setTitle(title);
                            uTitleComboBox.setValue(getTitle(title));
                        }

                        String lastName = customData.get("lastName") == null ? null : customData.get("lastName").toString();
                        user.setLastName(lastName);
                        lastNameField.setText(lastName);
                        
                        String otherName = customData.get("otherName") == null ? null : customData.get("otherName").toString();
                        user.setOtherName(otherName);
                        otherNameField.setText(otherName);

                        String jobTitle = customData.get("job") == null ? null : customData.get("job").toString();
                        user.setJob(jobTitle);
                        jobTitleField.setText(jobTitle);

                        Boolean active = customData.get("active") == null ? null : Boolean.valueOf(customData.get("active").toString());
                        user.setIsActive(active);
                        isActiveCheckBox.setSelected(active);
                    }
                    
                    // Get user role
                    List<Document> roles = (ArrayList) doc.get("roles");
                    if (roles != null){
                        for (Document role : roles) {
                            String uRole = role.get("role") == null ? null : role.get("role").toString();
                            user.setTitle(uRole);
                            Role uGroup = new Role(uRole);
                            uGroup.setCode(uRole);
                            uGroup.setName(uRole);

                            // TODO: Value is not displayed. WHY OH WHY?
                            uGroupComboBox.setValue(uGroup);
                        }
                    }
                    
                    // Add user to the ObservableList, userList
                    userDetails.add(user);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to parse user-list successfully", e);
        }

        return userDetails;
    }

    public void updateAction() {
        
        // If any input field is not valid variable validInput is set to false
        boolean validInput = true;
        errorMsgLabel.setText(null);

        /** Validate lastName
         * Mandatory single name, WITHOUT spaces, WITH special characters:
         * lastName should not contain a number
         * lastName should be longer than 2 characters
         */
        final String lname = lastNameField.getText();
        // if (lname != null && lname.trim().length() != 0 && lname.matches("\\b[a-zA-Z]{3,10}\\b")) {
        // if (lname != null && lname.matches("\\b[a-zA-Z]{3,10}\\b")) {
        if (lname != null && lname.matches("^[A-Za-z]{3,10}(((\\'|\\-|\\.)?([A-Za-z])+))?$")) {
            if (lname.trim() == null) {
                errorMsgLabel.setText("Invalid last name");
                lastNameReqLabel.setStyle("-fx-text-fill: RED;");
                validInput = false;
            } else {
                lastNameReqLabel.setStyle("-fx-text-fill: WHITE;");
                user.setLastName(lname);
            }
        } else {
            errorMsgLabel.setText("Invalid last name");
            lastNameReqLabel.setStyle("-fx-text-fill: RED;");
            validInput = false;
        }

        /** Validate otherNames
         * Mandatory single name, optional additional names, WITH spaces, WITH special characters:
         * otherNames should not contain a number
         * otherNames should be longer than 2 characters
         * otherNames can contain special characters
         */
        final String oNames = otherNameField.getText();
        // if (oNames != null && oNames.matches("\\b[a-zA-Z]{3,20}\\b")) {
        if (oNames != null && oNames.matches("^[A-Za-z]{3,10}((\\s)?((\\'|\\-|\\.)?[A-Za-z]{3,10}))*$")) {
            otherNameReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setOtherName(oNames);
            // validInput = true;
        } else {
            errorMsgLabel.setText("Invalid name");
            otherNameReqLabel.setStyle("-fx-text-fill: RED;");
            validInput = false;
        }

        

        /**
         * Validate userGroup
         */
        Role userGroup = uGroupComboBox.getValue();
        if (userGroup != null && !userGroup.getName().equals("")) {
            userGroupReqLabel.setStyle("-fx-text-fill: WHITE;");
            user.setRole(userGroup.getName());
            // validInput = true;
        } else {
            errorMsgLabel.setText("User group required");
            userGroupReqLabel.setStyle("-fx-text-fill: RED;");
            validInput = false;
        }

        
        
        // Get uTitle
        String uTitle;
        if (uTitleComboBox.getValue() != null) {
            uTitle = uTitleComboBox.getValue().getEnumTitle();
            user.setTitle(uTitle);
        }

        // Get job
        String job;
        if (jobTitleField.getText() != null || !(jobTitleField.getText().equals(""))) {
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
        if (validInput){
            // createUser(user);
            
            // Clear all fields
            uTitleComboBox.setValue(null);
            lastNameField.clear();
            otherNameField.clear();
            usernameField.clear();
            jobTitleField.clear();
            uGroupComboBox.setValue(null);
            passwordField.clear();
            confirmPwdField.clear();
            isActiveCheckBox.setSelected(true);
            
            errorMsgLabel.setText("User created successfully");
        }
    }
    
    /**
     * Updates user password
     */
    public void updatePassword(){
        String dialogCSS = "/patientrecords/styles/dialog.css";

        Dialog <String> dialog = new Dialog<>();
        dialog.setTitle("Reset User Password");
        
        // Set the button types.
        ButtonType updateButtonType = new ButtonType("Reset", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        // Set dialog css
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(dialogCSS).toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // pwddResetField
        JFXPasswordField pwddResetField = new JFXPasswordField();
        pwddResetField.setPromptText("Enter new password");
        
        final Tooltip tooltip = new Tooltip();
        final String passwordRules = " 1. Password is mandatory and should be/have:\n"
                + " 2. 6-15 characters\n"
                + " 3. a digit must occur at least once\n"
                + " 4. a lower case letter must occur at least once\n"
                + " 5. an upper case letter must occur at least once\n"
                + " 6. no whitespace allowed in the entire string";
        
        tooltip.setText(passwordRules);
        pwddResetField.setTooltip(tooltip);
        
        // confPwddResetField
        JFXPasswordField confPwddResetField = new JFXPasswordField();
        confPwddResetField.setPromptText("Confirm new password");
        
        gridPane.add(new Label("New Password:"), 0, 1);
        gridPane.add(pwddResetField, 1, 1);
        gridPane.add(new Label("ConfirmPassword:"), 0, 2);
        gridPane.add(confPwddResetField, 1, 2);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the pwddResetField field by default.
        Platform.runLater(() -> pwddResetField.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                
                boolean validPwd = true;
                
                /** Validate password
                * password should be longer than 6 characters
                */
               String password = pwddResetField.getText();
               final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,15}$";
               if (!(password != null && password.matches(passwordPattern))) {
                   validPwd = false;
               }
               
               /**
                * Confirm passwords match
                */
               String confirmPwd = confPwddResetField.getText();
               if (!(confirmPwd != null && confirmPwd.trim().length() > 0 && confirmPwd.equals(password))) {
                   validPwd = false;
               }
                
                
                if (validPwd) {
                    return password;
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    
                    DialogPane alertPane = alert.getDialogPane();
                    alertPane.getStylesheets().add(getClass().getResource(dialogCSS).toExternalForm());
                    alertPane.getStyleClass().add("myDialog");
 
                    alert.setTitle("Error alert");
                    alert.setHeaderText("Password was not reset");
                    alert.setContentText("Ensure that the passwords match and the new password meets the following requirements:\n"
                            +passwordRules
                    );

                    alert.showAndWait();
                    return null;
                }
                
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            Document resetCommand = new Document ("updateUser", username).append("pwd", result.get());
            db.runCommand(resetCommand);
            errorMsgLabel.setText("Password reset successfully");
        }
    }
    

    /**Creates a new user
     * @param user
     */
    private void createUser(User user) {
        
        Document customData = new Document("title", user.getTitle())
                .append("lastName", user.getLastName())
                .append("otherName", user.getOtherName())
                .append("job", user.getJob())
                .append("active", user.getIsActive())
                .append("created", new Date())
                .append("lastLogin", null);
        

        Document command = new Document("createUser", user.getUsername())
                .append("pwd", user.getPassword())
                .append("customData", customData)
                .append("roles", Collections.singletonList(user.getRole()))
                .append("digestPassword", true);

        try {
            db.runCommand(command);
            
        } catch (MongoCommandException e) {
            if (e.getErrorCode() == 31){
                logger.log(Level.SEVERE, "RoleNotFound", e);
            } else {
                logger.log(Level.SEVERE, null, e);
            }
        }
    }

    public void newGroupAction() {
        // TODO: EditUserContoller.newGroupAction()
    }

    public void cancelAction() {
        // TODO: EditUserContoller.cancelAction()
        stage.close();
    }

    public void setMain(EditUserController main) {
        this.main = main;
    }

    /**
     * Initializes the controller class.
     * @param url (URL)
     * @param rb (ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMsgLabel.setText(null);

        // Populate the UTitle JFXComboBox
        uTitleComboBox.getItems().setAll(Title.values());
        uTitleComboBox.getSelectionModel().select(1);
        uTitleComboBox.setVisibleRowCount(3);
        
        // Populate the uGroupComboBox JFXComboBox
        RoleDashboardController rdc = new RoleDashboardController();
        rdc.db = db;
        ObservableList<Role> roles = rdc.getRoleNameList();
        roles.sort(Comparator.comparing(Role::getCode)); // Sort the list
        uGroupComboBox.getItems().setAll(roles);
        uGroupComboBox.setVisibleRowCount(5);

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
        
        /**
        resetPwdLink.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            updatePassword();
            }
        });
        */

        // Populate form with data from DB system.users
        parseUserDetails(getUser(username));
        System.out.println("uGroupComboBox.getValue(): "+ uGroupComboBox.getValue());
    }
}