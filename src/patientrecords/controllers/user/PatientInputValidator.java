package patientrecords.controllers.user;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.mongodb.client.MongoDatabase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.PrefixSelectionComboBox;
import patientrecords.controllers.patient.PatientDashboardController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

/** Validates the input for patient details then persists
 * new patient details to DB
 *
 */

public class PatientInputValidator extends PatientDashboardController {

    @FXML
    public VBox createPatientPane;

    @FXML
    public TabPane newPatientTabPane;

    @FXML
    public Tab demographicsTab;

    @FXML
    public Tab contactsTab;

    @FXML
    public Tab nhifTab;

    @FXML
    public Tab insuranceTab;

    @FXML
    public Tab addInfoTab;

    @FXML
    public AnchorPane claimantPane;

    @FXML
    public AnchorPane nhifPane;

    @FXML
    public TextField idField;

    @FXML
    public TextField memStateField;

    @FXML
    public CheckBox isPatientAddrCheckBox;

    @FXML
    public CheckBox hasNHIFCheckBox;

    @FXML
    public TextField memAddrLn2Field;

    @FXML
    public JFXRadioButton empInsurRadio;

    @FXML
    public ToggleGroup insurRltnship;

    @FXML
    public ToggleGroup nhifRltnship;

    @FXML
    public TextField memLastNameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField otherProviderField;

    @FXML
    public TextField memIDField;

    @FXML
    public TextField nhifMemIDField;

    @FXML
    public TextField givenNameField;

    @FXML
    public JFXRadioButton childRadio;

    @FXML
    public TextField memAddrLn1Field;

    @FXML
    public TextField addrln1Field;

    @FXML
    public TextField addrln2Field;

    @FXML
    public JFXRadioButton spouseInsurRadio;

    @FXML
    public TextField memPhoneField;

    @FXML
    public PrefixSelectionComboBox countryComboBox;

    @FXML
    public ComboBox genderComboBox;

    @FXML
    public TextField altPhoneField;

    @FXML
    public TextField memZipCodeField;

    @FXML
    public TextField zipCodeField;

    @FXML
    public TextField memNhifNoField;

    @FXML
    public TextField stateField;

    @FXML
    public TextField cityField;

    @FXML
    public PrefixSelectionComboBox<?> insurProviderComboBox;

    @FXML
    public TextField schemeField;

    @FXML
    public TextField pNameField;

    @FXML
    public JFXRadioButton priMemRadio;

    @FXML
    public TextField memInsurNoField;

    @FXML
    public PrefixSelectionComboBox memCountryComboBox;

    @FXML
    public JFXRadioButton spouseRadio;

    @FXML
    public ComboBox idTypeComboBox;

    @FXML
    public JFXRadioButton childInsurRadio;

    @FXML
    public TextField phoneField;

    @FXML
    public TextField memEmailField;

    @FXML
    public TextField pNoField;

    @FXML
    public TextField memGivenNameField;

    @FXML
    public DatePicker dobDatePicker;

    @FXML
    public TextField memCityField;

    @FXML
    public Label errorMsgLabel;

    @FXML
    public JFXButton saveButton;

    public final String dateFormat = "dd-MM-yyyy";



    // If any input field is not valid variable validDemographicsInfo is set to false
    // public boolean validDemographicsInfo = true;

    public HashMap<String, HashMap<String, HashMap<String, Object>>> validInput = new HashMap<>();

    // public BooleanProperty validDemographicsInfo = new SimpleBooleanProperty(false);
    // public BooleanProperty validContactInfo = new SimpleBooleanProperty(false);
    // public BooleanProperty validInsuranceInfo = new SimpleBooleanProperty(false);

    private final Pattern zipPattern = Pattern.compile("[0-9]{5}");

