package aviasales.ytsaurus;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class YTsaurusManagedConnection implements ManagedConnection {
    private YTsaurusConnectionImpl connection;
    private List<ConnectionEventListener> listeners = new ArrayList<>();

    public YTsaurusManagedConnection(String endpoint, String token) {
        this.connection = new YTsaurusConnectionImpl(endpoint, token);
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) {
        return connection;
    }

    @Override
    public void destroy() throws ResourceException {}
    @Override
    public void cleanup() throws ResourceException {}
    @Override
    public void associateConnection(Object connection) throws ResourceException {}

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        listeners.add(listener);
    }
    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() { return null; }
    @Override
    public void setLogWriter(PrintWriter out) {}
    @Override
    public PrintWriter getLogWriter() { return null; }
}