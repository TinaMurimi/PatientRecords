package patientrecords;

import java.io.*;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.beans.value.ObservableValue;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import com.mongodb.DB;

import patientrecords.authentication.DBAuthentication;
import patientrecords.authentication.UserCredentials;
import patientrecords.controllers.DashboardController;


public class MainController implements Initializable {
    // public class MainController extends AnchorPane {
    private Main main;
    public Stage stage;

    //     @FXML
    //     private AnchorPane rootPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label uNameReqLabel;

    @FXML
    private Label uPwdReqLabel;

    @FXML
    private Label errorMsgLabel;

    @FXML
    private Button exitButton;
    @FXML
    private Button learnMoreButton;
    @FXML
    private Button signinButton;

    @FXML
    private JFXTextField username_field;
    @FXML
    private JFXPasswordField password_field;

    public void loaderInit() {
        //        try {
        // view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
        //        loader.setRoot(this);
        loader.setController(this);

        //            Parent pane = (Parent) loader.load();
        //            pane = loader.load();

        try {
            //            loader.load();
            //            rootPane = (AnchorPane)loader.load();
            rootPane = loader.load();

        } catch (IOException exception) {
            //            throw new RuntimeException(exception);
            exception.printStackTrace();
        }

        // scene on stage
        Scene scene = new Scene(rootPane, 760, 297);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Patient Management System");
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
    }

    //     connect main class to controller
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void exitButtonAction(ActionEvent event) throws IOException {
        Platform.exit();
    }

    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
//        String username = username_field.getText();
//        String password = password_field.getText();
        
        String username = "tina";
        String password = "zyx321";

//        if (username.equals(" ") || password.equals(" ")) {
//            errorMsgLabel.setText("Invalid username/password");
//        } else {
            DBAuthentication dbAuth = new DBAuthentication(username, password);
            HashMap<String, HashMap<String, Object>> conn = dbAuth.getConn();
            Boolean auth = (Boolean) conn.get(username).get("auth");
            DB db = (DB) conn.get(username).get("database");
        
//            if (auth) {
//                // errorMsgLabel.setText("Login successful");
                DashboardController dc = new DashboardController();
                dc.dashboardLoader(stage, db);
//                
//                
//            } else {
//                errorMsgLabel.setText("Invalid username/password");
//            }
//        }
    }

    @Override
    //    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Check to avoid NullPointerException
        assert password_field != null : "fx:id=\"password_field\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert username_field != null : "fx:id=\"username_field\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
        assert signinButton != null : "fx:id=\"signinButton\" was not injected: check your FXML file 'FXMLDocument.fxml'.";

        username_field.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> o, Boolean olduNameVal, Boolean newuNameVal) -> {
                    if (!newuNameVal
                            || (username_field.getText() != null && username_field.getText().trim().length() != 0)) {
                        uNameReqLabel.setText("");
                    } else {
                        uNameReqLabel.setText("Username required");
                    }

                    if (password_field.getText().trim().length() != 0 && password_field.getText() != null) {
                        uPwdReqLabel.setText("");
                    } else {
                        uPwdReqLabel.setText("Password required");
                    }
                });

        password_field.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> o, Boolean oldPwdVal, Boolean newPwdVal) -> {
                    if (!newPwdVal
                            || (password_field.getText() != null && password_field.getText().trim().length() != 0)) {
                        uPwdReqLabel.setText("");
                    } else {
                        uPwdReqLabel.setText("Password required");
                    }

                    if (username_field.getText() != null && username_field.getText().trim().length() != 0) {
                        uNameReqLabel.setText("");
                    } else {
                        uNameReqLabel.setText("Username required");
                    }
                });
    }
}
