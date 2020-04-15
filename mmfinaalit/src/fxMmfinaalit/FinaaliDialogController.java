package fxMmfinaalit;

import java.net.URL;
import java.util.ResourceBundle;

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
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handleCancel() {
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

