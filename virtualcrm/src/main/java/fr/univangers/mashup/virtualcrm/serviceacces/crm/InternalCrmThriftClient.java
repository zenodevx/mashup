package fr.univangers.mashup.virtualcrm.serviceacces.crm;

import fr.univangers.mashup.internalcrm.thrift.InternalCrmService;
import fr.univangers.mashup.internalcrm.thrift.InternalLeadDto;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

import static java.lang.System.Logger.Level.*;
import static java.lang.System.out;
import static java.text.MessageFormat.format;

public class InternalCrmThriftClient implements InternalCrmClient {
    private static final System.Logger logger = System.getLogger(InternalCrmThriftClient.class.getSimpleName());
    public static final String INTERNAL_CRM_ENDPOINT_ADDRESS = "http://localhost:8080/internalcrm/leads";
    private final InternalCrmService.Client client = getClient();

    private static InternalCrmService.Client getClient() {
        try {
            TTransport transport = new THttpClient(INTERNAL_CRM_ENDPOINT_ADDRESS);
            TProtocol protocol = new TBinaryProtocol(transport);
            return new InternalCrmService.Client(protocol);
        } catch (TTransportException e) {
            out.println(format("Failed to initialize Thrift client transport: " + e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        logger.log(INFO, "Finding leads by revenue: [{0} - {1}] in state: {2}", lowAnnualRevenue, highAnnualRevenue, state);
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            List<InternalLeadDto> leads = client.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
            logger.log(DEBUG, "Successfully retrieved {0} leads by revenue", leads.size());
            return leads;
        } catch (TException e) {
            logger.log(ERROR, "Thrift error during findLeads call: " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InternalLeadDto> findLeadsByDate(String startDate, String endDate) {
        logger.log(INFO, "Finding leads between {0} and {1}", startDate, endDate);
        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            List<InternalLeadDto> leads = client.findLeadsByDate(startDate, endDate);
            logger.log(DEBUG, "Successfully retrieved {0} leads by date", leads.size());
            return leads;
        } catch (TException e) {
            logger.log(ERROR, "Thrift error during findLeadsByDate call: " + e);
            throw new RuntimeException(e);
        }
    }
}
