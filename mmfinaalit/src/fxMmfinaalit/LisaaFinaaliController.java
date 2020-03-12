package fxMmfinaalit;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tietorakenne.Finaali;

public class LisaaFinaaliController {
	
	@FXML private TableView<Finaali> tableView;
	
	//Näitä käytetään, että voidaan luoda uusi finaali
    @FXML private TextField vuosiTextField;
    @FXML private TextField paikkaTextField;
    @FXML private TextField voittajaTextField;
    @FXML private TextField hopeajoukkueTextField;
    @FXML private TextField lopputulosTextField;
    @FXML private TextField katsojamaaraTextField;
    
    
    /**
     * Tämä metodi tallentaa textfieldeihin annetun datan ja luo uuden finaalin.
     */
    @FXML private void uusiFinaaliTallenna()
    {
        //
    }
}
