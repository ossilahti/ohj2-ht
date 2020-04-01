package tietorakenne;

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
    private final Finaalit finaalit = new Finaalit();
    private final Osallistujamaat osallistujamaat = new Osallistujamaat(); 


    /**
     * Palautaa finaalien lukumäärän
     * @return finaalien määrä
     */
    public int getFinaalit() {
        return finaalit.getLkm();
    }


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
     * Haetaan kaikki finaalin osallistujamaat
     * @param finaali finaali jolle osallistujamaita etsitään
     * @return tietorakenne jossa viiteet löydettyihin osallistujamaihin
     */
    public List<Osallistujamaa> annaOsallistujamaat(Finaali finaali) {
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
     * Lukee tietokannan tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        finaalit.lueTiedostosta(nimi);
        osallistujamaat.lueTiedostosta(nimi);
    }


    /**
     * Tallettaa tietokannan tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void talleta() throws SailoException {
        finaalit.talleta();
        osallistujamaat.talleta();
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
            // kerho.lueTiedostosta("kelmit");

            Finaali finaali1 = new Finaali(); finaali1.rekisteroi(); finaali1.testiFinaali(); tietokanta.lisaa(finaali1);
            Finaali finaali2 = new Finaali(); finaali2.rekisteroi(); finaali2.testiFinaali(); tietokanta.lisaa(finaali2);
            
            int id1 = finaali1.getTunnusNro();
            int id2 = finaali2.getTunnusNro();
            
            Osallistujamaa brasilia = new Osallistujamaa(id1); brasilia.testiOsallistujamaa(id1); tietokanta.lisaa(brasilia);
            Osallistujamaa saksa = new Osallistujamaa(id2); saksa.testiOsallistujamaa(id2); tietokanta.lisaa(saksa);


            System.out.println("============= Tietokannan testi =================");

            for (int i = 0; i < tietokanta.getFinaalit(); i++) {
                Finaali finaali = tietokanta.annaFinaali(i);
                System.out.println("Finaali paikassa: " + i);
                finaali.tulosta(System.out);
                List<Osallistujamaa> loytyneet = tietokanta.annaOsallistujamaat(finaali);
                for (Osallistujamaa maa : loytyneet)
                maa.tulosta(System.out);

            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

