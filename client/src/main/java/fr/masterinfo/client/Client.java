package fr.masterinfo.client;

import fr.masterinfo.internalcrm.thrift.InternalCrmService;
import fr.masterinfo.internalcrm.thrift.InternalLeadDto;
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
            client.addLead(new InternalLeadDto("A, B", 1000, "0102030405", "str", "49000", "angers", "france", Calendar.getInstance().getTimeInMillis(), "comp", "state"));
            System.out.println(client.findLeads(1000, 1000, "state"));
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
