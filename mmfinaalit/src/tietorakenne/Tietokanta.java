package tietorakenne;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Tietokanta-luokka, joka huolehtii finaaleista.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" finaaleihin.
 *
 * @author Ossi Lahti
 * @version 10.3.2020 | Lisäsin finaaliluokat
 * @version 11.3.2020 | Lisäsin osallistujamaaluokat
 */
public class Tietokanta {
    private Finaalit finaalit = new Finaalit();
    private Osallistujamaat osallistujamaat = new Osallistujamaat(); 



    /**
     * Poistaa finaaleista, osallistujamaista ja stadioneista ne joilla on nro. Kesken.
     * @param nro viitenumero, jonka mukaan poistetaan
     * @return montako finaalia poistettiin
     */
    public int poista(@SuppressWarnings("unused") int nro) {
        return 0;
    }


    /**
     * Lisää kerhoon uuden finaalin
     * @param finaali lisättävä finaali
     * @throws SailoException jos lisäystä ei voida tehdä
     * </pre>
     */
    public void lisaa(Finaali finaali) throws SailoException {
        finaalit.lisaa(finaali);
    }
    
    /**
     * Listään uusi osallistujamaa tietokantaan
     * @param maa lisättävä harrastus 
     */
    public void lisaa(Osallistujamaa maa) {
        osallistujamaat.lisaa(maa);
    }
    
    /**
     * Korvaa finaalin tietorakenteessa.  Ottaa finaalin omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva finaali.  Jos ei löydy,
     * niin lisätään uutena finaalina.
     * @param finaali lisätäävän finaaliin viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Finaali finaali) throws SailoException { 
        finaalit.korvaaTaiLisaa(finaali); 
    }

    /**
     * Haetaan kaikki finaalin osallistujamaat
     * @param finaali finaali jolle osallistujamaita etsitään
     * @return tietorakenne jossa viiteet löydettyihin osallistujamaihin
     */
    public List<Osallistujamaa> annaOsallistujamaat(Finaali finaali) throws SailoException  {
        return osallistujamaat.annaOsallistujamaat(finaali.getTunnusNro());
    }


    /**
     * Palauttaa i:n finaalin
     * @param i monesko finaali palautetaan
     * @return viite i:teen finaaliin
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Finaali annaFinaali(int i) throws IndexOutOfBoundsException {
        return finaalit.anna(i);
    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        finaalit.setTiedostonPerusNimi(hakemistonNimi + "listammfinaaleista");
        osallistujamaat.setTiedostonPerusNimi(hakemistonNimi + "osallistujamaat");
    }


    /**
     * Lukee tietokannan tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
    	 finaalit = new Finaalit(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
         osallistujamaat = new Osallistujamaat();

         setTiedosto(nimi);
         finaalit.lueTiedostosta();
         osallistujamaat.lueTiedostosta();
    }


    /**
     * Tallettaa tietokannan tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void talleta() throws SailoException {
    	String virhe = "";
        try {
            finaalit.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            osallistujamaat.talleta();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);

    }
    
    public Collection<Finaali> etsi(String hakuehto) throws SailoException { 
        return finaalit.etsi(hakuehto); 
    } 


    /**
     * Testiohjelma tietokannasta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Tietokanta tietokanta = new Tietokanta();

        try {
            //tietokanta.lueTiedostosta("tiedostot");

            Finaali finaali1 = new Finaali(); finaali1.rekisteroi(); finaali1.testiFinaali(); tietokanta.lisaa(finaali1);
            Finaali finaali2 = new Finaali(); finaali2.rekisteroi(); finaali2.testiFinaali(); tietokanta.lisaa(finaali2);
            
            int id1 = finaali1.getTunnusNro();
            int id2 = finaali2.getTunnusNro();
            
            Osallistujamaa brasilia = new Osallistujamaa(id1); brasilia.testiOsallistujamaa(id1); tietokanta.lisaa(brasilia);
            Osallistujamaa saksa = new Osallistujamaa(id2); saksa.testiOsallistujamaa(id2); tietokanta.lisaa(saksa);


            System.out.println("============= Tietokannan testi =================");

            Collection<Finaali> finaalit = tietokanta.etsi("");
            int i = 0;
            for (Finaali finaali: finaalit) {
                System.out.println("Finaali paikassa: " + i);
                finaali.tulosta(System.out);
                List<Osallistujamaa> loytyneet = tietokanta.annaOsallistujamaat(finaali);
                for (Osallistujamaa maa : loytyneet)
                maa.tulosta(System.out);
                i++;
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

