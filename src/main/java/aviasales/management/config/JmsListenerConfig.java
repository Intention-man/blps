package aviasales.management.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
@EnableJms
public class JmsListenerConfig {
    private final ConnectionFactory amqpConnectionFactory;

    public JmsListenerConfig(@Qualifier("amqpConnectionFactory") ConnectionFactory amqpConnectionFactory) {
        this.amqpConnectionFactory = amqpConnectionFactory;
    }

    @Bean("amqpJmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory amqpJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.amqpConnectionFactory);
        return factory;
    }
}
