/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if(cmbCitta.getValue()==null) {
    		txtResult.setText("inserire una città!");
    		return;
    	}
    	String city = cmbCitta.getValue();
    	txtResult.setText(this.model.creaGrafo(city));
    	this.cmbB1.getItems().clear();
    	this.cmbB1.getItems().addAll(this.model.getBusinesses());
    	this.cmbB2.getItems().clear();
    	this.cmbB2.getItems().addAll(this.model.getBusinesses());
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	if(cmbCitta.getValue()==null) {
    		txtResult.setText("inserire una città!");
    		return;
    	}
    	if(cmbB1.getValue()==null) {
    		txtResult.setText("scegliere un business!");
    		return;
    	}
    	txtResult.setText(this.model.getLocaleDistanza(cmbB1.getValue()));
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	if(cmbCitta.getValue()==null) {
    		txtResult.setText("inserire una città!");
    		return;
    	}
    	if(cmbB1.getValue()==null || cmbB2.getValue()==null) {
    		txtResult.setText("scegliere business di inizio e fine percorso!");
    		return;
    	}
    	double n=0;
    	try {
			n = Double.parseDouble(txtX2.getText());
		} catch (NumberFormatException e) {
			txtResult.setText("inserire numero soglia stelle corretta!");
			return;
		}
    	
    	List<Business> lista =  this.model.cercaLista(cmbB1.getValue(), cmbB2.getValue(), n);
    	for(Business b : lista) {
    		txtResult.appendText("\n"+b.getBusinessName());
    	}
    	txtResult.appendText("\ntot km percorsi: "+this.model.getKM());
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().clear();
    	cmbCitta.getItems().addAll(this.model.getCity());
    }
}
