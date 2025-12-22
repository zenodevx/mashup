package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.List;

public interface CrmClient {
    List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);

    List<VirtualLeadDto> findLeadsByDate(String startDate, String endDate);
}
