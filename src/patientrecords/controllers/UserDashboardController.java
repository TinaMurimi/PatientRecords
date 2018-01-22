package patientrecords.controllers;

// import patientrecords.Main;
import patientrecords.models.User;
import patientrecords.controllers.BaseInterface;

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
import javafx.geometry.Pos;

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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.bson.Document;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import javafx.util.Callback;

public class UserDashboardController implements BaseInterface, Initializable {

    private UserDashboardController main;
    private ObservableList<User> userList;

    private Stage stage;
    private DB db;

    @FXML
    private TextField searchField;

    @FXML
    private VBox userDashboardPane;

    @FXML
    TableView<User> usersTableView;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> jobCol;

    @FXML
    private TableColumn<User, Boolean> isActiveCol;

    @FXML
    private TableColumn<User, LocalDateTime> dateCreatedCol;

    @FXML
    private TableColumn<User, LocalDateTime> lastLoginCol;

    @FXML
    private TableColumn<User, String> isSelectedCol;

    @FXML // fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader

    @FXML // fx:id="delButton"
    private Button delButton; // Value injected by FXMLLoader

    @FXML // fx:id="addButton"
    private Button addButton;

    private CheckBox selectAll; // TODO

    // final TableColumn<Os, Boolean> loadedColumn = new TableColumn<>( "Delete" );
    // loadedColumn.setCellValueFactory( new PropertyValueFactory<>( "delete" ));
    // loadedColumn.setCellFactory( tc -> new CheckBoxTableCell<>());
    // columns.add( loadedColumn );

    public void userDashboardLoader(Stage stage, DB db) {
        this.stage = stage;
        this.db = db;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/Users.fxml"));
        loader.setController(this);

        try {
            userDashboardPane = (VBox) loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            Scene scene = new Scene(userDashboardPane, 1280, 800);
            stage.setTitle("Users");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

        // ((Node)(event.getSource())).getScene().getWindow().hide();

        parseUserList();
    }

    public void setMain(UserDashboardController main) {
        this.main = main;
    }

    /** TODO: UseDashBoardController
     * 1. Delete button
     * 2. TableView with items that are clickable
     * 3. Create repo from existing folder on local machine
     * 4. Pagination in Java (Show first x records, next + previous page)
     * 5. TableView with items with checkboxes
    
     */

    /**
     * Parses MongoDB Date to LocalDateTime
     * @param date (String) Date from MongoDB to be parsed
     * @return (LocalDateTime) parsed local date time
     */
    public LocalDateTime mongoDateToLDT(String date) {
        if (date.startsWith("TS time:")) {
            date = date.substring(8);
        }

        if (date.contains("inc:1")) {
            date = date.replace("inc:1", "");
        }

        date = date.trim();
        final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");

        // The parsed date
        final LocalDateTime parsed = LocalDateTime.parse(date, inputFormat);
        return parsed;
    }

    /**
     * @param  parsedDate (LocalDateTime) to format
     * @return (String) parsed local date time
     */
    public String LDTToString(LocalDateTime parsedDate) {
        // Convert LocalDateTime to String
        final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(parsedDate);
    }

    private ObservableList<User> parseUserList() {
        userList = FXCollections.observableArrayList();

        try {

            DBCollection collection = db.getCollection("Users");
            BasicDBObject searchObject = new BasicDBObject();

            String searchText = searchField.getText();

            if (searchText != null) {
                searchObject = new BasicDBObject();
                searchObject.put("username", Pattern.compile(Pattern.quote(searchText)));
            }

            BasicDBObject fieldObject = new BasicDBObject();
            fieldObject.put("password", 0);
            // fieldObject.put("_id", 0);

            DBCursor cursor = collection.find(searchObject, fieldObject).sort(new BasicDBObject("lastname", 1));
            if (cursor != null && cursor.count() > 0) {

                for (DBObject current : cursor) {
                    User user = new User();
                    DBObject doc = cursor.next();

                    user.setUserID(doc.get("_id").toString());
                    user.setUsername(doc.get("username").toString());

                    String title = doc.get("title").toString() == null ? "" : doc.get("title").toString();
                    user.setTitle(title);

                    user.setFirstName(doc.get("firstname").toString());
                    user.setLastName(doc.get("lastname").toString());

                    String otherName = doc.get("othername").toString() == null ? "" : doc.get("othername").toString();
                    user.setOtherName(otherName);

                    String jobTitle = doc.get("job").toString() == null ? "" : doc.get("job").toString();
                    user.setJob(jobTitle);

                    user.setIsActive(Boolean.valueOf(doc.get("active").toString()));

                    LocalDateTime created = mongoDateToLDT(doc.get("datecreated").toString());
                    user.setDateCreated(created);

                    String lastLoginDate = doc.get("lastlogin") == null ? null : doc.get("lastlogin").toString();
                    if (lastLoginDate != null) {
                        LocalDateTime lastLogin = mongoDateToLDT(lastLoginDate);
                        user.setLastLogin(lastLogin);
                    }

                    // Add user to the ObservableList, userList
                    userList.add(user);
                }
            }

        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, null, e);
        }

        return userList;
    }

    // Populate Users for TableView
    @FXML
    public void populateUsers(ObservableList<User> userData) throws ClassNotFoundException {
        // Display row data usersTableView
        usersTableView.setItems(userData);
        // usersTableView.getItems().setAll(parseUserList());
    }

    @FXML
    @Override
    public void searchAction(ActionEvent event) {
        //TODO
    }

    @FXML
    @Override
    public void addAction(ActionEvent event) {
        //TODO
    }

    @FXML
    @Override
    public void viewAction(ActionEvent event) {
        //TODO
    }

    @Override
    public void editAction(ActionEvent event) {
        // Pass
    }

    @FXML
    @Override
    public void deleteAction(ActionEvent event) {
        ObservableList<User> delList = FXCollections.observableArrayList();
        /**
        for (User user : userList){
            if (user.getIsSelected().isSelected()){
                delList.add(user);
            }
        }
        */

        userList.stream().filter((user) -> (user.getIsSelected().isSelected())).forEachOrdered((user) -> {
            delList.add(user);
        });

        System.out.println("-------------- deleteUser --------------");
        System.out.println(delList);

        // TODO: Delete users in list from DB
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        jobCol.setCellValueFactory(cellData -> cellData.getValue().jobProperty());

        isActiveCol.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        isActiveCol.setCellFactory(CheckBoxTableCell.forTableColumn(isActiveCol));

        dateCreatedCol.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        lastLoginCol.setCellValueFactory(cellData -> cellData.getValue().lastLoginProperty());

        isSelectedCol.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
        //        isSelectedCol.setCellFactory(CheckBoxTableCell.forTableColumn(isSelectedCol));

        try {
            populateUsers(parseUserList());
        } catch (ClassNotFoundException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, null, e);
        }

    }
}
