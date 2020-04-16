package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

///import javafx.beans.property.SimpleStringProperty;

/**
 * Finaali-luokka joka rekisteröi finaalin Finaalit-luokkaan.
 *
 * @author Ossi Lahti
 * @version 1.0, 10.3.2020
 */

public class Finaali implements Cloneable {

	private int        tunnusNro;
    private String     vuosi			= "";       
    private String     finaalipaikka	= "";
    private String     voittaja     	= "";
    private String     hopeajoukkue 	= "";
    private String     lopputulos   	= "";
    private int        katsojia;
    private static int seuraavaNro    = 1;


	    /**
	     * @return vuosi, milloin finaali pelataan.
	     */
	    public String getVuosi() {
	        return vuosi;
	    }


	    /**
	     * Apumetodi, jolla saadaan täytettyä testiarvot finaalille.
	     */
	    public void testiFinaali() {
	        vuosi = "" + rand(1930, 2018);
	        finaalipaikka = "Brasilia";
	        voittaja = "Saksa";
	        hopeajoukkue = "Ranska";
	        lopputulos = rand(1,7) + "-" + rand(1,7);
	        katsojia = rand(50000, 200000);
	    }
	    
	    /**
	     * Tekee satunnaisen numeron ala, ja yla väliltä.
	     * @param ala alin mahdollinen numero välillä.
	     * @param yla ylin mahdollinen numero välillä.
	     * @return palauttaa kokonaisluvun.
	     */
	    public static int rand(int ala, int yla) {
	        double n = (yla-ala)*Math.random() + ala;
	        return (int)Math.round(n);
	    }


	    /**
	     * Tulostetaan henkilön tiedot
	     * @param out tietovirta johon tulostetaan
	     */
	    public void tulosta(PrintStream out) {
	        out.println(String.format("%03d", tunnusNro, 3) + " || Vuosi: " + vuosi + " || Järjestäjä: " + finaalipaikka);
	        out.println("Voittaja: " + voittaja + " || Hopeajoukkue: " + hopeajoukkue + " || Lopputulos: " + lopputulos);
	        out.println("Katsojia yhteensä: " + String.format("%6d", katsojia)); 
	    }


	    /**
	     * Tulostetaan finaalin tiedot
	     * @param os tietovirta johon tulostetaan
	     */
	    public void tulosta(OutputStream os) {
	        tulosta(new PrintStream(os));
	    }


	    /**
	     * Antaa finaalille seuraavan rekisterinumeron.
	     * @return finaalin uusi tunnusNro
	     */
	    public int rekisteroi() {
	        tunnusNro = seuraavaNro;
	        seuraavaNro++;
	        return tunnusNro;
	    }


	    /**
	     * Palauttaa finaalin tunnusnumeron.
	     * @return finaalin tunnusnumero
	     */
	    public int getTunnusNro() {
	        return tunnusNro;
	    }
	    
	    /**
	     * Asettaa tunnusnumeron ja samalla varmistaa että
	     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	     * @param nr asetettava tunnusnumero
	     */
	    private void setTunnusNro(int nr) {
	        tunnusNro = nr;
	        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
	    }
	    

	    /**
	     * Palauttaa jäsenen tiedot merkkijonona jonka voi tallentaa tiedostoon.
	     * @return jäsen tolppaeroteltuna merkkijonona 
	     * @example
	     * <pre name="test">
	     *   Jasen jasen = new Jasen();
	     *   jasen.parse("   3  |  Ankka Aku   | 030201-111C");
	     *   jasen.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
	     * </pre>  
	     */
	    @Override
	    public String toString() {
	        return "" +
	                getTunnusNro() + "|" +
	                vuosi + "|" +
	                voittaja + "|" +
	                hopeajoukkue + "|" +
	                lopputulos + "|" +
	                katsojia + "|"; 
	    }


