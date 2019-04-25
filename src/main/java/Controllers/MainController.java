package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    FavoritesController favoritesController;
    HomeController homeController;

    String currentDriver;

    @FXML
    private ObservableList<String> list = FXCollections.observableArrayList("9gag","kwejk","jeb z dzidy");
    @FXML
    private ComboBox<String> listOfDrivers = new ComboBox<>();
    @FXML
    private void comboChange(){
        //System.out.println(listOfDrivers.getValue());
    }
    @FXML
    private void saveDriver(){
        currentDriver = listOfDrivers.getValue();
        System.out.format("%s\n",currentDriver);

    }

    @Override
    public void initialize(URL location, ResourceBundle r){
        listOfDrivers.getItems().addAll(list);
    }

}