     public PatientInputValidator(MongoDatabase db){
         // this.patient = new Patient();
         super(db);
         this.validInput.put("valid", new HashMap<>());
         this.validInput.get("valid").put("validDemographicsInfo", new HashMap<>());
         this.validInput.get("valid").get("validDemographicsInfo").put("Last Name", false);
         this.validInput.get("valid").get("validDemographicsInfo").put("Given Namw", false);
         this.validInput.get("valid").get("validDemographicsInfo").put("Date of Birth", false);
         this.validInput.get("valid").get("validDemographicsInfo").put("Gender", false);

         this.validInput.get("valid").put("validContactInfo", new HashMap<>());
         this.validInput.get("valid").get("validContactInfo").put("Zip Code", true);
         this.validInput.get("valid").get("validContactInfo").put("Email", true);

         this.validInput.get("valid").put("validInsuranceInfo", new HashMap<>());
         this.validInput.get("valid").get("validInsuranceInfo").put("Zip Code", true);
         this.validInput.get("valid").get("validInsuranceInfo").put("Email", true);
     }

    /**
     * FocusListener to validate lastname is not null
     */
    public final  ChangeListener<Boolean> nullLNameListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            // textField.textProperty().removeListener(this);

            if (newValue) {
                if (lastNameField.getText().matches("^[A-Za-z]{3,10}(((\\'|\\-|\\.)?([A-Za-z])+))?$")) {
                    lastNameField.setStyle(null);
                } else {
                    lastNameField.setStyle("-fx-border-color: RED;");
                }
            }
        }
    };

    /**
     * ChangeListener to validate lastname
     */
    public final  ChangeListener<String> lastNameListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            // textField.textProperty().removeListener(this);

            /** Validate lastName
             * Mandatory single name, WITHOUT spaces, WITH special characters:
             * lastName should not contain a number
             * lastName should be longer than 2 characters
             */
            final String lname = lastNameField.getText();
            if (lname.matches("^[A-Za-z]{3,10}(((\\'|\\-|\\.)?([A-Za-z])+))?$")) {
                // patient.setLastName(lname);
                // lastNameField.getStyleClass().clear();
                lastNameField.setStyle(null);
                validInput.get("valid").get("validDemographicsInfo").put("lastname", true);

                // Set patient name in the Insurance Info tab
                pNameField.setText(givenNameField.getText().trim() + " " + newValue.trim());
            } else {
                // errorMsgLabel.setText("Invalid last name");
                lastNameField.setStyle("-fx-border-color: RED;");
                validInput.get("valid").get("validDemographicsInfo").put("lastname", false);

            }
        }
    };

    /**
     * FocusListener to validate given name is not null
     */
    public final  ChangeListener<Boolean> nullONameListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            // textField.textProperty().removeListener(this);

            if (newValue) {
                if (givenNameField.getText().matches("^[\\p{L} .'-]{3,30}$")) {
                    givenNameField.setStyle(null);
                } else {
                    givenNameField.setStyle("-fx-border-color: RED;");
                }
            }
        }
    };

    /**
     * ChangeListener to validate given name
     */
    public final  ChangeListener<String> givenNameListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            // textField.textProperty().removeListener(this);

            /** Validate givenName
             * Mandatory single name, optional additional names, WITH spaces, WITH special characters:
             * givenName should not contain a number
             * givenName should be longer than 2 characters
             * givenName can contain special characters
             */
            final String oname = givenNameField.getText();
            if (oname.matches("^[\\p{L} .'-]{3,30}$")) {
                givenNameField.setStyle(null);
                validInput.get("valid").get("validDemographicsInfo").put("givename", true);


                // Set patient name in the Insurance Info tab
                pNameField.setText(newValue.trim() + " " + lastNameField.getText().trim());
            } else {
                // errorMsgLabel.setText("Invalid given name (s)");
                givenNameField.setStyle("-fx-border-color: RED;");
                validInput.get("valid").get("validDemographicsInfo").put("givename", false);

            }
        }
    };

    /**
     * ChangeListener to validate Gender
     */
    public final  ChangeListener<Boolean> genderListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                if (genderComboBox.getValue() == null) {
                    genderComboBox.setStyle("-fx-border-color: RED;");
                    validInput.get("valid").get("validDemographicsInfo").put("gender", false);
                } else {
                    genderComboBox.setStyle(null);
                    validInput.get("valid").get("validDemographicsInfo").put("gender", true);
                    // gender = genderComboBox.getValue().toString();
                    // patient.setGender(gender);
                }
            }
        }
    };


    /**
     * Disable days on the DatePicker that are after current date
     */
    public final Callback<DatePicker, DateCell> dayCellFactory =
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item.isAfter(LocalDate.now())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    };
                }
            };

    /**
     * Convert date in DatePicker to dd-mm-yyyy
     */
    public final StringConverter converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(dateFormat);
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    /**
     * Validate date is not after today
     */
    public final  ChangeListener<LocalDate> dateListener = new ChangeListener<LocalDate>() {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

            /** Validate dob
             * Date of birth can only be <= current date
             */
            if (newValue == null) {
                dobDatePicker.setStyle("-fx-border-color: RED;");
                validInput.get("valid").get("validDemographicsInfo").put("dob", false);
            }
            else {
                if (newValue.isAfter(LocalDate.now())) {
                    // errorMsgLabel.setText("Invalid given name (s)");
                    dobDatePicker.setStyle("-fx-border-color: RED;");
                    validInput.get("valid").get("validDemographicsInfo").put("dob", false);
                } else {
                    // patient.setLastName(lname);
                    // lastNameField.getStyleClass().clear();
                    dobDatePicker.setStyle(null);
                    validInput.get("valid").get("validDemographicsInfo").put("dob", true);
                }
            }
        }
    };

    /**
     * FocusListener to validate date of birth is not null
     */
    public final  ChangeListener<Boolean> nulldobListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                if (dobDatePicker.getValue() == null) {
                    dobDatePicker.setStyle("-fx-border-color: RED;");
                } else {
                    if (dobDatePicker.getValue().isAfter(LocalDate.now())) {
                        dobDatePicker.setStyle("-fx-border-color: RED;");
                    } else {
                        dobDatePicker.setStyle(null);
                    }
                }
            }
        }
    };

    /**
     * ChangeListener to validate email
     */
    public final  ChangeListener<String> emailListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            final String email = emailField.getText();
             if (!newValue.isEmpty()) {
                 if (EmailValidator.getInstance().isValid(newValue)) {
                     emailField.setStyle(null);
                     validInput.get("valid").get("validContactInfo").put("email", true);
                 } else {
                     emailField.setStyle("-fx-border-color: RED;");
                     validInput.get("valid").get("validContactInfo").put("email", false);
                 }
             } else {
                 emailField.setStyle(null);
                 validInput.get("valid").get("validContactInfo").put("email", true);
             }
        }
    };

    /**
     * Validate zipcode input
     */
    public final  ChangeListener<String> zipcodeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

            if (!newValue.isEmpty()) {
                if (zipPattern.matcher(newValue).matches()) {
                    zipCodeField.setStyle(null);
                    validInput.get("valid").get("validContactInfo").put("zipcode", true);
                } else {
                    // errorMsgLabel.setText("Invalid given name (s)");
                    zipCodeField.setStyle("-fx-border-color: RED;");
                    // validDemographicsInfo = false;
                    validInput.get("valid").get("validContactInfo").put("zipcode", false);
                }
            }  else {
                zipCodeField.setStyle(null);
                validInput.get("valid").get("validContactInfo").put("zipcode", true);
            }
        }
    };

    /**
     * ChangeListener to validate email
     */
    public final  ChangeListener<String> memEmailListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.isEmpty()){
                if (EmailValidator.getInstance().isValid(newValue)) {
                    memEmailField.setStyle(null);
                    validInput.get("valid").get("validInsuranceInfo").put("email", true);
                } else {
                    memEmailField.setStyle("-fx-border-color: RED;");
                    validInput.get("valid").get("validInsuranceInfo").put("email", false);
                }
            } else {
                memEmailField.setStyle(null);
                validInput.get("valid").get("validInsuranceInfo").put("email", true);
            }
        }
    };

    /**
     * Validate memZipCode input
     */
    public final  ChangeListener<String> memZipCodeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!newValue.isEmpty()) {
                if (zipPattern.matcher(newValue).matches()) {
                    memZipCodeField.setStyle(null);
                    validInput.get("valid").get("validInsuranceInfo").put("zipcode", true);
                } else {
                    // errorMsgLabel.setText("Invalid given name (s)");
                    memZipCodeField.setStyle("-fx-border-color: RED;");
                    validInput.get("valid").get("validInsuranceInfo").put("zipcode", false);
                }
            } else {
                memZipCodeField.setStyle(null);
                validInput.get("valid").get("validInsuranceInfo").put("zipcode", true);
            }
        }
    };
}




