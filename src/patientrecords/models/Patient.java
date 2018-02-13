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

    private final StringProperty lastName;
    private final StringProperty givenName;
    private final ListProperty<Document> address; // Address Line 1*, Address Line 2, City*, State, ZipCode, Country*

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


    // NextofKin, Parent, Doctor, Patient
    public Patient() {
        this.isSelected = new SimpleBooleanProperty();

        this._ID = new SimpleStringProperty(); // ObjectID from DB
        this.fileNo = new SimpleStringProperty();
        this.identification = new SimpleStringProperty();

        this.lastName = new SimpleStringProperty();
        this.givenName = new SimpleStringProperty();
        this.address = new SimpleListProperty<>();
        this.DoB = new SimpleObjectProperty<>(); // if age < 18, guardian/parent

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
    public void setAddress(ObservableList<Document> address) {
        this.address.set(address);
    }

    public ObservableList<Document> getAddress() {
        return address.get();
    }

    public ListProperty<Document> addressProperty() {
        return address;
    }

    /** @return the DoB */
    public Object getDoB() {
        return DoB.get();
    }

    public void setDoB(LocalDateTime DoB) {
        this.DoB.set(DoB);
    }

    public SimpleObjectProperty<LocalDateTime> dobProperty() {
        return DoB;
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
