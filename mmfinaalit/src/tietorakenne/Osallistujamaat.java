package tietorakenne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Tietokannan osallistujamaat, joka osaa mm. lisätä uuden osallistujamaan.
 *
 * @author Ossi Lahti
 * @version 11.3.2020
 */
public class Osallistujamaat implements Iterable<Osallistujamaa> {
	
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "osallistujamaat";
    
    /** Taulukko osallistujamaista */
    private final List<Osallistujamaa> alkiot        = new ArrayList<Osallistujamaa>();


    /**
     * Osallistujamaiden alustaminen
     */
    public Osallistujamaat() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden osallistujamaan tietorakenteeseen. Ottaa sen omistukseensa.
     * @param lisattavaMaa lisättävä osallistujamaa.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Osallistujamaa lisattavaMaa) {
        alkiot.add(lisattavaMaa);
        muutettu = true;
    }
    
    
    
    /**
     * Korvaa osallistujamaan tietorakenteessa. Ottaa maan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva maa. Jos ei löydy,
     * niin lisätään uutena harrastuksena.
     * @param maa lisättävän osallistujamaan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */ 
    public void korvaaTaiLisaa(Osallistujamaa maa) throws SailoException {
        int id = maa.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, maa);
                muutettu = true;
                return;
            }
        }
        lisaa(maa);
    }

    
    /**
     * Poistaa valitun maan
     * @param maa poistettava maa
     * @return tosi jos löytyi poistettava tietue
     */
    public boolean poista(Osallistujamaa maa) {
        boolean ret = alkiot.remove(maa);
        if (ret) muutettu = true;
        return ret;
    }
    
    
    /**
     * Tallentaa harrastukset tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Osallistujamaa maa : this) {
                fo.println(maa.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    
    /**
     * Poistaa kaikki tietyn tietyn finaalin osallistujat
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     */
    public int poistaFinaalinOsallistujat(int tunnusNro) {
        int n = 0;
        for (Iterator<Osallistujamaa> it = alkiot.iterator(); it.hasNext();) {
            Osallistujamaa maa = it.next();
            if ( maa.getFinaaliNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    

    /**
     * Lukee finaalit tiedostosta.  
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Osallistujamaa maa = new Osallistujamaa();
                maa.parse(rivi); 
                lisaa(maa);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }



    /**
     * Palauttaa tietokannan osallistujamaiden lukumäärän
     * @return harrastusten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien osallistujamaiden läpikäymiseen
     * @return osallistujamaaiteraattori
     */
    @Override
    public Iterator<Osallistujamaa> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki finaalin osallistujamaat
     * @param finaalinTunnusNro finaalin tunnusnumero jolle osallistujamaita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin osallistujamaihin
     */
    public List<Osallistujamaa> annaOsallistujamaat(int finaalinTunnusNro) {
        List<Osallistujamaa> loydetyt = new ArrayList<Osallistujamaa>();
        for (Osallistujamaa maa : alkiot)
            if (maa.getFinaaliNro() == finaalinTunnusNro) loydetyt.add(maa);
        return loydetyt;
    }


    /**
     * Testiohjelma harrastuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osallistujamaat osallistujamaat = new Osallistujamaat();
        Osallistujamaa brasilia = new Osallistujamaa();
        brasilia.testiOsallistujamaa(1);
        Osallistujamaa saksa = new Osallistujamaa();
        saksa.testiOsallistujamaa(2);
        Osallistujamaa ranska = new Osallistujamaa();
        ranska.testiOsallistujamaa(3);
        Osallistujamaa italia = new Osallistujamaa();
        italia.testiOsallistujamaa(4);

        osallistujamaat.lisaa(brasilia);
        osallistujamaat.lisaa(saksa);
        osallistujamaat.lisaa(ranska);
        osallistujamaat.lisaa(italia);

        System.out.println("============= Harrastukset testi =================");

        List<Osallistujamaa> osallistujamaat2 = osallistujamaat.annaOsallistujamaat(1);

        for (Osallistujamaa maa : osallistujamaat2) {
            System.out.print(maa.getFinaaliNro() + " ");
            maa.tulosta(System.out);
        }

    }
}

