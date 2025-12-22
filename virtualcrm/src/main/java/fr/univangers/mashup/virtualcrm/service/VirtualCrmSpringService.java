package fr.univangers.mashup.virtualcrm.service;

import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;
import fr.univangers.mashup.virtualcrm.serviceacces.GeoLocalisationServiceClient;
import fr.univangers.mashup.virtualcrm.serviceacces.GeoLocalisationServiceClientFactory;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.CrmClient;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.CrmClientFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

@RestController
public class VirtualCrmSpringService implements VirtualCrmService {
    private static final System.Logger logger = System.getLogger(VirtualCrmSpringService.class.getSimpleName());
    private static final CrmClient crmClient = CrmClientFactory.getCrmClient();
    private static final GeoLocalisationServiceClient localisationService = GeoLocalisationServiceClientFactory.getGeoLocalisationServiceClient();

    @Override
    @RequestMapping(value = "/leads", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<VirtualLeadDto> findLeads(
            @RequestParam(name = "low_annual_revenue") double lowAnnualRevenue,
            @RequestParam(name = "high_annual_revenue") double highAnnualRevenue,
            @RequestParam(name = "state") String state
    ) {
        logger.log(INFO, "REST Request: findLeads [revenue: {0} - {1}, state: {2}]", lowAnnualRevenue, highAnnualRevenue, state);
        List<VirtualLeadDto> leads = crmClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        for (VirtualLeadDto lead : leads) {
            lead.setGeographicPoint(localisationService.getGeographicPointFromLead(lead));
        }
        logger.log(DEBUG, "REST Response findLeads: returning {0} leads", leads.size());
        return leads;
    }

    @Override
    @RequestMapping(value = "/leads/date", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<VirtualLeadDto> findLeadsByDate(
            @RequestParam(name = "start_date") String startDate,
            @RequestParam(name = "end_date") String endDate
    ) {
        logger.log(INFO, "REST Request: findLeadsByDate [{0} to {1}]", startDate, endDate);
        List<VirtualLeadDto> leads = crmClient.findLeadsByDate(startDate, endDate);
        for (VirtualLeadDto lead : leads) {
            lead.setGeographicPoint(localisationService.getGeographicPointFromLead(lead));
        }
        logger.log(DEBUG, "REST Response findLeadsByDate: returning {0} leads", leads.size());
        return leads;
    }
}
