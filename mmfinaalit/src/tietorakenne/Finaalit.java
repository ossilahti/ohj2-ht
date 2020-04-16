package tietorakenne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tietorakenteen luokka Finaalit, joka osaa mm. lisätä uuden finaalin tietokantaan.
 *
 * @author Ossi Lahti
 * @version 10.3.2020
 */
public class Finaalit implements Iterable<Finaali> {
    private static final int MAX_JASENIA   = 21;
    private int              lkm           = 0;
    private boolean muutettu 			   = false;
    private Finaali          alkiot[]      = new Finaali[MAX_JASENIA];
    private String kokoNimi 			   = "";
    private String tiedostonPerusNimi      = "listammfinaaleista";

   

    /**
     * Oletusmuodostaja
     */
    public Finaalit() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.
     * @param jasen lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void lisaa(Finaali finaali) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = finaali;
        lkm++;
        muutettu = true;
    }


    /**
     * Palauttaa viitteen i:teen finaaliin.
     * @param i monennenko finaalin viite halutaan
     * @return viite finaaliin, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Finaali anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Korvaa finaalin tietorakenteessa.  Ottaa finaalin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva finaali.  Jos ei löydy,
     * niin lisätään uutena finaalina.
     * @param finaali lisätäävän finaaliin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Finaali finaali) throws SailoException {
        int id = finaali.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = finaali;
                muutettu = true;
                return;
            }
        }
        lisaa(finaali);
    }
    
    public class FinaalitIterator implements Iterator<Finaali> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava jäsen
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Finaali next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori jäsenistään.
     * @return jäsen iteraattori
     */
    public Iterator<Finaali> iterator() {
        return new FinaalitIterator();
    }
    
    /**
     * Etsitään hakuehtoon vastaavat kohteet listchooserista
     * @param hakuehto hakuehto
     * @return lista löytyneistä
     */
    @SuppressWarnings("unused")
    public Collection<Finaali> etsi(String hakuehto) { 
        Collection<Finaali> loytyneet = new ArrayList<Finaali>(); 
        for (Finaali finaali : this) { 
            loytyneet.add(finaali);  
        } 
        return loytyneet; 
    }
    
    


    /**
     * Lukee finaalin tiedostosta.  Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Finaalin nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
            // int maxKoko = Mjonot.erotaInt(rivi,10); // tehdään jotakin

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Finaali finaali = new Finaali();
                finaali.parse(rivi); // voisi olla virhekäsittely
                lisaa(finaali);
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
     * Tallennetaan alkiot.
     * @throws SailoException
     */

    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Finaali finaali : this) {
                fo.println(finaali.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    



    /**
     * Palauttaa Kerhon koko nimen
     * @return Kerhon koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }



    /**
     * Palauttaa tietokannan finaalien lukumäärän
     * @return finaalien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Testiohjelma finaaleille
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Finaalit finaalitTesti = new Finaalit();

        Finaali finaali1 = new Finaali(); 
        Finaali finaali2 = new Finaali();
        finaali1.rekisteroi();
        finaali1.testiFinaali();
        finaali2.rekisteroi();
        finaali2.testiFinaali();

        try {
            finaalitTesti.lisaa(finaali1);
            finaalitTesti.lisaa(finaali2);

            System.out.println("============= Finaalien testi =================");

            int i = 0;
            for (Finaali finaali: finaalitTesti) { 
                System.out.println("Finaali " + i);
                finaali.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

