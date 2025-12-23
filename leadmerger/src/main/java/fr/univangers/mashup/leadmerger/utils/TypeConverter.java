package fr.univangers.mashup.leadmerger.utils;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.text.SimpleDateFormat;

public class TypeConverter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static InternalLeadDto toInternalLead(VirtualLeadDto lead) {
        String name = lead.getLastName() + ", " + lead.getFirstName();
        String creationDate = dateFormat.format(lead.getCreationDate().getTime());

        return new InternalLeadDto(
                name,
                lead.getAnnualRevenue(),
                lead.getPhone(),
                lead.getStreet(),
                lead.getPostalCode(),
                lead.getCity(),
                lead.getCountry(),
                creationDate,
                lead.getCompany(),
                lead.getState()
        );
    }
}
