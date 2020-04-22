package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;

///import javafx.beans.property.SimpleStringProperty;

/**
 * Finaali-luokka joka rekisteröi finaalin Finaalit-luokkaan.
 *
 * @author Ossi Lahti
 * @version 1.0, 10.3.2020
 */

public class Finaali implements Cloneable, Tietue {

	private int tunnusNro;
	private String vuosi = "";
	private String finaalipaikka = "";
	private String voittaja = "";
	private String hopeajoukkue = "";
	private String lopputulos = "";
	private String katsojia;
	private static int seuraavaNro = 1;

	/**
	 * Finaalien vertailija
	 */
	public static class Vertailija implements Comparator<Finaali> {
		private int k;

		@SuppressWarnings("javadoc")
		public Vertailija(int k) {
			this.k = k;
		}

		@Override
		public int compare(Finaali fin1, Finaali fin2) {
			return fin1.getAvain(k).compareToIgnoreCase(fin2.getAvain(k));
		}
	}

	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * 
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	public String getAvain(int k) {
		switch (k) {
		case 0:
			return "" + tunnusNro;
		case 1:
			return "" + vuosi;
		case 2:
			return "" + finaalipaikka; // vaihda vuosi ja pvm keskenään
		case 3:
			return "" + voittaja;
		case 4:
			return "" + hopeajoukkue;
		case 5:
			return "" + lopputulos;
		case 6:
			return "" + katsojia;
		default:
			return "Kaikki ei oo nyt kunnossa.";
		}
	}

	/**
	 * Palauttaa jäsenen kenttien lukumäärän
	 * 
	 * @return kenttien lukumäärä
	 */
	@Override
	public int getKenttia() {
		return 7;
	}

	/**
	 * Eka kenttä joka on mielekäs kysyttäväksi
	 * 
	 * @return eknn kentän indeksi
	 */
	@Override
	public int ekaKentta() {
		return 1;
	}

	/**
	 * Alustetaan finaalin merkkijono-attribuuti tyhjiksi jonoiksi ja tunnusnro = 0.
	 */
	public Finaali() {
		// Toistaiseksi ei tarvita mitään
	}

	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * 
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	@Override
	public String anna(int k) {
		switch (k) {
		case 0:
			return "" + tunnusNro;
		case 1:
			return "" + vuosi;
		case 2:
			return "" + finaalipaikka; // vaihda vuosi ja pvm keskenään
		case 3:
			return "" + voittaja;
		case 4:
			return "" + hopeajoukkue;
		case 5:
			return "" + lopputulos;
		case 6:
			return "" + katsojia;
		default:
			return "Kaikki ei oo nyt kunnossa.";
		}
	}

	/**
	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
	 * 
	 * @param k    kuinka monennen kentän arvo asetetaan
	 * @param jono jonoa joka asetetaan kentän arvoksi
	 * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
	 * @example
	 * 
	 *          <pre name="test">
	 *   Jasen jasen = new Jasen();
	 *   jasen.aseta(1,"Ankka Aku") === null;
	 *   jasen.aseta(2,"kissa") =R= "Hetu liian lyhyt"
	 *   jasen.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
	 *   jasen.aseta(2,"030201-111C") === null; 
	 *   jasen.aseta(9,"kissa") === "Liittymisvuosi väärin jono = \"kissa\"";
	 *   jasen.aseta(9,"1940") === null;
	 *          </pre>
	 */
	@Override
	public String aseta(int k, String jono) {
		String tjono = jono.trim();
		StringBuffer sb = new StringBuffer(tjono);
		switch (k) {
		case 0:
			setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
			return null;
		case 1:
			vuosi = tjono;
			return null;
		case 2:
			finaalipaikka = tjono;
			return null;
		case 3:
			voittaja = tjono;
			return null;
		case 4:
			hopeajoukkue = tjono;
			return null;
		case 5:
			lopputulos = tjono;
			return null;
		case 6:
			katsojia = tjono;
			return null;
		default:
			return "Kaikki ei oo kunnossa.";
		}
	}

	/**
	 * Palauttaa k:tta finaalin kenttää vastaavan kysymyksen
	 * 
	 * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
	 * @return k:netta kenttää vastaava kysymys
	 */
	@Override
	public String getKysymys(int k) {
		switch (k) {
		case 0:
			return "Tunnusnumero";
		case 1:
			return "Vuosi";
		case 2:
			return "Finaalipaikka";
		case 3:
			return "Voittaja";
		case 4:
			return "Hopeajoukkue";
		case 5:
			return "Lopputulos";
		case 6:
			return "Katsojia";
		default:
			return "Joku on nyt pielessä.";
		}
	}

	/**
	 * Tutkii onko finaalin tiedot samat kuin parametrina tuodun finaalin tiedot
	 * 
	 * @param finaali jäsen johon verrataan
	 * @return true jos kaikki tiedot samat, false muuten
	 */
	public boolean equals(Finaali finaali) {
		if (finaali == null)
			return false;
		for (int k = 0; k < getKenttia(); k++)
			if (!anna(k).equals(finaali.anna(k)))
				return false;
		return true;
	}