	    /**
	     * Selvitää jäsenen tiedot | erotellusta merkkijonosta
	     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
	     * @param rivi josta jäsenen tiedot otetaan
	     * 
	     * @example
	     * <pre name="test">
	     *   Jasen jasen = new Jasen();
	     *   jasen.parse("   3  |  Ankka Aku   | 030201-111C");
	     *   jasen.getTunnusNro() === 3;
	     *   jasen.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
	     *
	     *   jasen.rekisteroi();
	     *   int n = jasen.getTunnusNro();
	     *   jasen.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
	     *   jasen.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
	     *   jasen.getTunnusNro() === n+20+1;
	     *     
	     * </pre>
	     */
	    public void parse(String rivi) {
	        StringBuffer sb = new StringBuffer(rivi);
	        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
	        vuosi = Mjonot.erota(sb, '|', vuosi);
	        voittaja = Mjonot.erota(sb, '|', voittaja);
	        hopeajoukkue = Mjonot.erota(sb, '|', hopeajoukkue);
	        lopputulos = Mjonot.erota(sb, '|', lopputulos);
	        katsojia = Mjonot.erota(sb, '|', katsojia);
	    }
	    
	    @Override
	    public boolean equals(Object jasen) {
	        if ( jasen == null ) return false;
	        return this.toString().equals(jasen.toString());
	    }

	    @Override
	    public int hashCode() {
	        return tunnusNro;
	    }

	    /**
	     * Testiohjelma finaalille..
	     * @param args ei käytössä
	     */
	    public static void main(String args[]) {
	        Finaali finaali1 = new Finaali();
	        Finaali finaali2 = new Finaali();
	        finaali1.rekisteroi();
	        finaali2.rekisteroi();
	        
	        finaali1.testiFinaali();
	        finaali1.tulosta(System.out);

	        finaali2.testiFinaali();
	        finaali2.tulosta(System.out);

	        finaali2.testiFinaali();
	        finaali2.tulosta(System.out);
	    }


		public String getFinaalipaikka() {
			return finaalipaikka;
		}


		public String getVoittaja() {
			return voittaja;
		}


		public String getHopeajoukkue() {
			return hopeajoukkue;
		}
		
	    /**
	     * @param s finaalille laitettava vuosi
	     * @return virheilmoitus, null jos ok
	     */
	    public String setVuosi(String s) {
	    	if (s.startsWith("18")) return "Yhtään peliä ei pelattu 1800-luvulla.";
	    	if (s.startsWith("192")) return "Yhtään peliä ei pelattu ennen 1930-lukua.";
	    	if (!s.matches("[0-9]*") ) return "Vuoden on oltava numeerisessa muodossa.";
	    	vuosi = s;
	        return null;
	    }

	    /**
	     * @param s finaalin järjestäjä
	     * @return virheilmoitus, null jos ok
	     */
	    public String setJarjestaja(String s) {
	    	if ( !s.matches("[^0-9]*") ) return "Järjestäjän nimessä ei voi olla numeroita.";
	        finaalipaikka = s;
	        return null;
	    }

	    
	    /**
	     * @param s finaalille laitettava voittaja
	     * @return virheilmoitus, null jos ok
	     */
	    public String setVoittaja(String s) {
	    	if ( !s.matches("[^0-9]*") ) return "Maan nimessä ei voi olla numeroita.";
	        voittaja = s;
	        return null;
	    }

	    
	    /**
	     * @param s finaalille laitettava hopeajoukkue
	     * @return virheilmoitus, null jos ok
	     */
	    public String setHopeajoukkue(String s) {
	        if ( !s.matches("[^0-9]*") ) return "Maan nimessä ei voi olla numeroita.";
	        hopeajoukkue = s;
	        return null;
	    }
	    
	    /**
	     * Tehdään identtinen klooni jäsenestä
	     * @return Object kloonattu jäsen
	     * @example
	     * <pre name="test">
	     * #THROWS CloneNotSupportedException 
	     *   Jasen jasen = new Jasen();
	     *   jasen.parse("   3  |  Ankka Aku   | 123");
	     *   Jasen kopio = jasen.clone();
	     *   kopio.toString() === jasen.toString();
	     *   jasen.parse("   4  |  Ankka Tupu   | 123");
	     *   kopio.toString().equals(jasen.toString()) === false;
	     * </pre>
	     */
	    @Override
	    public Finaali clone() throws CloneNotSupportedException {
	        Finaali uusi;
	        uusi = (Finaali) super.clone();
	        return uusi;
	    }
}