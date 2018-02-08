package patientrecords.controllers.user;

import patientrecords.models.User;
import patientrecords.controllers.BaseController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.ListBinding;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * 
 * @author tinabob
 * PENDING in Users Feature
 * - Pagination
 * - Check if user has permission to access system.users admin collection
 * - Menubar
 * - Dahsboard
 * - Toogle
 */

public class UserDashboardController extends BaseController implements Initializable {

    private UserDashboardController main;
    public Stage primaryStage;
    private final Logger logger;

    public MongoDatabase db;
    public MongoCollection<Document> collection;
    private ObservableList<User> userList;
    private final HashMap<String, User> usernames = new HashMap<>();
    private final URL url; // Dashboard CSS file URL

    @FXML
    private TextField searchField;

    @FXML
    public VBox userDashboardPane;

    @FXML
    TableView<User> usersTableView;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> lastNameCol;

    @FXML
    private TableColumn<User, String> concatNameCol;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private TableColumn<User, String> jobCol;

    @FXML
    private TableColumn<User, Boolean> isActiveCol;

    @FXML
    private TableColumn<User, LocalDateTime> dateCreatedCol;

    @FXML
    private TableColumn<User, LocalDateTime> lastLoginCol;

    @FXML
    private TableColumn<User, Boolean> isSelectedCol;

    @FXML // fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader

    @FXML // fx:id="delButton"
    private Button delButton; // Value injected by FXMLLoader

    @FXML // fx:id="addButton"
    private Button addButton;

    @FXML // fx:id="undoButton"
    private Button undoButton;

    private CheckBox selectAllCheckBox;

    public UserDashboardController() {
        this.logger = Logger.getLogger(getClass().getName());
        this.url = this.getClass().getResource("/patientrecords/styles/dashboard.css");
    }

