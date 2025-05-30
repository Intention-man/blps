package aviasales.ytsaurus;


import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

@Setter
@Getter
public class YTsaurusManagedConnectionFactory implements ManagedConnectionFactory {
    private String endpoint;
    private String token;

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) {
        return new YTsaurusConnectionFactory(this, cxManager);
    }
    @Override
    public Object createConnectionFactory() {
        return new YTsaurusConnectionFactory(this, null);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo info)
            throws ResourceException {
        return new YTsaurusManagedConnection(endpoint, token);
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() { return null; }

    @Override
    public void setLogWriter(PrintWriter out) { }

    @Override
    public boolean equals(Object other) { return super.equals(other); }
    @Override
    public int hashCode() { return super.hashCode(); }
}