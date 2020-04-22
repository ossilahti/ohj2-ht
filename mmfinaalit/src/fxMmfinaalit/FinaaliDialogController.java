package fxMmfinaalit;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

    @FXML private ScrollPane panelFinaali;
    @FXML private GridPane gridFinaali;
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
    private static Finaali apufinaali = new Finaali(); // Finaali, jolta voi kysyä tietoja..
    private TextField[] edits;
    private int kentta = 0;
    
    /**
     * Luodaan GridPaneen jäsenen tiedot
     * @param gridFinaali mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridFinaali) {
        gridFinaali.getChildren().clear();
        TextField[] edits = new TextField[apufinaali.getKenttia()];
        
        for (int i=0, k = apufinaali.ekaKentta(); k < apufinaali.getKenttia(); k++, i++) {
            Label label = new Label(apufinaali.getKysymys(k));
            gridFinaali.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridFinaali.add(edit, 1, i);
        }
        return edits;
    }
    
    
    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
    

    /**
     * Tyhjentään tekstikentät 
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
        	if ( edit != null ) edit.setText(""); 
    }


    /**
     * Tekee tarvittavat muut alustukset.
     */
    protected void alusta() {
    	 edits = luoKentat(gridFinaali);
         for (TextField edit : edits)
             if ( edit != null )
                 edit.setOnKeyReleased( e -> kasitteleMuutosFinaaliin((TextField)(e.getSource())));
         panelFinaali.setFitToHeight(true);
    }
    
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
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
    	 kentta = Math.max(apufinaali.ekaKentta(), Math.min(kentta, apufinaali.getKenttia()-1));
    	 edits[kentta].requestFocus();
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
    private void kasitteleMuutosFinaaliin(TextField edit) {
        if (finaaliKohdalla == null) return;
        int k = getFieldId(edit,apufinaali.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = finaaliKohdalla.aseta(k,s); 
       
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
        for (int k = finaali.ekaKentta(); k < finaali.getKenttia(); k++) {
            edits[k].setText(finaali.anna(k));
        }
    }
    
    
    /**
     * Luodaan jäsenen kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Finaali kysyFinaali(Stage modalityStage, Finaali oletus, int kentta) {
        return ModalController.<Finaali, FinaaliDialogController>showModal(
                    FinaaliDialogController.class.getResource("FinaaliDialogView.fxml"),
                    "Muokkaa finaalia", modalityStage, oletus, null);
    }

}

