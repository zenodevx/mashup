package fr.univangers.mashup.internalcrm.model;

import java.util.Calendar;
import java.util.List;

public interface LeaModel {
    List<Lead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);

    List<Lead> findLeadsByDate(Calendar startDate, Calendar endDate);

    void addLead(Lead lead);

    boolean deleteLead(Lead lead);
}
