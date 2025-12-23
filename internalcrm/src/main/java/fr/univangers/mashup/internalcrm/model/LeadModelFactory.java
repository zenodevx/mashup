package fr.univangers.mashup.internalcrm.model;

import java.util.GregorianCalendar;

public class LeadModelFactory {
    private static final LeaModel model;

    static {
        model = new LeadModelImpl();
        model.addLead(new Lead("Bertha", "Boxer", 900750000.0, "(850) 644-4200", "321 Westcott Building", "32306", "Tallahassee", "USA", new GregorianCalendar(2025, 9, 1, 9, 22, 27), "Farmers Coop. of Florida", "FL"));
        model.addLead(new Lead("Jeff", "Glimpse", 1250000.0, "886-2-25474189", "No. 1, Section 4, Roosevelt Rd", "10617", "Taipei", "Taiwan", new GregorianCalendar(2025, 4, 2, 9, 22, 27), "Jackson Controls", "Taipei City"));
        model.addLead(new Lead("Pat", "Stumuller", 5400000.0, "33562156600", "12 Avenue de l'Opéra", "75001", "Paris", "France", new GregorianCalendar(2025, 2, 2, 9, 22, 27), "Pyramid Construction Inc.", "Ile-de-France"));
        model.addLead(new Lead("David", "Monaco", 8900000.0, "(033) 452-1299", "1-6-1 Roppongi, Minato-ku", "106-6001", "Tokyo", "Japan", new GregorianCalendar(2025, 9, 3, 9, 22, 27), "Blues Entertainment Corp.", "Tokyo"));
        model.addLead(new Lead("Jack", "Rogers", 350000000.0, "(336) 222-7000", "525 S. Lexington Ave", "27215", "Burlington", "USA", new GregorianCalendar(2025, 8, 2, 9, 22, 27), "Burlington Textiles Corp of America", "NC"));
        model.addLead(new Lead("Shelly", "Brownell", 15000000.0, "(408) 326-9000", "101 Lucas Valley Rd", "94903", "San Rafael", "USA", new GregorianCalendar(2025, 1, 7, 9, 22, 27), "Western Telecommunications Corp.", "CA"));
    }

    private LeadModelFactory() {
    }

    public static LeaModel getModel() {
        return model;
    }
}
