package fr.univangers.mashup.internalcrm.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LeadModelFactory {
    private static final LeaModel model;

    static {
        model = new LeadModelImpl();
        model.addLead(new Lead("Alice", "Martin", 75000, "01 23 45 67 89", "123 Rue du Faubourg", "75001", "Paris", "France", new GregorianCalendar(2025, Calendar.JANUARY, 15), "Innovations Tech", "Île-de-France"));
        model.addLead(new Lead("Bob", "Dupont", 120000, "02 34 56 78 90", "456 Avenue des Champs", "69001", "Lyon", "France", new GregorianCalendar(2025, Calendar.FEBRUARY, 20), "Solutions Créatives", "Auvergne-Rhône-Alpes"));
        model.addLead(new Lead("Charlie", "Durand", 50000, "03 45 67 89 01", "789 Boulevard de la Liberté", "13001", "Marseille", "France", new GregorianCalendar(2025, Calendar.MARCH, 10), "Technologies Futures", "Provence-Alpes-Côte d'Azur"));
        model.addLead(new Lead("David", "Moreau", 95000, "04 56 78 90 12", "234 Place Bellecour", "31000", "Toulouse", "France", new GregorianCalendar(2025, Calendar.APRIL, 25), "Entreprise Commerciale", "Occitanie"));
        model.addLead(new Lead("Eve", "Leroy", 300000, "05 67 89 01 23", "567 Rue de la République", "59000", "Lille", "France", new GregorianCalendar(2025, Calendar.MAY, 30), "Experts Financiers", "Hauts-de-France"));
    }

    private LeadModelFactory() {
    }

    public static LeaModel getModel() {
        return model;
    }
}
