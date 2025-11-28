namespace java fr.univangers.mashup.internalcrm.thrift

struct InternalLeadDto {
     1: string name
     2: double annualRevenue
     3: string phone
     4: string street
     5: string postalCode
     6: string city
     7: string country
     8: string creationDate
     9: string company
    10: string state
}

exception InvalidAnnualRevenueArgument {
       1: string lowAnnualRevenue
       2: string highAnnualRevenue
}

exception InvalidDateFormatException {
    1: string date
    2: string expectedFormat
}

exception LeadNotFoundException {
    1: InternalLeadDto leadDto
}

service InternalCrmService {
    list<InternalLeadDto> findLeads(
        1: double lowAnnualRevenue
        2: double highAnnualRevenue
        3: string state
    ) throws (1: InvalidAnnualRevenueArgument e)

    list<InternalLeadDto> findLeadsByDate(
        1: string startDate
        2: string endDate
    ) throws (1: InvalidDateFormatException e)

    void addLead(1: InternalLeadDto leadDto) throws (1: InvalidDateFormatException e)

    void deleteLead(1: InternalLeadDto leadDto) throws (
        1: InvalidDateFormatException e
        2: LeadNotFoundException ee
    )
}
