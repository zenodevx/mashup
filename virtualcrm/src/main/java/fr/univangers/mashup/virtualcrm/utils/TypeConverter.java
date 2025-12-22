package fr.univangers.mashup.virtualcrm.utils;

import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;

import java.util.Calendar;
import java.util.List;

public class TypeConverter {
    public static VirtualLeadDto toVirtualLeadDto(InternalLeadDto lead) {
        String[] names = lead.getName().split(", ");
        String firstName = names[0];
        String lastName = names[1];
        Calendar creationDate = null;
        try {
            creationDate = DateUtils.parse(lead.getCreationDate());
        } catch (InvalidDateFormatException ignored) {
        }

        return new VirtualLeadDto(
                firstName,
                lastName,
                lead.getAnnualRevenue(),
                lead.getPhone(),
                lead.getStreet(),
                lead.getPostalCode(),
                lead.getCity(),
                lead.getCountry(),
                creationDate,
                lead.getCompany(),
                lead.getState(),
                null
        );
    }

    public static List<VirtualLeadDto> toVirtualLeadDtos(List<InternalLeadDto> leads) {
        return leads.stream()
                .map(TypeConverter::toVirtualLeadDto)
                .toList();
    }
}
