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

public class Patient {
    private final BooleanProperty isSelected;

    private final StringProperty _ID;
    private final StringProperty fileNo;
    private final StringProperty identification; // Field National ID/ Passprt Number

    private final StringProperty title;
    private final StringProperty lastName;
    private final StringProperty otherName;
    private final StringProperty address; // Address Line 1*, Address Line 2, City*, State, ZipCode, Country*

    private final SimpleObjectProperty<LocalDateTime> DoB;

    private final StringProperty phoneNo;
    private final StringProperty altPhoneNo; // Alternative Phone Number
    private final StringProperty email;

    private final StringProperty bloodGroup;
    private final StringProperty rh;

    private final ListProperty<Document> allergies;
    private final ListProperty<Document> chronicDiseases;
    private final ListProperty<Document> vaccinations; // Vaccination/ Immunization name, valid until

    private final SimpleObjectProperty<LocalDateTime> dateCreated;

    /**
    public Person(int id, List<Priority> list) {
        ...
        ObservableList<Priority> observableList = FXCollections.observableArrayList(list)
        this.choice = new SimpleListProperty<Priority>(observableList);
    }
    */

    /**
    private final ListProperty<MyClass> someList = ...;
    
    public ObservableList<MyClass> getSomeList() {
    return someList.get();
    }
    
    public void setSomeList(ObservableList<MyClass> newList) {
    someList.set(newList);
    }
    
    public ListProperty<MyClass> someListProperty() {
    return someList;
    }
     */


    public Patient() {
        this.isSelected = new SimpleBooleanProperty();

        this._ID = new SimpleStringProperty(); // ObjectID from DB
        this.fileNo = new SimpleStringProperty();
        this.identification = new SimpleStringProperty();

        this.title = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.otherName = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.DoB = new SimpleObjectProperty<>();

        this.phoneNo = new SimpleStringProperty();
        this.altPhoneNo = new SimpleStringProperty();
        this.email = new SimpleStringProperty();

        this.bloodGroup = new SimpleStringProperty();
        this.rh = new SimpleStringProperty();

        this.allergies = new SimpleListProperty<>();
        this.chronicDiseases = new SimpleListProperty<>();
        this.vaccinations = new SimpleListProperty<>();

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

    // @return the fileNo
    public void setFileNo(String fileNo) {
        this.fileNo.set(fileNo);
    }

    public String getFileNo() {
        return fileNo.get();
    }

    public StringProperty fileNoProperty() {
        return fileNo;
    }

    // @return the identification
    public void setIdentification(String identification) {
        this.identification.set(identification);
    }

    public String getIdentification() {
        return identification.get();
    }

    public StringProperty identificationProperty() {
        return identification;
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

    // the address
    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

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

    // the bloodGroup
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup.set(bloodGroup);
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public StringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    // the rh
    public void setRH(String rh) {
        this.rh.set(rh);
    }

    public String getRH() {
        return rh.get();
    }

    public StringProperty rhProperty() {
        return rh;
    }

    // the allergies
    public void setAllergies(ObservableList<Document> allergies) {
        this.allergies.set(allergies);
    }

    public ObservableList<Document> getAllergies() {
        return allergies.get();
    }

    public ListProperty<Document> allergiesProperty() {
        return allergies;
    }

    // the chronicDiseases
    public void setChronic(ObservableList<Document> chronicDiseases) {
        this.chronicDiseases.set(chronicDiseases);
    }

    public ObservableList<Document> getChronic() {
        return chronicDiseases.get();
    }

    public ListProperty<Document> chronicProperty() {
        return chronicDiseases;
    }

    // the vaccinations
    public void setVaccine(ObservableList<Document> vaccinations) {
        this.vaccinations.set(vaccinations);
    }

    public ObservableList<Document> getVaccine() {
        return vaccinations.get();
    }

    public ListProperty<Document> vaccineProperty() {
        return vaccinations;
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
