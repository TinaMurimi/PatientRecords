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

public class Patient extends Person {
    // private final BooleanProperty isSelected;
    
    private final SimpleObjectProperty<LocalDateTime> DoB;
    private final StringProperty gender;
    
    private final StringProperty bloodGroup;
    private final StringProperty rh;

    private final ListProperty<String> allergies;
    private final ListProperty<String> chronicDiseases;
    private final ListProperty<String> vaccinations; // Vaccination/ Immunization name, valid until

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

        this.DoB = new SimpleObjectProperty<>(); // if age < 18, guardian/parent
        this.gender = new SimpleStringProperty();

        this.bloodGroup = new SimpleStringProperty();
        this.rh = new SimpleStringProperty();

        this.allergies = new SimpleListProperty<>();
        this.chronicDiseases = new SimpleListProperty<>();
        this.vaccinations = new SimpleListProperty<>();
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
    public void setAllergies(ObservableList<String> allergies) {
        this.allergies.set(allergies);
    }

    public ObservableList<String> getAllergies() {
        return allergies.get();
    }

    public ListProperty<String> allergiesProperty() {
        return allergies;
    }

    // the chronicDiseases
    public void setChronic(ObservableList<String> chronicDiseases) {
        this.chronicDiseases.set(chronicDiseases);
    }

    public ObservableList<String> getChronic() {
        return chronicDiseases.get();
    }

    public ListProperty<String> chronicProperty() {
        return chronicDiseases;
    }

    // the vaccinations
    public void setVaccine(ObservableList<String> vaccinations) {
        this.vaccinations.set(vaccinations);
    }

    public ObservableList<String> getVaccine() {
        return vaccinations.get();
    }

    public ListProperty<String> vaccineProperty() {
        return vaccinations;
    }
}
