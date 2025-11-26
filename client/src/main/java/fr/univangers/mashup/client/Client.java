package fr.univangers.mashup.client;

import fr.univangers.mashup.internalcrm.thrift.InternalCrmService;
import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.Calendar;

public class Client {
    public static void main(String[] args) {
        var client = getClient();
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();

            InternalLeadDto leadDto = new InternalLeadDto("LastName, FirstName", 100, "0612452356", "street", "49000", "Angers", "France", Calendar.getInstance().getTimeInMillis(), "Company", "State");
            client.addLead(leadDto);
            System.out.println("Add lead: " + leadDto);

            InternalLeadDto foundLeadDto = client.findLeads(0, 1_000, "State").get(0);
            System.out.println("Found lead: " + foundLeadDto);

            client.deleteLead(foundLeadDto);
            System.out.println("Delete lead: " + foundLeadDto);

            System.out.println("Leads: " + client.findLeads(0, 1_000, "State"));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    private static InternalCrmService.Client getClient() {
        try {
            TTransport transport = new THttpClient("http://localhost:8080/internalcrm/leads");
            TProtocol protocol = new TBinaryProtocol(transport);
            return new InternalCrmService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
