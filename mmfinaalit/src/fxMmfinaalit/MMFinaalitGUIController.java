package fxMmfinaalit;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * 
 * @author Ossi Lahti
 * @version Viimeisin muokkauspäivä: 12.2.2020
 */

public class MMFinaalitGUIController implements Initializable {
	
	
	@FXML private TextField searchCriteria;
	@FXML private Label labelError;
	private TextInputControl searchField;
	
	/**
	 * Alustetaan finaalitaulukon sarakkeet
	 */
	
    @FXML private TableView<Finaali> tableView;
    @FXML private TableColumn<Finaali,String> vuosiColumn;
    @FXML private TableColumn<Finaali,String> paikkaColumn;
    @FXML private TableColumn<Finaali,String> voittajaColumn;
    @FXML private TableColumn<Finaali,String> hopeajoukkueColumn;
    @FXML private TableColumn<Finaali,String> lopputulosColumn;
    @FXML private TableColumn<Finaali,String> katsojatColumn;
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // sarakkeiden muodostus taulukossa 
        vuosiColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("vuosi"));
        paikkaColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("paikka"));
        voittajaColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("voittaja"));
        hopeajoukkueColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("hopeajoukkue"));
        lopputulosColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("lopputulos"));
        katsojatColumn.setCellValueFactory(new PropertyValueFactory<Finaali, String>("katsojat"));
        
        // Tehdään metodi jonka avulla taulukkoon lisätään dataa
        tableView.setItems(getFinaalit());
    }  


    /**
     * Tietoja ikkunan painallus
     */
	@FXML private void handleAboutWindow() {
		Dialogs.showMessageDialog("Tietoikkunan tekeminen on vielä kesken!");
	}

	/**
	 * Finaalin lisäys metodi
	 */

	@FXML private void handleAddFinal() {
		Dialogs.showMessageDialog("Finaalin lisäämistä ei pystytty tekemään.");
	}
	
	/**
	 * Apua ikkunan painallus
	 */
	
	@FXML private void handleHelpWindow() {
	    helpWindow();
	}

	/**
	 * Tulosta napin painallus
	 */
	
	@FXML private void handlePrintWindow() {
		Dialogs.showMessageDialog("Tulostettavaa materiaalia ei vielä ole.");
	}
	
	/**
	 * Lopeta luokan metodi
	 */
	

    @FXML private void handleExit() {
        Platform.exit();
    } 
	
    /**
     * Arvo finaali napin metodi. 
     */
    
	@FXML private void handleRandomize() {
		Dialogs.showMessageDialog("Arpomista ei vielä pysty tehdä.");
	}
	
	/**
	 * Poista finaali metodin handleri
	 */
	
	
	@FXML private void handleRemoveFinal() {
		
		// Ideana suorittaa varmennus tähän, jossa kysytään halutaanko finaali varmasti poistaa.
		Dialogs.showMessageDialog("Finaalin poistamista ei pystytty suorittamaan.");
	}
	
	/**
	 * Hakuehtoon kun kirjoittaa, mitä tapahtuu:
	 */
	
	@FXML private void handleHakuehto() {
        String hakukentta = searchField.getText();
        if ( hakukentta.isEmpty() )
            naytaVirhe(null);
        else
            naytaVirhe("Ei osata vielä lukea tekstiä: " + hakukentta);
    }

	
	///=================================================================================
    
        private void naytaVirhe(String virhe) {
            if ( virhe == null || virhe.isEmpty() ) {
                labelError.setText("");
                labelError.getStyleClass().removeAll("Virhe");
                return;
            }
            labelError.setText(virhe);
            labelError.getStyleClass().add("Virhe");
        }
        

        /** Osa handleExit metodia
         * @return true jos saa sammua, false jos ei
         */
        public boolean shutdown() {
            return true;
        }
        

        /**
         * "Apua"-ikkuna uudessa välilehdessä.
         */
        
        private void helpWindow() {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://www.youtube.com/watch?v=oHg5SJYRHA0");
                desktop.browse(uri);
            } catch (URISyntaxException e) {
                return;
            } catch (IOException e) {
                return;
            }
        }

        /**
         * Dataa jota lisätään tietokantaan. Tavoitteena tulevaisuudessa tehdä siis useita luokkia tähän joiden avulla voi esim.
         * finaalipaikan lisätä omasta luokastaan.
         * @return lisätty data
         */
        
        private ObservableList<Finaali> getFinaalit() {
    		
        	ObservableList<Finaali> finaalit = FXCollections.observableArrayList();
            finaalit.add(new Finaali("2018","Venäjä", "Ranska", "Kroatia", "4-2", 78000));
            finaalit.add(new Finaali("2014","Brasilia", "Saksa", "Argentiina", "1-0", 74738));
            finaalit.add(new Finaali("2010","Etelä-Afrikka", "Espanja", "Hollanti", "1-0", 84490));
            finaalit.add(new Finaali("2006","Saksa", "Italia", "Ranska", "2-1", 69000));
            
            return finaalit;
    	}

	
}
