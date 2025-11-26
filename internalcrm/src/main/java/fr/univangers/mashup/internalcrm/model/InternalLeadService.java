package fr.univangers.mashup.internalcrm.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InternalLeadService {
    private final List<InternalLead> leads = new ArrayList<>();

    public List<InternalLead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        return leads.stream()
                .filter(lead -> lead.getAnnualRevenue() >= lowAnnualRevenue
                        && lead.getAnnualRevenue() <= highAnnualRevenue
                        && lead.getState().equals(state))
                .toList();
    }

    public List<InternalLead> findLeadsByDate(Calendar startDate, Calendar endDate) {
        return leads.stream()
                .filter(lead -> lead.getCreationDate().equals(startDate)
                        && lead.getCreationDate().after(startDate)
                        && lead.getCreationDate().equals(endDate)
                        && lead.getCreationDate().before(endDate))
                .toList();
    }

    public void addLead(InternalLead lead) {
        leads.add(lead);
    }

    public void deleteLead(InternalLead lead) {
        leads.remove(lead);
    }
}
