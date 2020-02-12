package fxMmfinaalit;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;


public class StartingWindowController {

	    /*
	     * Avaa tietokanta napin painalluksen tapahtuma
	     */
	
	    @FXML private void handleStartingWindow() {
			Dialogs.showMessageDialog("Tietokannan avaamista ei pystytty suorittamaan.");
		}
	    
	   
}

