package fr.univangers.mashup.internalcrm.service;

import fr.univangers.mashup.internalcrm.thrift.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static fr.univangers.mashup.internalcrm.model.LeadModelFactory.getModel;
import static fr.univangers.mashup.internalcrm.utils.TypeConverter.toInternalLeadDtos;
import static fr.univangers.mashup.internalcrm.utils.TypeConverter.toLead;
import static java.lang.System.Logger.Level.*;

public class InternalCrmServiceImpl implements InternalCrmService.Iface {
    private static final System.Logger logger = System.getLogger(InternalCrmServiceImpl.class.getSimpleName());
    private static final String datePattern = "yyyy-MM-dd";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidAnnualRevenueArgument {
        logger.log(INFO, "Searching leads with revenue between {0} and {1} (state: {2})", lowAnnualRevenue, highAnnualRevenue, state);
        if (lowAnnualRevenue > highAnnualRevenue) {
            logger.log(WARNING, "Invalid revenue range requested: low={0} > high={1}", lowAnnualRevenue, highAnnualRevenue);
            throw new InvalidAnnualRevenueArgument(lowAnnualRevenue, highAnnualRevenue);
        }
        return toInternalLeadDtos(getModel().findLeads(lowAnnualRevenue, highAnnualRevenue, state));
    }

    @Override
    public List<InternalLeadDto> findLeadsByDate(String startDate, String endDate) throws InvalidDateFormatException {
        logger.log(INFO, "Searching leads from {0} to {1}", startDate, endDate);
        Calendar startDateCalendar = Calendar.getInstance();
        try {
            startDateCalendar.setTime(dateFormat.parse(startDate));
        } catch (ParseException e) {
            logger.log(ERROR, "Invalid start date format: {0}", startDate);
            throw new InvalidDateFormatException(startDate, datePattern);
        }

        Calendar endDateCalender = Calendar.getInstance();
        try {
            endDateCalender.setTime(dateFormat.parse(endDate));
        } catch (ParseException e) {
            logger.log(ERROR, "Invalid end date format: {0}", endDate);
            throw new InvalidDateFormatException(endDate, datePattern);
        }

        return toInternalLeadDtos(getModel().findLeadsByDate(startDateCalendar, endDateCalender));
    }

    @Override
    public void addLead(InternalLeadDto leadDto) throws InvalidDateFormatException {
        logger.log(INFO, "Adding new lead: {0}", leadDto.getName());
        getModel().addLead(toLead(leadDto));
        logger.log(DEBUG, "Lead successfully added");
    }

    @Override
    public void deleteLead(InternalLeadDto leadDto) throws InvalidDateFormatException, LeadNotFoundException {
        logger.log(INFO, "Attempting to delete lead: {0}", leadDto.getName());
        if (!getModel().deleteLead(toLead(leadDto))) {
            logger.log(WARNING, "Delete failed: lead not found");
            throw new LeadNotFoundException(leadDto);
        }
        logger.log(DEBUG, "Lead successfully deleted");
    }
}
