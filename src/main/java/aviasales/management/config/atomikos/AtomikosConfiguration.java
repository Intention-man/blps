package aviasales.management.config.atomikos;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.spring.AtomikosDataSourceBean;
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
                "aviasales"
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
        JtaTransactionManager manager = new JtaTransactionManager(userTransaction, atomikosTransactionManager);
        manager.setAllowCustomIsolationLevels(true);
        return manager;
    }


    @Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
    public AtomikosDataSourceBean dataSource(
            @Value("${atomikos.xa.db.user}") String dbUser,
            @Value("${atomikos.xa.db.password}") String dbPassword,
            @Value("${atomikos.xa.db.serverName}") String dbServerName,
            @Value("${atomikos.xa.db.portNumber}") String dbPortNumber,
            @Value("${atomikos.xa.db.databaseName}") String dbDatabaseName
    ) {
        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setUniqueResourceName("postgresXa_" + System.getProperty("atomikos.tm.unique.name", "defaultTm"));
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
        adapter.setDatabase(Database.POSTGRESQL);
        return adapter;
    }

    @Bean("entityManagerFactory")
    @DependsOn({"transactionManager", "dataSource"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJtaDataSource(dataSource);
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setPackagesToScan("aviasales");
        em.setJpaProperties(jpaProperties());
        return em;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
//        properties.put("hibernate.transaction.jta.platform", "aviasales.management.config.AtomikosJtaPlatform");
        properties.put("jakarta.persistence.transactionType", "JTA");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        return properties;
    }

    @Bean
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
