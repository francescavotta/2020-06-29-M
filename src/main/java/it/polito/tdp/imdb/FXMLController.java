/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
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

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		txtResult.appendText("Scegliere un anno dalla tendina!");
    		return;
    	}else {
    		txtResult.appendText(model.creaGrafo(anno));
    		this.boxRegista.getItems().addAll(model.getVertici());
    	}

    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	
    	Director d = this.boxRegista.getValue();
    	if(d==null) {
    		txtResult.appendText("Scegliere un anno dalla tendina!");
    		return;
    	}else {
    		List <Arco> archi = model.getAdiacenti(d);
    		for(Arco a: archi) {
    			txtResult.appendText("vicino: " + a.getD2() + " peso " + a.getPeso() + "\n");
    		}
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	txtResult.clear();
    	Director d = this.boxRegista.getValue();
    	if(d==null) {
    		txtResult.appendText("Scegliere un anno dalla tendina!");
    		return;
    	}
    	int c;
    	try {
    		c = Integer.parseInt(txtAttoriCondivisi.getText());
    		List<Director> director = model.cercaCammino(d, c);
    		String stampa = "";
    		for(Director dd: director) {
    			stampa += dd.toString() +" \n";
    		}
    		txtResult.appendText(stampa);
    		txtResult.appendText("Il numero di attori è " + model.pesoMigliore());
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Inserire numero valido!");
    		return;
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	List<Integer> anni = new ArrayList<Integer>();
    	anni.add(2004);
    	anni.add(2005);
    	anni.add(2006);
    	this.boxAnno.getItems().addAll(anni);
    	
    }
    
}
