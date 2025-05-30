package aviasales.ytsaurus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class YTsaurusJcaConfig {

    @Value("${ytsaurus.endpoint:localhost:8001}")
    private String endpoint;

    @Value("${ytsaurus.token:}")
    private String token;

    @Bean
    public YTsaurusConnectionFactory yTsaurusConnectionFactory() {
        YTsaurusManagedConnectionFactory mcf = new YTsaurusManagedConnectionFactory();
        mcf.setEndpoint(endpoint);
        mcf.setToken(token);
        return (YTsaurusConnectionFactory) mcf.createConnectionFactory();
    }

}