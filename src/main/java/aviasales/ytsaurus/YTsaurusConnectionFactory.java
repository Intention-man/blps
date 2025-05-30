package aviasales.ytsaurus;

import jakarta.resource.spi.ConnectionManager;

public class YTsaurusConnectionFactory {
    private final YTsaurusManagedConnectionFactory mcf;
    private final ConnectionManager cm;

    public YTsaurusConnectionFactory(YTsaurusManagedConnectionFactory mcf, ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    public YTsaurusConnection getConnection() throws Exception {
        return new YTsaurusConnectionImpl(mcf.getEndpoint(), mcf.getToken());
    }

    public void closeConnection(YTsaurusConnection connection){
        connection.close();
    }
}