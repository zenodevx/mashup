package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;
import fr.univangers.mashup.internalcrm.thrift.LeadNotFoundException;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.List;

public interface InternalCrmClient {
    List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);

    List<InternalLeadDto> findLeadsByDate(String startDate, String endDate);

    void addLead(InternalLeadDto leadDto) throws InvalidDateFormatException;

    void deleteLead(InternalLeadDto leadDto) throws InvalidDateFormatException, LeadNotFoundException;
}
