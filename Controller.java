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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {


    dbInterface dbi;
    String entryA, entryB, entryASpecifier, entryBSpecifier;
    public Button submitButton;
    public Button refreshButton;
    public ComboBox<String> member1DropDown;
    public ComboBox<String> member2DropDown;
    public TextField memberString1;
    public TextField memberString2;
    public ScrollPane dbResults;
    public TextFlow textResults;




    @FXML
    public ObservableList<String> member1List = FXCollections.observableArrayList(
            "actor",
            "director",
            "writer",
            "year",
            "");
    @FXML
    public ObservableList<String> member2List = FXCollections.observableArrayList(
            "actor",
            "director",
            "writer",
            "year",
            "");
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbi = new dbInterface();
        member1DropDown.setItems(member1List);
        member2DropDown.setItems(member2List);


    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        if(event.getSource() == submitButton){
            //grab text field data
            entryA = member1DropDown.getValue();
            entryB = member2DropDown.getValue();
            entryASpecifier = memberString1.getText();
            entryBSpecifier = memberString2.getText();
            String str = dbi.query(entryA, entryASpecifier, entryB, entryBSpecifier);
            Text txt = new Text(str);
            textResults.getChildren().add(txt);
            textResults.getChildren().add(new Text(System.lineSeparator()));
        }
        else {
            //clear all text text field contents
        }
    }

    @FXML
    public void handleHighestRatedActorMovieButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HighestRatedActorMoviePopup.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Highest Rated Actor Movie");
        stage.setScene(new Scene(root1));
        stage.showAndWait();
        HighestRatedActorMovieController HRAMC = (HighestRatedActorMovieController) fxmlLoader.getController();
        entryA = HRAMC.actorName.getText();
        entryB = "HighestRatedActorMovie";
        entryASpecifier = HRAMC.begRange.getText();
        entryBSpecifier = HRAMC.endRange.getText();

        String str = dbi.query(entryA, entryASpecifier, entryB, entryBSpecifier);
        Text txt = new Text(str);
        Text titletxt = new Text("Movie \t Rating");
        textResults.getChildren().add(titletxt);
        textResults.getChildren().add(new Text(System.lineSeparator()));
        textResults.getChildren().add(txt);
        textResults.getChildren().add(new Text(System.lineSeparator()));

    }

    public void handleDegofSeperationButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DegreesofSeperationPopup.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Degrees of Seperation");
        stage.setScene(new Scene(root1));
        stage.showAndWait();
        DegreesofSeperationController DoS = (DegreesofSeperationController) fxmlLoader.getController();
        entryA = "actor";
        entryB = "DegreesofSeperation";
        entryASpecifier = DoS.firstActorName.getText();
        entryBSpecifier = DoS.secondActorName.getText();

        String str = dbi.query(entryA, entryASpecifier, entryB, entryBSpecifier);
        Text txt = new Text(str);
        textResults.getChildren().add(txt);
        textResults.getChildren().add(new Text(System.lineSeparator()));

    }
    public void handleShortestListfromYearRangeButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShortestListfromYearRangePopup.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Shortest Actor List given Range");
        stage.setScene(new Scene(root1));
        stage.showAndWait();
        ShortestListfromYearRangeController SLFYR = (ShortestListfromYearRangeController) fxmlLoader.getController();
        entryA = "year";
        entryB = "ShortestListfromYearRange";
        entryASpecifier = SLFYR.begYear.getText();
        entryBSpecifier = SLFYR.endYear.getText();

        String str = dbi.query(entryA, entryASpecifier, entryB, entryBSpecifier);
        Text txt = new Text(str);
        textResults.getChildren().add(txt);
        textResults.getChildren().add(new Text(System.lineSeparator()));

    }


    @FXML
    private void printResultsInScrollPane(String str){



    }





}

