package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class HighestRatedActorMovieController implements Initializable {
    //dbInterface dbi;
    public Button submitButton;
    public TextField actorName;
    public TextField begRange;
    public TextField endRange;



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //dbi = new dbInterface();

    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        if (event.getSource() == submitButton) {
            //grab text field data
            String actorNameText = actorName.getText();
            String begRangeText = begRange.getText();
            String endRangeText = endRange.getText();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();




        }

    }

    public List<String> returnHighestRatedActorMovieData(String actorNameText, String begRangeText, String endRangeText){

        return Arrays.asList(actorNameText, begRangeText, endRangeText);
    }

}

