package fr.univangers.mashup.internalcrm.utils;

import fr.univangers.mashup.internalcrm.model.Lead;
import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;

import java.util.Calendar;
import java.util.List;

public class TypeConverter {
    public static Lead toLead(InternalLeadDto leadDto) throws InvalidDateFormatException {
        String[] split = leadDto.getName().split(", ");
        String lastName = split[0];
        String firstName = split[1];
        Calendar creationDate = DateUtils.parse(leadDto.getCreationDate());

        return new Lead(
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

    public static InternalLeadDto toInternalLeadDto(Lead lead) {
        String name = lead.getLastName() + ", " + lead.getFirstName();
        String creationDate = DateUtils.toString(lead.getCreationDate());

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

    public static List<InternalLeadDto> toInternalLeadDtos(List<Lead> leads) {
        return leads.stream()
                .map(TypeConverter::toInternalLeadDto)
                .toList();
    }
}
