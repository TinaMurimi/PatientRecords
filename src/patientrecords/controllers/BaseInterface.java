/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patientrecords.controllers;

import javafx.event.ActionEvent;

public interface BaseInterface {

    public void addAction(ActionEvent event);

    public void deleteAction(ActionEvent event);

    public void editAction(ActionEvent event);

    public void viewAction(ActionEvent event);

    public void searchAction(ActionEvent event);

    // public void populateTableView();
}
