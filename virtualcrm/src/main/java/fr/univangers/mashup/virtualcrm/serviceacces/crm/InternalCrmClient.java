package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;
import fr.univangers.mashup.internalcrm.thrift.LeadNotFoundException;

public interface InternalCrmClient extends CrmClient {
    void addLead(InternalLeadDto leadDto) throws InvalidDateFormatException;

    void deleteLead(InternalLeadDto leadDto) throws InvalidDateFormatException, LeadNotFoundException;
}
