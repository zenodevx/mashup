package fr.univangers.mashup.internalcrm.service;

import fr.univangers.mashup.internalcrm.model.InternalLead;
import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;

import java.util.Calendar;
import java.util.List;

public class LeadToInternalLeadDtoConverter {
    public static InternalLead toInternalLead(InternalLeadDto leadDto) {
        String[] split = leadDto.getName().split(", ");
        String lastName = split[0];
        String firstName = split[1];
        Calendar creationDate = Calendar.getInstance();
        creationDate.setTimeInMillis(leadDto.getCreationDateTimestamp());

        return new InternalLead(
                firstName,
                lastName,
                leadDto.getAnnualRevenue(),
                leadDto.getPhone(),
                leadDto.getStreet(),
                leadDto.getPostalCode(),
                leadDto.getCity(),
                leadDto.getCountry(),
                creationDate,
                leadDto.getCompany(),
                leadDto.getState()
        );
    }

    public static InternalLeadDto toInternalLeadDto(InternalLead lead) {
        String name = lead.getLastName() + ", " + lead.getFirstName();
        long creationDateTimestamp = lead.getCreationDate().getTimeInMillis();

        return new InternalLeadDto(
                name,
                lead.getAnnualRevenue(),
                lead.getPhone(),
                lead.getStreet(),
                lead.getPostalCode(),
                lead.getCity(),
                lead.getCountry(),
                creationDateTimestamp,
                lead.getCompany(),
                lead.getState()
        );
    }

    public static List<InternalLeadDto> toInternalLeadDtos(List<InternalLead> leads) {
        return leads.stream()
                .map(LeadToInternalLeadDtoConverter::toInternalLeadDto)
                .toList();
    }
}
