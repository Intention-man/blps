package com.example.prac.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.spring.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
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
        basePackages = "com.example.prac.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class AtomikosConfiguration {

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() {
        UserTransactionManager manager = new UserTransactionManager();
        manager.setForceShutdown(false);
        return manager;
    }

    @Bean
    public UserTransaction userTransaction() throws SystemException {
        UserTransactionImp userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(300);
        return userTransaction;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            UserTransaction userTransaction,
            UserTransactionManager atomikosTransactionManager
    ) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean dataSource() {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("postgresXa");
        dataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        dataSource.setXaProperties(xaProperties());
        dataSource.setPoolSize(5);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.POSTGRESQL);
        return adapter;
    }

    @Bean
    @DependsOn({"atomikosTransactionManager", "userTransaction"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setPackagesToScan("com.example.prac.data");
        em.setJpaProperties(jpaProperties());
        return em;
    }

    private Properties xaProperties() {
        Properties props = new Properties();
        props.put("user", "s367044");
        props.put("password", "rkemWfU26OYiwbkD");
        props.put("serverName", "localhost");
        props.put("portNumber", "25432");
        props.put("databaseName", "studs");
        return props;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("jakarta.persistence.transactionType", "JTA");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        return properties;
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}