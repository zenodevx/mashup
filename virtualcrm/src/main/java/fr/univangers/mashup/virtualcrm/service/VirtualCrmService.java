package fr.univangers.mashup.virtualcrm.service;

import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.List;

public interface VirtualCrmService {
    List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state);

    List<VirtualLeadDto> findLeadsByDate(String startDate, String endDate);
}
