package fr.univangers.mashup.internalcrm.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LeadModelImpl implements LeaModel {
    private final ArrayList<Lead> leads = new ArrayList<>();

    @Override
    public List<Lead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        return leads.stream()
                .filter(lead -> lead.getAnnualRevenue() >= lowAnnualRevenue
                        && lead.getAnnualRevenue() <= highAnnualRevenue
                        && lead.getState().equals(state))
                .toList();
    }

    @Override
    public List<Lead> findLeadsByDate(Calendar startDate, Calendar endDate) {
        return leads.stream()
                .filter(lead -> lead.getCreationDate().equals(startDate)
                        || lead.getCreationDate().after(startDate)
                        || lead.getCreationDate().equals(endDate)
                        || lead.getCreationDate().before(endDate))
                .toList();
    }

    @Override
    public void addLead(Lead lead) {
        leads.add(lead);
    }

    @Override
    public boolean deleteLead(Lead lead) {
        return leads.remove(lead);
    }
}
