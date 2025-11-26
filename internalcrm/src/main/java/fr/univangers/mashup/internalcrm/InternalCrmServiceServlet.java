package fr.univangers.mashup.internalcrm;

import fr.univangers.mashup.internalcrm.thrift.InternalCrmService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;

@WebServlet("/leads")
public class InternalCrmServiceServlet extends HttpServlet {
    private final TProcessor processor = new InternalCrmService.Processor<InternalCrmService.Iface>(new InternalCrmServiceImpl());
    private final TProtocolFactory inProtocolFactory = new TBinaryProtocol.Factory();
    private final TProtocolFactory outProtocolFactory = new TBinaryProtocol.Factory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/x-thrift");
            TTransport transport = new TIOStreamTransport(req.getInputStream(), resp.getOutputStream());
            processor.process(inProtocolFactory.getProtocol(transport), outProtocolFactory.getProtocol(transport));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}
