package fr.univangers.mashup.internalcrm;

import fr.univangers.mashup.internalcrm.model.InternalLead;
import fr.univangers.mashup.internalcrm.model.InternalLeadService;
import fr.univangers.mashup.internalcrm.thrift.InternalCrmService;
import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;

import java.util.Calendar;
import java.util.List;

import static fr.univangers.mashup.internalcrm.LeadToInternalLeadDtoConverter.toInternalLead;
import static fr.univangers.mashup.internalcrm.LeadToInternalLeadDtoConverter.toInternalLeadDtos;

public class InternalCrmServiceImpl implements InternalCrmService.Iface {
    private final InternalLeadService leadService = new InternalLeadService();

    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        List<InternalLead> leads = leadService.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        return toInternalLeadDtos(leads);
    }

    @Override
    public List<InternalLeadDto> findLeadsByDate(long startDateTimestamp, long endDateTimestamp) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(startDateTimestamp);
        endDate.setTimeInMillis(endDateTimestamp);

        List<InternalLead> leads = leadService.findLeadsByDate(startDate, endDate);
        return toInternalLeadDtos(leads);
    }

    @Override
    public void addLead(InternalLeadDto leadDto) {
        leadService.addLead(toInternalLead(leadDto));
    }

    @Override
    public void deleteLead(InternalLeadDto leadDto) {
        leadService.deleteLead(toInternalLead(leadDto));
    }
}
