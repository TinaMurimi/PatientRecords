package patientrecords.models;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Claimant extends Person {

    // NHIF
    private final BooleanProperty hasNhif; // Is the patient an NHIF member or related to a member
    private final StringProperty memberIdNo;
    private final StringProperty nhifNo;
    private final StringProperty rltnNhif; // Relationship to Principal NHIF Member 

    // Insurance
    private final StringProperty insurProvider; // Insurance Provider
    private final StringProperty scheme; // Scheme Name/No
    private final StringProperty insurPatientNo; // Patient Member No
    private final StringProperty insurPrinNo; // Principal Member No
    private final StringProperty rltnInsur; // Relationship to Principal Insurance Member 

    public Claimant() {
        super();

        this.hasNhif= new SimpleBooleanProperty();
        this.memberIdNo = new SimpleStringProperty();
        this.nhifNo = new SimpleStringProperty();        
        this.rltnNhif = new SimpleStringProperty();

        this.insurProvider = new SimpleStringProperty();
        this.scheme = new SimpleStringProperty();
        this.insurPatientNo = new SimpleStringProperty();
        this.insurPrinNo = new SimpleStringProperty();
        this.rltnInsur = new SimpleStringProperty();
    }

    // @return hasNhif
    public Boolean getHasNhif() {
        return hasNhif.get();
    }

    public void setHasNhif(Boolean hasNhif) {
        this.hasNhif.set(hasNhif);
    }

    public BooleanProperty hasNhifProperty() {
        return hasNhif;
    }

    // @return the memberIdNo
    public void setIdNo(String memberIdNo) {
        this.memberIdNo.set(memberIdNo);
    }

    public String getIdNo() {
        return memberIdNo.get();
    }

    public StringProperty memberIdNoProperty() {
        return memberIdNo;
    }

    // @return the nhifNo
    public void setNhifNo(String nhifNo) {
        this.nhifNo.set(nhifNo);
    }

    public String getNhifNo() {
        return nhifNo.get();
    }

    public StringProperty nhifNoProperty() {
        return nhifNo;
    }

    // @return the rltnNhif
    public void setRltnNhif(String rltnNhif) {
        this.rltnNhif.set(rltnNhif);
    }

    public String getRltnNhif() {
        return rltnNhif.get();
    }

    public StringProperty rltnNhifProperty() {
        return rltnNhif;
    }

    // @return the insurProvider
    public void setInsurProvider(String insurProvider) {
        this.insurProvider.set(insurProvider);
    }

    public String getInsurProvider() {
        return insurProvider.get();
    }

    public StringProperty insurProviderProperty() {
        return insurProvider;
    }

    // @return the scheme
    public void setScheme(String scheme) {
        this.scheme.set(scheme);
    }

    public String getScheme() {
        return scheme.get();
    }

    public StringProperty schemeProperty() {
        return scheme;
    }

    // @return the insurPatientNo
    public void setInsurPatientNo(String insurPatientNo) {
        this.insurPatientNo.set(insurPatientNo);
    }

    public String getInsurPatientNo() {
        return insurPatientNo.get();
    }

    public StringProperty insurPatientNoProperty() {
        return insurPatientNo;
    }

    // @return the insurPrinNo
    public void setInsurPrinNo(String insurPrinNo) {
        this.insurPrinNo.set(insurPrinNo);
    }

    public String getInsurPrinNo() {
        return insurPrinNo.get();
    }

    public StringProperty insurPrinNoProperty() {
        return insurPrinNo;
    }

    // @return the rltnInsur
    public void setRltnInsur(String rltnInsur) {
        this.rltnInsur.set(rltnInsur);
    }

    public String getRltnInsur() {
        return rltnInsur.get();
    }

    public StringProperty rltnInsurProperty() {
        return rltnInsur;
    }

}
