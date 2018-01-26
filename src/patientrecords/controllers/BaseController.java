/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patientrecords.controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import org.bson.Document;

public abstract class BaseController implements BaseControllerInterface {

    // Dashboard CSS file URL
    private final URL url = this.getClass().getResource("/patientrecords/styles/dashboard.css");

    //    Optional<String> emptyGender = Optional.empty();
    Optional<String> emptyGender = null;

    @Override
    public void addAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void deleteAction(ActionEvent event) {
        // TODO
    }

    /**
     * Displays a dialogue box for confirmation of delete action
     * @return (Boolean) 
     */
    @Override
    public Boolean deleteConfirmation() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete selected items?");

        ButtonType yesButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.initStyle(StageStyle.UTILITY);

        // Add css file
        //        String css = "-fx-border-color: #48aaad; -fx-font-family: Arial; -fx-text-fill: black; -fx-font-size: 13px; -fx-font-weight: bold;";
        //        // StageStyle.DECORATED, StageStyle.UNDECORATED, StageStyle.TRANSPARENT, StageStyle.UTILITY, or StageStyle.UNIFIED.
        //        alert.getDialogPane().setStyle(css);
        //        alert.getDialogPane().getScene().setFill(Color.GREEN); // Used for better visual representation of the bug

//        DialogPane dialogPane = alert.getDialogPane();
//        if (url != null) {
//            String css = url.toExternalForm();
//            dialogPane.getStylesheets().add(css);
//            dialogPane.getStyleClass().add("confirmDialog");
//        } else {
//            System.out.println("CSS URL not found!");
//        }

        // Stage stageDialogue = (Stage) dialog.getDialogPane().getScene().getWindow();
        // stage(stageDialogue, 366, 175);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == yesButton;
    }

    @Override
    public void editAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void viewAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void searchAction(ActionEvent event) {
        // TODO
    }
    
    /**
     * Returns a list of all the documents in the collection provided
     * @return itemData (FindIterable<Document>)
     */
    public FindIterable<Document> listAllItems(MongoCollection collection){
        Document query = new Document();
        FindIterable<Document> result = collection.find(query);

        return result;
    }
    
    public void undoAction(ActionEvent event){
        // TODO
    }

    
    /**
     * Parses MongoDB Date to LocalDateTime
     * @param date (String) Date from MongoDB to be parsed
     * @return (LocalDateTime) parsed local date time
     */
    public LocalDateTime mongoDateToLDT(String date) {
        if (date.startsWith("TS time:")) {
            date = date.substring(8);
        }

        if (date.contains("inc:1")) {
            date = date.replace("inc:1", "");
        }

        date = date.trim();
        final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");

        // The parsed date
        final LocalDateTime parsed = LocalDateTime.parse(date, inputFormat);
        return parsed;
    }

    /**
     * @param  parsedDate (LocalDateTime) to format
     * @return (String) parsed local date time
     */
    public String LDTToString(LocalDateTime parsedDate) {
        // Convert LocalDateTime to String
        final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(parsedDate);
    }

}
