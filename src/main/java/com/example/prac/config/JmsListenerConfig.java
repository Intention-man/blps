package com.example.prac.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
@EnableJms
public class JmsListenerConfig {

    // ConnectionFactory для AMQP 1.0 (из AmqpProducerConfig)
    private final ConnectionFactory amqpConnectionFactory;

    public JmsListenerConfig(@Qualifier("amqpConnectionFactory") ConnectionFactory amqpConnectionFactory) {
        this.amqpConnectionFactory = amqpConnectionFactory;
    }

    @Bean("amqpJmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory amqpJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.amqpConnectionFactory);
        // factory.setConcurrency("1-5"); // Настройка параллелизма
        // factory.setSessionTransacted(true); // Если нужны транзакции на уровне JMS сессии
        // factory.setTransactionManager(...); // Если JTA с Atomikos
        return factory;
    }

//    @Bean
//    public JmsListenerContainerFactory<?> amqpJmsListenerContainerFactory(
//            ConnectionFactory connectionFactory,
//            PlatformTransactionManager transactionManager) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setTransactionManager(transactionManager);
//        factory.setSessionTransacted(true); // Важно для транзакций
//        return factory;
//    }
}
