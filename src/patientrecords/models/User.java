package patientrecords.models;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.CheckBox;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.binding.Bindings;

public class User {

    private static final HashMap<String, User> USERS = new HashMap<String, User>();
    // private CheckBox isSelected;
    private final BooleanProperty isSelected;
    private final BooleanProperty passwordValid;

    private final StringProperty userID;
    private final StringProperty username;
    private final StringProperty title;
    private final StringProperty role;
    private final StringProperty lastName;
    private final StringProperty otherName;

    private final StringProperty jobTitle;
    private final BooleanProperty isActive;
    private final StringProperty password;
    // private final StringProperty dateCreated;
    private final SimpleObjectProperty<LocalDateTime> dateCreated;
    // private StringProperty lastLoginDate;
    private final SimpleObjectProperty<LocalDateTime> lastLoginDate;

    LocalDateTime currDateTime = LocalDateTime.now();
    // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    // private StringProperty created = dateFormat.format(currDate);

    public static User of(String username) {
        User user = USERS.get(username);
        System.out.println("USERS.get(username): " + USERS.get(username));
        if (user == null) {
            System.out.println("user == null\n");
            user = new User();
            USERS.put(username, user);
        } else {
            System.out.println("Username already exists");
        }
        return user;
    }

    public User() {
        //     this.isSelected = new CheckBox();
        this.isSelected = new SimpleBooleanProperty();
        this.passwordValid= new SimpleBooleanProperty();

        this.userID = new SimpleStringProperty();
        this.username = new SimpleStringProperty();

        // this.dateCreated = new SimpleStringProperty((String) dateFormat.format(currDate));
        this.dateCreated = new SimpleObjectProperty<>(currDateTime);
        this.title = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.otherName = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.jobTitle = new SimpleStringProperty();
        this.isActive = new SimpleBooleanProperty();
        this.password = new SimpleStringProperty();
        // this.lastLoginDate = new SimpleStringProperty();
        this.lastLoginDate = new SimpleObjectProperty<>();
    }

    // select
    // public CheckBox getIsSelected() {
    //     return isSelected;
    // }

    public Boolean getIsSelected() {
        return isSelected.get();
    }

    // public void setIsSelected(CheckBox isSelected) {
    //this.isSelected = isSelected;
    //}
    public void setIsSelected(Boolean isSelected) {
        this.isSelected.set(isSelected);
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    // @return the userID
    public void setUserID(String usesrID) {
        this.userID.set(usesrID);
    }

    public String getUserID() {
        return userID.get();
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    // the username
    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // @return the dateCreated
    /**
    public String getDateCreated() {
        return dateCreated.get();
    }
    
    public StringProperty DateCreatedroperty() {
        return dateCreated;
    }
    */

    /** @return the dateCreated */
    public Object getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(LocalDateTime lastLoginDate) {
        this.lastLoginDate.set(lastLoginDate);
    }

    public SimpleObjectProperty<LocalDateTime> dateCreatedProperty() {
        return dateCreated;
    }

    // title
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        if (title != null) {
            title = StringUtils.capitalize(title.toLowerCase().trim());
        }
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // lastName
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            lastName = StringUtils.capitalize(lastName.toLowerCase().trim());
        }
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    // otherName
    public String getOtherName() {
        return otherName.get();
    }

    public void setOtherName(String otherName) {
        if (otherName != null) {
            otherName = StringUtils.capitalize(otherName.toLowerCase().trim());
        }
        this.otherName.set(otherName);
    }

    public StringProperty otherNameProperty() {
        return otherName;
    }

   /**
    * @return role DB role
    */ 
    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public StringProperty roleProperty() {
        return role;
    }

    // job
    public String getJob() {
        return jobTitle.get();
    }

    public void setJob(String jobTitle) {
        if (jobTitle != null) {
            jobTitle = StringUtils.capitalize(jobTitle.toLowerCase().trim());
        }
        this.jobTitle.set(jobTitle);
    }

    public StringProperty jobProperty() {
        return jobTitle;
    }

    // @return if user isActive
    public Boolean getIsActive() {
        return isActive.get();
    }

    public void setIsActive(Boolean isActive) {
        this.isActive.set(isActive);
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    // @return the password
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
    
    public BooleanProperty validPwdProperty() {
        return passwordValid;
    }
    // @return the lastLoginDate
    /**
    public String getLastLogin() {
        return lastLoginDate.get();
    }
    
    public void setLastLogin(String lastLoginDate) {
        lastLoginDate = dateFormat.format(currDate);
        this.lastLoginDate.set(lastLoginDate);
    }
    
    public StringProperty lastLoginProperty() {
        return lastLoginDate;
    }
     * @return 
    */

    // lastLoginDate
    public Object getLastLogin() {
        return lastLoginDate.get();
    }

    public void setLastLogin(LocalDateTime lastLoginDate) {
        this.lastLoginDate.set(lastLoginDate);
    }

    public SimpleObjectProperty<LocalDateTime> lastLoginProperty() {
        return lastLoginDate;
    }

    public HashMap allUsers() {
        return USERS;
    }

    public User getUserDetails() {
        return USERS.get(username);
    }

}
