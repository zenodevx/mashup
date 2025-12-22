package fr.univangers.mashup.virtualcrm.serviceacces;

import fr.univangers.mashup.virtualcrm.dto.GeographicPointDto;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

public interface GeoLocalisationServiceClient {
    GeographicPointDto getGeographicPointFromLead(VirtualLeadDto lead);
}
