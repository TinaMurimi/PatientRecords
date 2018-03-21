package patientrecords.controllers.patient;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import patientrecords.controllers.user.PatientInputValidator;
import patientrecords.models.Claimant;
import patientrecords.models.Patient;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXRadioButton;

import javafx.collections.FXCollections;
import org.controlsfx.control.PrefixSelectionComboBox;


import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;


import javax.print.attribute.standard.DocumentName;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CreatePatient extends PatientInputValidator implements Initializable{

    public Stage stage;

    private final MongoDatabase db;
    private final MongoCollection collection;
    private final Logger logger;

    private Patient patient;
    private Claimant claimant;

    private final URL url;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    public CreatePatient (MongoDatabase db){
        super(db);
        this.db = db;
        this.collection = db.getCollection("Patients");
        this.logger =  Logger.getLogger(getClass().getName());
        this.url = this.getClass().getResource("/patientrecords/styles/newPatient.css");

        this.patient = new Patient();
        this.claimant = new Claimant();
    }

    public void createPatientLoader (){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/patientrecords/views/patient/CreatePatient.fxml"));

        loader.setController(this);

        try {
            createPatientPane = (VBox) loader.load();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Failed to load loader", exception);
        }

        try {
            // Scene scene = new Scene(createPatientPane, 765, 800, Color.TRANSPARENT);

            Scene scene = new Scene(createPatientPane, Color.TRANSPARENT);

            // Add css file
            if (url != null) {
                String css = url.toExternalForm();
                scene.getStylesheets().add(css);
            } else {
                logger.log(Level.WARNING, "CSS URL not found!");
            }

            // stage = new Stage(StageStyle.TRANSPARENT);
            stage = new Stage();
            stage.setTitle("Create New Patient");
            stage.setFullScreen(false);
            // stage.initOwner(primaryStage); // PatientDashBoardController.primaryStage: UserDashboeard stage
            stage.setScene(scene);
            stage.centerOnScreen();
            // stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    /**
     * Creates a new object of Patient
     */
    private void createPatient(){

        String errorText = "Fields listed below (or highlighted in RED) have invalid input"
                + "\nTo see the correct input format, hover over the field" +
                "\n   to display the Tooltip.";

        String demographicsErrorText = "\n\tDemographics (Basic) Info";
        String contactsErrorText = "\n\tContacts Info";
        String insuranceErrorText = "\n\tInsurance Info";

        List <String> invalidFields = new ArrayList<>();

        for (HashMap.Entry<String, HashMap<String, HashMap<String, Object>>> entry : validInput.entrySet()) {
            for (HashMap.Entry<String, HashMap<String, Object>> tabs : entry.getValue().entrySet()) {

                String tab = tabs.getKey();
                tab = tab.replace("valid", "");
                tab = StringUtils.capitalize(tab.toLowerCase().trim());
                tab = tab.replace("info", " Info");

                for (HashMap.Entry<String, Object> fields : tabs.getValue().entrySet()) {
                    String field = fields.getKey();
                    Boolean valid = Boolean.valueOf(fields.getValue().toString());

                    if (!valid) {
                        invalidFields.add(field);
                        if (tab.equals("Demographics Info")) {
                            demographicsErrorText = demographicsErrorText + "\n\t\t- " + field;
                        } else if (tab.equals("Insurance Info")) {
                            insuranceErrorText = insuranceErrorText + "\n\t\t- " + field;
                        } else if (tab.equals("Contact Info")) {
                            contactsErrorText = contactsErrorText + "\n\t\t- " + field;
                        }
                    }
                }
            }
        } // end for loop

        if (invalidFields.isEmpty()) {
            errorMsgLabel.setVisible(false);



            patient.setLastName(lastNameField.getText());
            patient.setGivenName(givenNameField.getText());
            patient.setGender(genderComboBox.getValue().toString());
            patient.setDoB(stringToLDT(dobDatePicker.getValue().toString()));

            new Document(idTypeComboBox.getValue().toString(), idField.getText());

            patient.setIdentification(new Document("identification",
                    new Document(idTypeComboBox.getValue().toString(), idField.getText())));


            String country = countryComboBox.getValue() == null ? null : countryComboBox.getValue().toString();
            patient.setAddress(new Document("address",
                    new Document("addrln1", addrln1Field.getText())
                            .append("addrln2", addrln2Field.getText())
                            .append("city", cityField.getText())
                            .append("state", stateField.getText())
                            .append("zipcode", zipCodeField.getText())
                            .append("country", country)
                    )
            );

            patient.setEmail(emailField.getText());
            patient.setPhoneNo(phoneField.getText());
            patient.setAltPhoneNo(altPhoneField.getText());


            // NHIF Info
            claimant.setIdNo(nhifMemIDField.getText());
            claimant.setNhifNo(memNhifNoField.getText());
            claimant.setRltnNhif(memNhifNoField.getText());

            if ( nhifRltnship.getSelectedToggle() != null ) {
                String rltnship = ((RadioButton) nhifRltnship.getSelectedToggle()).getText();
                claimant.setRltnNhif(rltnship);
            }

            // Insurance Info
            String insurer = insurProviderComboBox.getValue() == null ? null : insurProviderComboBox.getValue().toString();
            if (insurer == null) {
                insurer = otherProviderField.getText();
            }

            claimant.setInsurProvider(insurer);
            claimant.setScheme(schemeField.getText());
            claimant.setInsurPatientNo(schemeField.getText());

            if ( insurRltnship.getSelectedToggle() != null ) {
                String rltnship = ((RadioButton) insurRltnship.getSelectedToggle()).getText();
                claimant.setRltnInsur(rltnship);
            }


            claimant.setLastName(memLastNameField.getText());
            claimant.setGivenName(memGivenNameField.getText());
            claimant.setPrinIdNo(memIDField.getText());
            claimant.setInsurPrinNo(memInsurNoField.getText());

            String memCountry = memCountryComboBox.getValue() == null ? null : memCountryComboBox.getValue().toString();
            claimant.setAddress(new Document("address",
                            new Document("addrln1", addrln1Field.getText())
                                    .append("addrln2", addrln2Field.getText())
                                    .append("city", cityField.getText())
                                    .append("state", stateField.getText())
                                    .append("zipcode", zipCodeField.getText())
                                    .append("country", memCountry)
                    )
            );

            claimant.setEmail(memEmailField.getText());
            claimant.setPhoneNo(memPhoneField.getText());


            // Additional Information
            /*
            patient.setBloodGroup(bloodgroup);
            patient.setRH(rh);
            patient.setAllergies(allergies);
            patient.setChronic(chronic);
            patient.setVaccine(vaccinations);
            */

        } else {
            // Concatenate the error messages for each tab
            errorMsgLabel.setText(errorText + demographicsErrorText + contactsErrorText + insuranceErrorText);

            errorMsgLabel.setVisible(true);
            newPatientTabPane.requestFocus();
            newPatientTabPane.getSelectionModel().select(demographicsTab);
        }

    }


    /**
     * Persists new patient to the DB
     * @param patient
     */
    private void savePatient(Patient patient) {

        Document details = new Document("identification", patient.getIdentification())
                .append("lastname", patient.getLastName())
                .append("givenname", patient.getGivenName())
                .append("dob", patient.getDoB())
                // new Date("2016-05-18T16:00:00Z")
                .append("address", patient.getAddress())
                .append("phoneno", patient.getPhoneNo())
                .append("altphoneno", patient.getAltPhoneNo())
                .append("email", patient.getEmail())
                .append("gender", patient.getGender())
                .append("bloodgroup", patient.getBloodGroup())
                .append("rh", patient.getRH())
                .append("allergies", patient.getAllergies())
                .append("chronicdiseases", patient.getChronic())
                // .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("vaccinations", patient.getVaccine())
                .append("created", new Date());

        try {
            collection.insertOne(details);

        } catch (MongoCommandException e) {
            logger.log(Level.SEVERE, "Unable to insert new patient", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        newPatientTabPane.requestFocus();
        newPatientTabPane.getSelectionModel().select(demographicsTab);

        /**
         * Set tooltips and event/change listeners
        */

        // Last name
        lastNameField.requestFocus();
        final Tooltip lNameTooltip = new Tooltip();
        final String lastNameRules = "Last Name requirements:\n" + " 1. Last name is mandatory\n"
                + "  ... and should be/have:\n"
                + " 2. 3-10 characters without spaces\n"
                + " 3. allowed special characters: .-'\n"
                + " \tdot(.), hyphen(-), apostrophe(')\n";

        lNameTooltip.setText(lastNameRules);
        lastNameField.setTooltip(lNameTooltip);
        lastNameField.textProperty().addListener(lastNameListener);
        lastNameField.focusedProperty().addListener(nullLNameListener);


        // Last name
        final Tooltip oNameTooltip = new Tooltip();
        final String givenNameRules = "Given Name requirements:\n"
                + " 1. Other name is mandatory\n"
                + "  ... and should be/have:\n"
                + " 2. 3-30 characters \n"
                + " 3. allowed special characters: a-zA-Z\n"
                + " \tdot(.), hyphen(-), apostrophe('), space( )\n";

        oNameTooltip.setText(givenNameRules);
        givenNameField.setTooltip(oNameTooltip);
        givenNameField.textProperty().addListener(givenNameListener);
        givenNameField.focusedProperty().addListener(nullONameListener);

        // DoB
        final Tooltip dobTooltip = new Tooltip();
        final String dobRules = "Date of birth has to be less or equal than current date";
        dobTooltip.setText(dobRules);
        dobDatePicker.setTooltip(dobTooltip);
        dobDatePicker.setDayCellFactory(dayCellFactory);
        dobDatePicker.setConverter(converter);
        dobDatePicker.setPromptText(dateFormat.toLowerCase());
        dobDatePicker.valueProperty().addListener(dateListener);
        dobDatePicker.focusedProperty().addListener(nulldobListener);

        // Populate the genderComboBox ComboBox
        genderComboBox.setItems(genderOptions);
        genderComboBox.setValue(null);
        genderComboBox.focusedProperty().addListener(genderListener);


        // Populate the idTypeComboBox ComboBox
        idTypeComboBox.setItems(idTypeOptions);
        idTypeComboBox.setValue(null);

        // Zipcode
        final Tooltip zipcodeTooltip = new Tooltip();
        final String zipcodeRules = "5 (five) Values between 0-9 eg 00100";
        zipcodeTooltip.setText(zipcodeRules);
        zipCodeField.setTooltip(zipcodeTooltip);
        zipCodeField.textProperty().addListener(zipcodeListener);

        // Populate Country ComboBox
        countryComboBox.setItems(sortedCountryOptions);
        memCountryComboBox.setItems(sortedCountryOptions);

        // Email
        emailField.textProperty().addListener(emailListener);

        // Group NHIF Relationship Radio Buttons
        nhifRltnship = new ToggleGroup();
        priMemRadio.setToggleGroup(nhifRltnship);
        spouseRadio.setToggleGroup(nhifRltnship);
        childRadio.setToggleGroup(nhifRltnship);

        // Group Insurance Relationship Radio Buttons
        insurRltnship = new ToggleGroup();
        empInsurRadio.setToggleGroup(insurRltnship);
        spouseInsurRadio.setToggleGroup(insurRltnship);
        childInsurRadio.setToggleGroup(insurRltnship);

        // memEmail
        memEmailField.textProperty().addListener(memEmailListener);

        // memZipCode
        final Tooltip memZipCodeTooltip = new Tooltip();
        memZipCodeTooltip.setText(zipcodeRules);
        memZipCodeField.setTooltip(memZipCodeTooltip);
        memZipCodeField.textProperty().addListener(memZipCodeListener);

        /*
         * If a patient is the principal member, it is not necessary to input the member/claimant details
         * Set the principal member details to be equal to the patient details
         * and make the fields uneditable
        */
        insurRltnship.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
            {
                RadioButton choice = (RadioButton) newValue.getToggleGroup().getSelectedToggle(); // Cast object to radio button
                if (choice.getText().equals("Principal Member/Employee")){
                    claimantPane.setDisable(true);

                    memLastNameField.setText(lastNameField.getText());
                    memLastNameField.setEditable(false);

                    memGivenNameField.setText(givenNameField.getText());
                    memGivenNameField.setEditable(false);

                    memIDField.setText(idField.getText());
                    memIDField.setEditable(false);

                    memInsurNoField.setText(pNoField.getText());
                    memInsurNoField.setEditable(false);

                    isPatientAddrCheckBox.setSelected(true);
                } else {
                    claimantPane.setDisable(false);

                    memLastNameField.clear();
                    memLastNameField.setEditable(true);

                    memGivenNameField.clear();
                    memGivenNameField.setEditable(true);

                    memIDField.clear();
                    memIDField.setEditable(true);

                    memInsurNoField.clear();
                    memInsurNoField.setEditable(true);

                    isPatientAddrCheckBox.setSelected(false);
                }
            }
        });


        isPatientAddrCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    memAddrLn1Field.setText(addrln1Field.getText());
                    memAddrLn1Field.setEditable(false);

                    memAddrLn2Field.setText(addrln2Field.getText());
                    memAddrLn2Field.setEditable(false);

                    memCityField.setText(cityField.getText());
                    memCityField.setEditable(false);

                    memStateField.setText(stateField.getText());
                    memStateField.setEditable(false);

                    memZipCodeField.setText(zipCodeField.getText());
                    memZipCodeField.setEditable(false);

                    String country = countryComboBox.getValue() == null ? null : countryComboBox.getValue().toString();
                    memCountryComboBox.setValue(country);
                    memCountryComboBox.setDisable(true);

                    memEmailField.setText(emailField.getText());
                    memEmailField.setEditable(false);

                    memPhoneField.setText(phoneField.getText());
                    memPhoneField.setEditable(false);

                } else {
                    memAddrLn1Field.clear();
                    memAddrLn1Field.setEditable(true);

                    memAddrLn2Field.clear();
                    memAddrLn2Field.setEditable(true);

                    memCityField.clear();
                    memCityField.setEditable(true);

                    memStateField.clear();
                    memStateField.setEditable(true);

                    memZipCodeField.clear();
                    memZipCodeField.setEditable(true);

                    memCountryComboBox.setValue(null);
                    memCountryComboBox.setDisable(false);

                    memEmailField.clear();
                    memEmailField.setEditable(true);

                    memPhoneField.clear();
                    memPhoneField.setEditable(true);
                }
            }
        });

        /**
         * ChangeListener for hasNHIFCheckBox
         */
        hasNHIFCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    nhifMemIDField.setText(idField.getText());
                    nhifPane.setDisable(false);
                } else {
                    nhifMemIDField.clear();
                    nhifPane.setDisable(true);
                }
            }
        });


        newPatientTabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {

                        if (newTab == insuranceTab){
                            // Update the member's zipcode if using address similar to patient's address
                            if (isPatientAddrCheckBox.isSelected()) {
                                isPatientAddrCheckBox.setSelected(false);
                                isPatientAddrCheckBox.setSelected(true);
                            }

                            if ( insurRltnship.getSelectedToggle() != null ) {
                                if (((RadioButton) insurRltnship.getSelectedToggle()).getText().equals("Principal Member/Employee")) {
                                    spouseInsurRadio.setSelected(true);
                                    empInsurRadio.setSelected(true);
                                }
                            }
                        }
                    }
                }
        );

        pNoField.textProperty().addListener( new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue != null) {
                    memInsurNoField.setText(pNoField.getText());
                }
            }
        });

        saveButton.setOnAction(actionEvent ->  {
            createPatient();
        });
    }
}
