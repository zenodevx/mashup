package fr.univangers.mashup.internalcrm.service;

import fr.univangers.mashup.internalcrm.thrift.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static fr.univangers.mashup.internalcrm.model.LeadModelFactory.getModel;
import static fr.univangers.mashup.internalcrm.utils.TypeConverter.toInternalLeadDtos;
import static fr.univangers.mashup.internalcrm.utils.TypeConverter.toLead;

public class InternalCrmServiceImpl implements InternalCrmService.Iface {
    private static final String datePattern = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidAnnualRevenueArgument {
        if (lowAnnualRevenue > highAnnualRevenue) {
            throw new InvalidAnnualRevenueArgument(lowAnnualRevenue, highAnnualRevenue);
        }
        return toInternalLeadDtos(getModel().findLeads(lowAnnualRevenue, highAnnualRevenue, state));
    }

    @Override
    public List<InternalLeadDto> findLeadsByDate(String startDate, String endDate) throws InvalidDateFormatException {
        Calendar startDateCalendar = Calendar.getInstance();
        try {
            startDateCalendar.setTime(dateFormat.parse(startDate));
        } catch (ParseException e) {
            throw new InvalidDateFormatException(startDate, datePattern);
        }

        Calendar endDateCalender = Calendar.getInstance();
        try {
            endDateCalender.setTime(dateFormat.parse(endDate));
        } catch (ParseException e) {
            throw new InvalidDateFormatException(endDate, datePattern);
        }

        return toInternalLeadDtos(getModel().findLeadsByDate(startDateCalendar, endDateCalender));
    }

    @Override
    public void addLead(InternalLeadDto leadDto) throws InvalidDateFormatException {
        getModel().addLead(toLead(leadDto));
    }

    @Override
    public void deleteLead(InternalLeadDto leadDto) throws InvalidDateFormatException, LeadNotFoundException {
        if (!getModel().deleteLead(toLead(leadDto))) {
            throw new LeadNotFoundException(leadDto);
        }
    }
}
