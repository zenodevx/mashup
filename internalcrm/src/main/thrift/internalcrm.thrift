namespace java fr.masterinfo.internalcrm.thrift

struct InternalLeadDto {
     1: string name
     2: double annualRevenue
     3: string phone
     4: string street
     5: string postalCode
     6: string city
     7: string country
     8: i64 creationDateTimestamp
     9: string company
    10: string state
}

service InternalCrmService {
    list<InternalLeadDto> findLeads(
        1: double lowAnnualRevenue
        2: double highAnnualRevenue
        3: string state
    )

    list<InternalLeadDto> findLeadsByDate(
        1: i64 startDateTimestamp
        2: i64 endDateTimestamp
    )

    void addLead(1: InternalLeadDto leadDto)

    void deleteLead(1: InternalLeadDto leadDto)
}
