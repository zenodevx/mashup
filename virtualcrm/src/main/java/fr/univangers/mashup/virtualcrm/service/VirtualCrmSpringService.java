package fr.univangers.mashup.virtualcrm.service;

import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;
import fr.univangers.mashup.virtualcrm.serviceacces.GeoLocalisationServiceClient;
import fr.univangers.mashup.virtualcrm.serviceacces.GeoLocalisationServiceClientFactory;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.CrmClient;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.InternalCrmClientFactory;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.SalesForceCrmClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.Logger.Level.*;
import static java.util.Comparator.*;

@RestController
public class VirtualCrmSpringService implements VirtualCrmService {
    private static final System.Logger logger = System.getLogger(VirtualCrmSpringService.class.getSimpleName());
    private static final List<CrmClient> crmClients = new ArrayList<>();
    private static final GeoLocalisationServiceClient localisationService = GeoLocalisationServiceClientFactory.getGeoLocalisationServiceClient();

    static {
        logger.log(INFO, "Initializing VirtualCrmSpringService");
        crmClients.add(InternalCrmClientFactory.getInternalCrmClient());
        crmClients.add(new SalesForceCrmClient());
    }

    @Override
    @RequestMapping(value = "/leads", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<VirtualLeadDto> findLeads(
            @RequestParam(name = "low_annual_revenue") double lowAnnualRevenue,
            @RequestParam(name = "high_annual_revenue") double highAnnualRevenue,
            @RequestParam(name = "state") String state
    ) {
        logger.log(INFO, "Request received: findLeads [revenue: {0} - {1}, state: {2}]", lowAnnualRevenue, highAnnualRevenue, state);
        List<VirtualLeadDto> leads = crmClients.stream()
                .flatMap(crmClient -> crmClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state).stream())
                .sorted(comparing(VirtualLeadDto::getAnnualRevenue, nullsLast(reverseOrder())))
                .toList();
        geolocateLeads(leads);
        logger.log(INFO, "Returning {0} aggregated and geolocated leads", leads.size());
        return leads;
    }

    @Override
    @RequestMapping(value = "/leads/date", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<VirtualLeadDto> findLeadsByDate(
            @RequestParam(name = "start_date") String startDate,
            @RequestParam(name = "end_date") String endDate
    ) {
        logger.log(INFO, "Request received: findLeadsByDate [start: {0}, end: {1}]", startDate, endDate);
        List<VirtualLeadDto> leads = crmClients.stream()
                .flatMap(crmClient -> crmClient.findLeadsByDate(startDate, endDate).stream())
                .sorted(comparing(VirtualLeadDto::getAnnualRevenue, nullsLast(reverseOrder())))
                .toList();
        geolocateLeads(leads);
        logger.log(INFO, "Returning {0} leads found by date", leads.size());
        return leads;
    }

    private void geolocateLeads(List<VirtualLeadDto> leads) {
        if (leads.isEmpty()) {
            return;
        }

        logger.log(DEBUG, "Starting geolocation process for {0} leads", leads.size());
        for (VirtualLeadDto lead : leads) {
            try {
                lead.setGeographicPoint(localisationService.getGeographicPointFromLead(lead));
            } catch (Exception e) {
                logger.log(WARNING, "Failed to geolocate lead {0} {1}: {2}", lead.getFirstName(), lead.getLastName(), e.getMessage());
            }
        }
    }
}
