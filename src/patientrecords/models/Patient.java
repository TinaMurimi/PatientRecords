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

public class Patient extends Person {
    // private final BooleanProperty isSelected;

    private final StringProperty fileNo;
    
    private final SimpleObjectProperty<LocalDateTime> DoB;
    private final StringProperty gender;
    
    private final StringProperty bloodGroup;
    private final StringProperty rh;

    private final ListProperty<Document> allergies;
    private final ListProperty<Document> chronicDiseases;
    private final ListProperty<Document> vaccinations; // Vaccination/ Immunization name, valid until

    // Insurance details
    // NHIF Number


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
        super();
        // this.isSelected = new SimpleBooleanProperty();

        
        this.fileNo = new SimpleStringProperty();
        
        this.DoB = new SimpleObjectProperty<>(); // if age < 18, guardian/parent
        this.gender = new SimpleStringProperty();

        this.bloodGroup = new SimpleStringProperty();
        this.rh = new SimpleStringProperty();

        this.allergies = new SimpleListProperty<>();
        this.chronicDiseases = new SimpleListProperty<>();
        this.vaccinations = new SimpleListProperty<>();
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

    // gender
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
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
}