	@Override
	public boolean equals(Object finaali) {
		if (finaali instanceof Finaali)
			return equals((Finaali) finaali);
		return false;
	}

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
		lopputulos = rand(1, 7) + "-" + rand(1, 7);
		katsojia = "" + rand(50000, 200000);
	}

	/**
	 * Tekee satunnaisen numeron ala, ja yla väliltä.
	 * 
	 * @param ala alin mahdollinen numero välillä.
	 * @param yla ylin mahdollinen numero välillä.
	 * @return palauttaa kokonaisluvun.
	 */
	public static int rand(int ala, int yla) {
		double n = (yla - ala) * Math.random() + ala;
		return (int) Math.round(n);
	}

	/**
	 * Tulostetaan henkilön tiedot
	 * 
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("Finaalin numero: " + getTunnusNro() + " || Vuosi: " + vuosi + " || Järjestäjä: " + finaalipaikka);
		out.println("Voittaja: " + voittaja + " || Hopeajoukkue: " + hopeajoukkue + " || Lopputulos: " + lopputulos);
		out.println("Katsojia yhteensä: " + String.format("%6d", katsojia));
	}

	/**
	 * Tulostetaan finaalin tiedot
	 * 
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}

	/**
	 * Antaa finaalille seuraavan rekisterinumeron.
	 * 
	 * @return finaalin uusi tunnusNro
	 */
	public int rekisteroi() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}

	/**
	 * Palauttaa finaalin tunnusnumeron.
	 * 
	 * @return finaalin tunnusnumero
	 */
	public int getTunnusNro() {
		return tunnusNro;
	}

	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että seuraava numero on aina
	 * suurempi kuin tähän mennessä suurin.
	 * 
	 * @param nr asetettava tunnusnumero
	 */
	private void setTunnusNro(int nr) {
		tunnusNro = nr;
		if (tunnusNro >= seuraavaNro)
			seuraavaNro = tunnusNro + 1;
	}

	/**
	 * Palauttaa jäsenen tiedot merkkijonona jonka voi tallentaa tiedostoon.
	 * 
	 * @return jäsen tolppaeroteltuna merkkijonona
	 * @example
	 * 
	 *          <pre name="test">
	 *   Jasen jasen = new Jasen();
	 *   jasen.parse("   3  |  Ankka Aku   | 030201-111C");
	 *   jasen.toString().startsWith("3|Ankka Aku|030201-111C|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
	 *          </pre>
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		String erotin = "";
		for (int k = 0; k < getKenttia(); k++) {
			sb.append(erotin);
			sb.append(anna(k));
			erotin = "|";
		}
		return sb.toString();
	}

	/**
	 * Selvitää jäsenen tiedot | erotellusta merkkijonosta Pitää huolen että
	 * seuraavaNro on suurempi kuin tuleva tunnusNro.
	 * 
	 * @param rivi josta jäsenen tiedot otetaan
	 * 
	 * @example
	 * 
	 *          <pre name="test">
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
	 *          </pre>
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		for (int k = 0; k < getKenttia(); k++)
			aseta(k, Mjonot.erota(sb, '|'));
	}

	@Override
	public int hashCode() {
		return tunnusNro;
	}

	/**
	 * Testiohjelma finaalille..
	 * 
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
		if (s.startsWith("18"))
			return "Yhtään peliä ei pelattu 1800-luvulla.";
		if (s.startsWith("192"))
			return "Yhtään peliä ei pelattu ennen 1930-lukua.";
		if (!s.matches("[0-9]*"))
			return "Vuoden on oltava numeerisessa muodossa.";
		vuosi = s;
		return null;
	}

	/**
	 * @param s finaalin järjestäjä
	 * @return virheilmoitus, null jos ok
	 */
	public String setJarjestaja(String s) {
		if (!s.matches("[^0-9]*"))
			return "Järjestäjän nimessä ei voi olla numeroita.";
		finaalipaikka = s;
		return null;
	}

	/**
	 * @param s finaalille laitettava voittaja
	 * @return virheilmoitus, null jos ok
	 */
	public String setVoittaja(String s) {
		if (!s.matches("[^0-9]*"))
			return "Maan nimessä ei voi olla numeroita.";
		voittaja = s;
		return null;
	}

	/**
	 * @param s finaalille laitettava hopeajoukkue
	 * @return virheilmoitus, null jos ok
	 */
	public String setHopeajoukkue(String s) {
		if (!s.matches("[^0-9]*"))
			return "Maan nimessä ei voi olla numeroita.";
		hopeajoukkue = s;
		return null;
	}

	/**
	 * Tehdään identtinen klooni finaalista
	 * 
	 * @return Object kloonattu finaali
	 * @example
	 * 
	 *          <pre name="test">
	 * #THROWS CloneNotSupportedException 
	 *   Jasen jasen = new Jasen();
	 *   jasen.parse("   3  |  Ankka Aku   | 123");
	 *   Jasen kopio = jasen.clone();
	 *   kopio.toString() === jasen.toString();
	 *   jasen.parse("   4  |  Ankka Tupu   | 123");
	 *   kopio.toString().equals(jasen.toString()) === false;
	 *          </pre>
	 */
	@Override
	public Finaali clone() throws CloneNotSupportedException {
		Finaali uusi;
		uusi = (Finaali) super.clone();
		return uusi;
	}
}