    public void userDashboardLoader(Stage primaryStage, MongoDatabase db) {
        this.primaryStage = primaryStage;
        this.db = db;
        this.collection = db.getCollection("Users");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/user/UsersDashboard.fxml"));
        loader.setController(this);

        try {
            userDashboardPane = (VBox) loader.load();
            userDashboardPane.setAlignment(Pos.CENTER);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            Scene scene = new Scene(userDashboardPane, 1270, 845);

            // Add css file
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                System.out.println("CSS URL not found!");
            }

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

    public void setMain(UserDashboardController main) {
        this.main = main;
    }

    @FXML
    public void populateUsers(List<Document> userData) {

        // Ensure isSelected checkbox is removed after a document is deleted
        setSelectAllCheckBox();

        // Display row data usersTableView
        usersTableView.setItems(parseUserList(userData));
    }

    /** TODO: UseDashBoardController Pagination
     * 1. Pagination in Java (Show first x records, next + previous page)
    
     */

    /**
     * Returns a list of all users in the DB
     * @return users
     */
    public List<Document> getUsers() {

        List<Document> users = new ArrayList<>();
        Document command = new Document("usersInfo", 1);

        try {
            Document result = db.runCommand(command);
            users = (ArrayList) result.get("users");
        } catch (MongoCommandException e) {
            logger.log(Level.SEVERE, "Unable to fetch list of all users", e);
        }

        return users;
    }

    /**
     * Returns the details of a single user in the DB
     * @param user (String) username to fetch from DB
     * @return users
     */
    public List<Document> getUser(String user) {

        List<Document> users = new ArrayList<>();
        Document command = new Document("usersInfo", user);

        try {
            Document result = db.runCommand(command);
            users = (ArrayList) result.get("users");
        } catch (MongoCommandException e) {
            logger.log(Level.SEVERE, "Unable to fetch user details", e);
        }

        return users;
    }

    private ObservableList<User> parseUserList(List<Document> result) {

        userList = FXCollections.observableArrayList();

        // TODO: parseUserList()- Dialogue box if user has not permission to perform CRUD action
        // Throw error if user has no access to system.users
        try {

            // Iterate throught the result
            if (result == null || result.isEmpty()) {
                System.out.println("No results found");

            } else {
                for (Document doc : result) {

                    User user = new User();

                    user.setUserID(doc.get("_id").toString());
                    user.setUsername(doc.get("user").toString());

                    // Extract customData
                    Document customData = (Document) doc.get("customData");
                    if (customData != null) {
                        String title = customData.get("title") == null ? null : customData.get("title").toString();
                        user.setTitle(title);

                        String lastName = customData.get("lastName") == null ? null
                                : customData.get("lastName").toString();
                        user.setLastName(lastName);

                        String otherName = customData.get("otherName") == null ? null
                                : customData.get("otherName").toString();
                        user.setOtherName(otherName);

                        String jobTitle = customData.get("job") == null ? null : customData.get("job").toString();
                        user.setJob(jobTitle);

                        Boolean active = customData.get("active") == null ? null
                                : Boolean.valueOf(customData.get("active").toString());
                        user.setIsActive(active);

                        LocalDateTime created = mongoDateToLDT(customData.get("created").toString());
                        user.setDateCreated(created);

                        String lastLoginDate = customData.get("lastlogin") == null ? null
                                : customData.get("lastlogin").toString();
                        if (lastLoginDate != null) {
                            LocalDateTime lastLogin = mongoDateToLDT(lastLoginDate);
                            user.setLastLogin(lastLogin);
                        }
                    }

                    // Get user role
                    List<Document> roles = (ArrayList) doc.get("roles");
                    if (roles != null) {
                        for (Document role : roles) {
                            String uRole = role.get("role") == null ? null : role.get("role").toString();
                            user.setRole(uRole);
                        }
                    }

                    // Add user to the ObservableList, userList
                    userList.add(user);

                    // Add username to usernames HashMap
                    usernames.put(doc.get("user").toString(), user);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to parse user-list successfully", e);
        }

        return userList;
    }

    /**
     * Returns a list of documents (user) in the collection system.users
     * that match the pattern in searchField
     * @return userData (FindIterable<Document>)
     */
    private List<Document> searchItems(String searchText) {
        List<Document> matches = new ArrayList<>();
        List<Document> users = new ArrayList<>();

        if (searchText != null && !searchText.equals(" ")) {
            Pattern p = Pattern.compile(searchText.trim());

            /**
             * Get usernames that match searchText
             * Searches over username, lastName, firstName
            */
            for (Map.Entry<String, User> user : usernames.entrySet()) {

                User u = user.getValue();
                if (p.matcher(user.getKey()).find() || (u.getLastName() != null && p.matcher(u.getLastName()).find())
                        || (u.getOtherName() != null && p.matcher(u.getOtherName()).find())) {

                    matches.add(new Document("user", user.getKey()).append("db", db.getName()));
                }
            }

            if (!matches.isEmpty()) {
                try {
                    Document command = new Document("usersInfo", matches);
                    Document result = db.runCommand(command);
                    users = (ArrayList) result.get("users");
                } catch (MongoCommandException e) {
                    logger.log(Level.SEVERE, "Unable to fetch user details", e);
                }
            }
        } else {
            users = getUsers();
        }

        return users;
    }

    @FXML
    @Override
    public void searchAction() {
        try {
            List<Document> result = searchItems(searchField.getText());
            populateUsers(result);
            parseUserList(getUsers());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Search action failed", e);
        }
    }

    /**
     * Reload usersTableView to list all users after performing search action
     * @param event 
     */
    @FXML
    @Override
    public void undoAction(ActionEvent event) {
        try {
            populateUsers(getUsers());
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @FXML
    @Override
    public void addAction(ActionEvent event) {
        // pass
    }

    @FXML
    @Override
    public void viewAction(ActionEvent event) {
        // pass
    }

    @FXML
    @Override
    public void updateAction(ActionEvent event) {
        // Pass
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

    /**
     * Delete a document or multiple selected documents from Users COllection
     * @param event
    */
    @FXML
    @Override
    public void deleteAction(ActionEvent event) {
        final List<String> delList = new ArrayList<>();

        // Get selected users
        usersTableView.getItems().stream().filter((user) -> (user.getIsSelected())).forEachOrdered((user) -> {
            delList.add(user.getUsername());
        });

        if (delList.isEmpty()) {
            // Display warning dialogue box: no items selected
            deleteWarning();
        } else {

            // Require user to confirm before deleting
            if (deleteConfirmation()) {

                // TODO: deleteUser Throw exception if user does not have access permission to system.users

                try {
                    //Removes the selected documents in the User collection
                    for (String user : delList) {
                        Document delCommand = new Document("dropUser", user);
                        db.runCommand(delCommand);
                    }
                } catch (MongoCommandException e) {
                    logger.log(Level.SEVERE, "User not deleted successfully", e);
                }

                // reload usersTableView
                populateUsers(getUsers());

            }
        }
    }

    /**
     * Adds an EventHandler to the CheckBox to select/deselect all users in table
     * The EventHandler sets forEach user.isSelected: true when the selectAllCheckBox is checked
     * else false
     * @return selectAllCheckBox (CheckBox)
     */
    public CheckBox getSelectAllCheckBox() {
        if (selectAllCheckBox == null) {
            final CheckBox selectAllCheckBox = CheckBoxBuilder.create().build();

            // Adding EventHandler to the CheckBox to select/deselect all users in table.
            selectAllCheckBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Setting the value in all the users
                    usersTableView.getItems().forEach((user) -> {
                        user.setIsSelected(selectAllCheckBox.isSelected());
                    });
                }
            });

            this.selectAllCheckBox = selectAllCheckBox;
        }
        return selectAllCheckBox;
    }

    public void setSelectAllCheckBox() {
        // An additional coloumn for isSelected Checkbox
        isSelectedCol.setGraphic(getSelectAllCheckBox());

        // Set css style
        isSelectedCol.getStyleClass().add("isSelected");

        isSelectedCol.setCellValueFactory(cellData -> cellData.getValue().isSelectedProperty());
        isSelectedCol.setCellFactory((TableColumn<User, Boolean> p) -> {
            final TableCell<User, Boolean> cell = new TableCell<User, Boolean>() {
                @Override
                public void updateItem(final Boolean item, boolean empty) {
                    if (item == null)
                        return;
                    super.updateItem(item, empty);
                    if (!isEmpty()) {
                        final User user = getTableView().getItems().get(getIndex());
                        CheckBox checkBox = new CheckBox();
                        checkBox.selectedProperty().bindBidirectional(user.isSelectedProperty());
                        setGraphic(checkBox);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER_LEFT);
            return cell;
        });
    }

    /**
     * User honorific title
     */
    public enum Title {
        Dr("Dr."), Mr("Mr."), Ms("Ms."), Miss("Miss"), Prof("Prof."), Mx("Mx."), Other("Other");

        private final String title;
        private Title code;

        Title(String title) {
            this.title = title;
        }

        public String getEnumTitle() {
            return title;
        }
    }

    /**
     * Returns user's honorific title
     * @param name (String) Title name to match to
     * @return title (Title)
     */
    public Title getTitle(String name) {
        Title title = null;

        for (Title t : Title.values()) {
            if (t.getEnumTitle().equals(name)) {
                title = t;
                break;
            }
        }

        return title;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // onEnter in searchField
        searchField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                searchAction();
            }
        });

        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        concatNameCol.setCellValueFactory(cellData -> cellData.getValue().otherNameProperty());
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        roleCol.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        jobCol.setCellValueFactory(cellData -> cellData.getValue().jobProperty());

        isActiveCol.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        isActiveCol.setCellFactory(CheckBoxTableCell.forTableColumn(isActiveCol));

        dateCreatedCol.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        lastLoginCol.setCellValueFactory(cellData -> cellData.getValue().lastLoginProperty());

        // An additional coloumn for isSelected Checkbox
        setSelectAllCheckBox();

        ListBinding<Boolean> lb = new ListBinding<Boolean>() {
            {
                bind(usersTableView.getItems());
            }

            @Override
            protected ObservableList<Boolean> computeValue() {
                ObservableList<Boolean> list = FXCollections.observableArrayList();
                usersTableView.getItems().forEach((p) -> {
                    list.add(p.getIsSelected());
                });

                return list;
            }
        };

        lb.addListener((ObservableValue<? extends ObservableList<Boolean>> arg0, ObservableList<Boolean> arg1,
                ObservableList<Boolean> l) -> {
            selectAllCheckBox.indeterminateProperty().set(true);
            selectAllCheckBox.setAllowIndeterminate(false);

            // Checking for an unselected user in the table view.      
            boolean unSelectedFlag = false;
            for (boolean b : l) {
                if (!b) {
                    unSelectedFlag = true;
                    break;
                }
            }

            /*
            * If at least one user is not selected, then deselecting the check box in the table column header, 
            * else if all users are selected, then selecting the check box in the header.
            */
            if (unSelectedFlag) {
                getSelectAllCheckBox().setSelected(false);
            } else {
                getSelectAllCheckBox().setSelected(true);
            }

            // Checking for a selected user in the table view.
            boolean selectedFlag = false;
            for (boolean b : l) {
                if (!b) {
                    selectedFlag = true;
                    break;
                }
            }

            /**
             * If at least one user is selected, then display the "Delete" button,
             * else if none of the users are selected,
             * then hide the "Delete" button.
             */
            if (selectedFlag) {
                delButton.setVisible(true);
            } else {
                delButton.setVisible(false);

            }
        });

        usersTableView.getItems().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                System.out.println("invalidated");
            }
        });

        usersTableView.getItems().addListener(new ListChangeListener<User>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends User> arg0) {
                System.out.println("changed");
            }
        });

        // Detect double click
        usersTableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    User rowData = row.getItem();

                    userDashboardPane.setEffect(new GaussianBlur());
                    EditUserController euc = new EditUserController(rowData.getUsername());
                    euc.editUserLoader(db);

                    // CreateUserForm.cancelButton action
                    euc.cancelButton.setOnAction(cancelEvent -> {
                        populateUsers(getUsers());
                        euc.stage.close();
                        userDashboardPane.setEffect(null);
                    });
                }
            });
            return row;
        });

        // Allow for selection of multiple rows
        // usersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // populate table
        try {
            populateUsers(getUsers());
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }

        // Load CreateUserForm.fxml
        addButton.setOnAction(e -> {
            userDashboardPane.setEffect(new GaussianBlur());

            CreateUserController cuc = new CreateUserController();
            cuc.createUserLoader(db);

            // CreateUserForm.cancelButton action
            cuc.cancelButton.setOnAction(event -> {
                populateUsers(getUsers());
                cuc.stage.close();
                userDashboardPane.setEffect(null);
            });
        });
    }
}
