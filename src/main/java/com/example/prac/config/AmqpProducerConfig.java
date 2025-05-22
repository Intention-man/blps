package com.example.prac.config;


import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class AmqpProducerConfig {

    @Value("${spring.activemq.broker-url-amqp:amqp://localhost:5672}") // URL для AMQP
    private String amqpBrokerUrl;

    @Value("${spring.activemq.user:admin}")
    private String user;

    @Value("${spring.activemq.password:admin}")
    private String password;

    @Bean("amqpConnectionFactory")
    public ConnectionFactory amqpConnectionFactory() {
        org.apache.qpid.jms.JmsConnectionFactory qpidFactory = new org.apache.qpid.jms.JmsConnectionFactory();
        qpidFactory.setRemoteURI(amqpBrokerUrl);
        qpidFactory.setUsername(user);
        qpidFactory.setPassword(password);
        return qpidFactory;
    }

    @Bean("amqpJmsTemplate")
    public JmsTemplate amqpJmsTemplate(ConnectionFactory amqpConnectionFactory) {
        // Явно указываем, что используем наш AMQP ConnectionFactory
        return new JmsTemplate(amqpConnectionFactory);
    }
}
