package fr.univangers.mashup.leadmerger;

import fr.univangers.mashup.internalcrm.thrift.InvalidDateFormatException;
import fr.univangers.mashup.virtualcrm.dto.VirtualLeadDto;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.InternalCrmClient;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.InternalCrmClientFactory;
import fr.univangers.mashup.virtualcrm.serviceacces.crm.SalesForceCrmClient;

import java.util.List;

import static fr.univangers.mashup.leadmerger.utils.TypeConverter.toInternalLead;

public class LeadMerger {
    public static void main(String[] args) {
        System.out.println("Starting lead merge process...");

        List<VirtualLeadDto> virtualLeadDtos = new SalesForceCrmClient().retrieveAllLeads();
        int totalLeads = virtualLeadDtos.size();
        System.out.println(totalLeads + " leads retrieved from Salesforce.");

        InternalCrmClient internalCrmClient = InternalCrmClientFactory.getInternalCrmClient();
        int successCount = 0;

        for (int i = 0; i < totalLeads; i++) {
            VirtualLeadDto virtualLeadDto = virtualLeadDtos.get(i);
            try {
                System.out.print("[" + (i + 1) + "/" + totalLeads + "] Merging lead: " + virtualLeadDto.getFirstName() + " " + virtualLeadDto.getLastName() + "... ");
                internalCrmClient.addLead(toInternalLead(virtualLeadDto));
                System.out.println("DONE");
                successCount++;
            } catch (InvalidDateFormatException e) {
                System.out.println("FAILED (Invalid Date)");
                throw new RuntimeException("Invalid data error for lead " + virtualLeadDto.getLastName(), e);
            } catch (Exception e) {
                System.out.println("FAILED (Unexpected error: " + e.getMessage() + ")");
            }
        }

        System.out.println("Merge complete.");
        System.out.println("Successfully merged: " + successCount + "/" + totalLeads + " leads.");
    }
}
