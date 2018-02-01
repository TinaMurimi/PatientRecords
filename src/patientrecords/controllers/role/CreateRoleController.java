/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patientrecords.controllers.role;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;
import org.bson.conversions.Bson;

import patientrecords.controllers.BaseController;
import patientrecords.controllers.user.UserDashboardController;
import patientrecords.models.User;

public class CreateRoleController extends RoleDashboardController {

    private Logger logger;
    private Stage stage;

    @FXML
    private AnchorPane createRoleForm;

    public void createUserLoader(MongoDatabase db) {

        // this.primaryStage = primaryStage;
        this.db = db;
        this.collection = db.getCollection("Users");

        // this.username = usernameField.getText();
        // this.password = passwordField.getText();
        // this.confirmPwd = confirmPwdField.getText();

        this.logger = Logger.getLogger(getClass().getName());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/role/CreateRoleForm.fxml"));
        loader.setController(this);

        try {
            createRoleForm = (AnchorPane) loader.load();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            Scene scene = new Scene(createRoleForm, 450, 482, Color.TRANSPARENT);

            // Add css file
            /**
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("CSS URL not found!");
            }
            */

            stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle("Create New User Group");
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

   

    private void createRole() {
        // List of privileges
        System.out.println("\n------MainController(): creating new role------");

        List<Document> privileges = new ArrayList<>();
        privileges.add(new Document("resource", new Document("db", db.getName()).append("collection", "Patients"))
                .append("actions", Arrays.asList("find", "update", "insert")));
        privileges.add(new Document("resource", new Document("db", db.getName()).append("collection", "Records"))
                .append("actions", Arrays.asList("find", "update", "insert")));
        privileges.add(new Document("resource", new Document("db", db.getName()).append("collection", "Appointments"))
                .append("actions", Arrays.asList("find", "update", "insert")));

        // parse command
        Document createRoleCommand = new Document("createRole", "EyGlas.nurse").append("privileges", privileges)
                .append("roles", Collections.EMPTY_LIST);

        try {
            db.runCommand(createRoleCommand);
            System.out.println("Successful");
        } catch (MongoCommandException e) {
            logger.log(Level.SEVERE, "Failed to create new role", e);
        }
    }
}
