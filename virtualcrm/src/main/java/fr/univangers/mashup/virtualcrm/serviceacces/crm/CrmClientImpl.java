package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.List;

import static fr.univangers.mashup.virtualcrm.utils.TypeConverter.toVirtualLeadDtos;

public class CrmClientImpl implements CrmClient {
    private final InternalCrmClient internalCrmClient;
    private final SalesForceCrmClient salesForceCrmClient;

    public CrmClientImpl() {
        internalCrmClient = new InternalCrmThriftClient();
        salesForceCrmClient = new SalesForceCrmClient();
    }

    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        List<InternalLeadDto> leads = internalCrmClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        return toVirtualLeadDtos(leads);
    }

    @Override
    public List<VirtualLeadDto> findLeadsByDate(String startDate, String endDate) {
        List<InternalLeadDto> leads = internalCrmClient.findLeadsByDate(startDate, endDate);
        return toVirtualLeadDtos(leads);
    }
}
