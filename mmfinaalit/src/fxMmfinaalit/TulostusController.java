package fxMmfinaalit;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import tietorakenne.Finaali;

public class TulostusController implements ModalControllerInterface<String> {

	@FXML TextArea tulostusAlue;
	
    @Override
    public void setDefault(String oletus) {
        // if ( oletus == null ) return;
        tulostusAlue.setText(oletus);
    }

    
    /**
     * @return alue johon tulostetaan
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }
    
    
    /**
     * Näyttää tulostusalueessa tekstin
     * @param tulostus tulostettava teskti
     * @return kontrolleri, jolta voidaan pyytää lisää tietoa
     */
    public static TulostusController tulosta(String tulostus) {
        TulostusController tulostusCtrl = 
          ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"),
                                       "Tulostus", tulostus);
        return tulostusCtrl;
    }


	public static TulostusController tulosta(Finaali tulostus) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getResult() {
		return null;
	}


	@Override
	public void handleShown() {
		//
	}

}
