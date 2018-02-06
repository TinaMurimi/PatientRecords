/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patientrecords.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.apache.commons.lang3.StringUtils;

public class Role {

    private StringProperty roleID;
    private StringProperty code;
    private StringProperty name;

    public Role() {
        this.roleID = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.code = new SimpleStringProperty();
    }
    
    public Role(String code, String name) {
        code = StringUtils.capitalize(code.toLowerCase().trim());
        this.code = new SimpleStringProperty(code);
        
        name = name.toLowerCase().trim();
        this.name = new SimpleStringProperty(name);
    }

    // @return the roleID
    public void setRoleID(String usesrID) {
        this.roleID.set(usesrID);
    }

    public String getRoleID() {
        return roleID.get();
    }

    public StringProperty roleIDProperty() {
        return roleID;
    }

    // name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        if (name != null) {
            name = name.toLowerCase().trim();
        }
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // @return code
    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        if (code != null) {
            code = StringUtils.capitalize(code.toLowerCase().trim());
        }
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }
    
    @Override
    public String toString(){
        return this.code.get();
    }

}
