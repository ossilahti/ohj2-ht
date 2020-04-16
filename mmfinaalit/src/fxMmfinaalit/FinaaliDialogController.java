package fxMmfinaalit;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tietorakenne.Finaali;

/**
 * Kysytään finaalin tiedot luomalla sille uusi dialogi
 * 
 * @author Ossi Lahti
 * @version 15.4.2020
 *
 */
public class FinaaliDialogController implements ModalControllerInterface<Finaali>,Initializable  {

    @FXML private TextField editVuosi;
    @FXML private TextField editJarjestaja;
    @FXML private TextField editVoittaja;
    @FXML private TextField editHopeajoukkue;    
    @FXML private Label labelVirhe;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {    	
    	if ( finaaliKohdalla != null && finaaliKohdalla.getVuosi().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
    	}
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
    	finaaliKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

// ========================================================    
    private Finaali finaaliKohdalla;
    private TextField edits[];
   

    /**
     * Tyhjentään tekstikentät 
     * @param edits tauluko jossa tyhjennettäviä tektsikenttiä
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            edit.setText("");
    }


    /**
     * Tekee tarvittavat muut alustukset.
     */
    protected void alusta() {
        edits = new TextField[]{editVuosi, editJarjestaja, editVoittaja, editHopeajoukkue};
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased( e -> kasitteleMuutosFinaaliin(k, (TextField)(e.getSource())));
        }
    }
    
    
    @Override
    public void setDefault(Finaali oletus) {
        finaaliKohdalla = oletus;
        naytaFinaali(edits, finaaliKohdalla);
    }

    
    @Override
    public Finaali getResult() {
        return finaaliKohdalla;
    }
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        editVuosi.requestFocus();
    }
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    
    /**
     * Käsitellään finaaliin tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosFinaaliin(int k, TextField edit) {
        if (finaaliKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
           case 1 : virhe = finaaliKohdalla.setVuosi(s); break;
           case 2 : virhe = finaaliKohdalla.setJarjestaja(s); break;
           case 3 : virhe = finaaliKohdalla.setVoittaja(s); break;
           case 4 : virhe = finaaliKohdalla.setHopeajoukkue(s); break;
           default:
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään finaalin tiedot TextField komponentteihin
     * @param edits taulukko jossa tekstikenttiä
     * @param finaali näytettävä finaali
     */
    public static void naytaFinaali(TextField[] edits, Finaali finaali) {
        if (finaali == null) return;
        edits[0].setText(finaali.getVuosi());
        edits[1].setText(finaali.getFinaalipaikka());
        edits[2].setText(finaali.getVoittaja());
        edits[3].setText(finaali.getHopeajoukkue());
    }
    
    
    /**
     * Luodaan jäsenen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Finaali kysyFinaali(Stage modalityStage, Finaali oletus) {
        return ModalController.<Finaali, FinaaliDialogController>showModal(
                    FinaaliDialogController.class.getResource("FinaaliDialogView.fxml"),
                    "Muokkaa finaalia", modalityStage, oletus, null);
    }

}

