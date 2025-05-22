package com.example.prac.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.example.prac.repository",
                "com.example.prac.messaging.validator_node"
        },
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class AtomikosConfiguration {
    @Bean(initMethod = "init", destroyMethod = "close", name = "atomikosTransactionManager")
    public UserTransactionManager atomikosTransactionManager(
            @Value("${atomikos.log.dir}") String logDir,
            @Value("${atomikos.tm.unique.name}") String tmUniqueName) {

        System.setProperty("com.atomikos.icatch.log_base_dir", logDir);
        System.setProperty("com.atomikos.icatch.tm_unique_name", tmUniqueName);

        UserTransactionManager manager = new UserTransactionManager();
        manager.setForceShutdown(false);
        return manager;
    }

    @Bean(name = "atomikosUserTransaction")
    public UserTransaction userTransaction() throws SystemException {
        return new UserTransactionImp();
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("atomikosUserTransaction") UserTransaction userTransaction,
            @Qualifier("atomikosTransactionManager") TransactionManager atomikosTransactionManager
    ) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }


    @Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
    public com.atomikos.spring.AtomikosDataSourceBean dataSource(
            @Value("${atomikos.xa.db.user}") String dbUser,
            @Value("${atomikos.xa.db.password}") String dbPassword,
            @Value("${atomikos.xa.db.serverName}") String dbServerName,
            @Value("${atomikos.xa.db.portNumber}") String dbPortNumber,
            @Value("${atomikos.xa.db.databaseName}") String dbDatabaseName
    ) {
        com.atomikos.spring.AtomikosDataSourceBean dataSource = new com.atomikos.spring.AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("postgresXa_" + System.getProperty("atomikos.tm.unique.name", "defaultTm")); // Делаем имя ресурса уникальным
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

        Properties xaProps = new Properties();
        xaProps.put("user", dbUser);
        xaProps.put("password", dbPassword);
        xaProps.put("serverName", dbServerName);
        xaProps.put("portNumber", dbPortNumber);
        xaProps.put("databaseName", dbDatabaseName);
        dataSource.setXaProperties(xaProps);

        dataSource.setPoolSize(5);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        // adapter.setGenerateDdl(true); // Лучше управлять схемой через validate или миграции
        adapter.setDatabase(Database.POSTGRESQL);
        return adapter;
    }

    @Bean("entityManagerFactory")
    // entityManagerFactory должен создаваться ПОСЛЕ transactionManager и dataSource
    @DependsOn({"transactionManager", "dataSource"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource, // Инжектируем XA DataSource от Atomikos
            JpaVendorAdapter jpaVendorAdapter
            // PlatformTransactionManager transactionManager // Необязательно передавать, если используется стандартная JTA Platform
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJtaDataSource(dataSource); // Важно: используем setJtaDataSource для XA DataSource
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setPackagesToScan("com.example.prac.data", "com.example.prac.messaging");
        em.setJpaProperties(jpaProperties());
        return em;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        // Используем твой кастомный AtomikosJtaPlatform
        properties.put("hibernate.transaction.jta.platform", "com.example.prac.config.AtomikosJtaPlatform");
        properties.put("jakarta.persistence.transactionType", "JTA"); // Указывает JPA использовать JTA
        properties.put("hibernate.hbm2ddl.auto", "update"); // или validate
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); // Для некоторых версий Hibernate/PostgreSQL
        return properties;
    }

    @Bean
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
