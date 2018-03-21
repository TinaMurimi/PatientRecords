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

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == yesButton;
    }
    
    /**
     * Displays a dialogue box for confirmation of delete action
     * @return (Boolean) 
     */
    @Override
    public void deleteWarning() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Warning");
        alert.setHeaderText(null);
        alert.setContentText("No items selected");
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }

    @Override
    public void updateAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void viewAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void searchAction() {
        // TODO
    }
    
    /**
     * Returns a list of all the documents in the collection provided
     * @param collection (MongoCollection) Collection to query
     * @return itemData (FindIterable<Document>)
     */
    public FindIterable<Document> listAllItems(MongoCollection collection){
        Document query = new Document();
        FindIterable<Document> result = collection.find(query);

        return result;
    }
    
    @Override
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
     * Parses MongoDB Date to LocalDateTime
     * @param date (String) string to be converted to LocalDateTime
     * @return (LocalDateTime) parsed local date time
     */
    public LocalDateTime stringToLDT(String date) {
        date = date.trim();
        final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // The parsed date
        final LocalDateTime parsed = LocalDateTime.parse(date, inputFormat);
        return parsed;
    }


    /**
     * @param  parsedDate (LocalDateTime) to format
     * @return (String) parsed local date time
     */
    public String LDTToString(LocalDateTime parsedDate) {
        // new Date("<YYYY-mm-ddTHH:MM:ss>")
        // Convert LocalDateTime to String
        final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(parsedDate);
    }

    /**
     * Honorific titles
     */
    public enum Title {
        Dr("Dr."), Mr("Mr."), Ms("Ms."), Miss("Miss"), Prof("Prof."), Mx("Mx."), Other("Other");

        private final String title;
        private Title code;

        Title(String title) {
            this.title = title;
        }

        public String getEnumTitle() {
            return title;
        }
    }

    /**
     * Returns user's honorific title
     * @param name (String) Title name to match to
     * @return title (Title)
     */
    public Title getTitle(String name) {
        Title title = null;

        for (Title t : Title.values()) {
            if (t.getEnumTitle().equals(name)) {
                title = t;
                break;
            }
        }

        return title;
    }

}
