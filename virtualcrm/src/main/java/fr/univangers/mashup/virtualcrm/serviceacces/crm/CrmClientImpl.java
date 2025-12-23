package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.ArrayList;
import java.util.List;

import static fr.univangers.mashup.virtualcrm.utils.TypeConverter.toVirtualLeadDtos;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

public class CrmClientImpl implements CrmClient {
    private static final System.Logger logger = System.getLogger(CrmClientImpl.class.getSimpleName());
    private final InternalCrmClient internalCrmClient;
    private final SalesForceCrmClient salesForceCrmClient;

    public CrmClientImpl() {
        logger.log(INFO, "Initializing CRM Client aggregator...");
        internalCrmClient = InternalCrmClientFactory.getInternalCrmClient();
        salesForceCrmClient = new SalesForceCrmClient();
    }

    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        logger.log(INFO, "Aggregating leads by revenue [{0} - {1}] for state: {2}", lowAnnualRevenue, highAnnualRevenue, state);

        List<InternalLeadDto> internalLeads = internalCrmClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        logger.log(DEBUG, "Fetched {0} leads from Internal CRM", internalLeads.size());
        List<VirtualLeadDto> leads = new ArrayList<>(toVirtualLeadDtos((internalLeads)));

        List<VirtualLeadDto> sfLeads = salesForceCrmClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        logger.log(DEBUG, "Fetched {0} leads from Salesforce", sfLeads.size());
        leads.addAll(sfLeads);

        logger.log(INFO, "Total leads aggregated: {0}", leads.size());
        return leads;
    }

    @Override
    public List<VirtualLeadDto> findLeadsByDate(String startDate, String endDate) {
        logger.log(INFO, "Aggregating leads by date range: [{0} to {1}]", startDate, endDate);

        List<InternalLeadDto> internalLeads = internalCrmClient.findLeadsByDate(startDate, endDate);
        logger.log(DEBUG, "Fetched {0} leads by date from Internal CRM", internalLeads.size());
        List<VirtualLeadDto> leads = new ArrayList<>(toVirtualLeadDtos((internalLeads)));

        List<VirtualLeadDto> sfLeads = salesForceCrmClient.findLeadsByDate(startDate, endDate);
        logger.log(DEBUG, "Fetched {0} leads by date from Salesforce", sfLeads.size());
        leads.addAll(sfLeads);

        logger.log(INFO, "Total leads aggregated by date: {0}", leads.size());
        return leads;
    }
}
