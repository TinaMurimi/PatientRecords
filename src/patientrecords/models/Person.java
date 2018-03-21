package patientrecords.models;

import org.apache.commons.lang3.StringUtils;


import java.time.LocalDateTime;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.bson.Document;

public class Person {
    private final BooleanProperty isSelected;

    private final StringProperty _ID; // DB ObjectID
    private Document identification; // Field National ID/ Passport Number

    private final StringProperty lastName;
    private final StringProperty givenName;
        
    private final StringProperty phoneNo;
    private final StringProperty altPhoneNo; // Alternative Phone Number
    private final StringProperty email;
    // private final ListProperty<Document> address; // Address Line 1*, Address Line 2, City*, State, ZipCode, Country*
    private Document address;

    private final SimpleObjectProperty<LocalDateTime> dateCreated;

    public Person() {
        this.isSelected = new SimpleBooleanProperty();

        this._ID = new SimpleStringProperty(); // ObjectID from DB

        this.lastName = new SimpleStringProperty();
        this.givenName = new SimpleStringProperty();
        // this.address = new SimpleListProperty<>();
        // this.address = new Document();

        this.phoneNo = new SimpleStringProperty();
        this.altPhoneNo = new SimpleStringProperty();
        this.email = new SimpleStringProperty();

        this.dateCreated = new SimpleObjectProperty<>();
    }

    public Boolean getIsSelected() {
        return isSelected.get();
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected.set(isSelected);
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    // @return the _ID
    public void setID(String _ID) {
        this._ID.set(_ID);
    }

    public String getID() {
        return _ID.get();
    }

    public StringProperty IDProperty() {
        return _ID;
    }

    // @return the identification
    public void setIdentification(Document identification) {
        this.identification = identification;
    }

    public Document getIdentification() {
        return identification;
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

    // givenName
    public String getGivenName() {
        return givenName.get();
    }

    public void setGivenName(String givenName) {
        if (givenName != null) {
            givenName = StringUtils.capitalize(givenName.toLowerCase().trim());
        }
        this.givenName.set(givenName);
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    // the address
    public void setAddress(Document address) {
        this.address = address;
    }

    public Document getAddress() {
        return address;
    }

    /*public ListProperty<Document> addressProperty() {
        return address;
    }*/

    // the phoneNo
    public void setPhoneNo(String phoneNo) {
        this.phoneNo.set(phoneNo);
    }

    public String getPhoneNo() {
        return phoneNo.get();
    }

    public StringProperty phoneNoProperty() {
        return phoneNo;
    }

    // the altPhoneNo
    public void setAltPhoneNo(String altPhoneNo) {
        this.altPhoneNo.set(altPhoneNo);
    }

    public String getAltPhoneNo() {
        return altPhoneNo.get();
    }

    public StringProperty altPhoneNoProperty() {
        return altPhoneNo;
    }

    // the email
    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    /** @return the dateCreated */
    public Object getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public SimpleObjectProperty<LocalDateTime> dateCreatedProperty() {
        return dateCreated;
    }
}
