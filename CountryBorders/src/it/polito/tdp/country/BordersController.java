package it.polito.tdp.country;

import it.polito.tdp.country.model.Country;
import it.polito.tdp.country.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class BordersController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Country> boxStart;
    
    @FXML
    private ComboBox<Country> boxEnd;

    @FXML
    private Button btnCompute;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCompute(ActionEvent event) {

    }
    
    public void setModel(Model m) {
    	this.model = m ;
    	boxStart.getItems().addAll(model.getCountries()) ;
    	boxEnd.getItems().addAll(model.getCountries()) ;
    }

    @FXML
    void initialize() {
        assert boxStart != null : "fx:id=\"boxStart\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
        assert btnCompute != null : "fx:id=\"btnCompute\" was not injected: check your FXML file 'Borders.fxml'.";
        assert boxEnd != null : "fx:id=\"boxEnd\" was not injected: check your FXML file 'Borders.fxml'.";

    }
}

