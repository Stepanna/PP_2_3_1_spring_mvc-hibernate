package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "web")
@EnableTransactionManagement
@PropertySource(value = "classpath:db.properties")
public class HibernateConfig {
    @Autowired
    private Environment envitonment;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(Objects.requireNonNull(envitonment.getProperty("jdbc.driver")));
        dataSource.setUrl(envitonment.getProperty("jdbc.url"));
        dataSource.setUsername(envitonment.getProperty("jdbc.username"));
        dataSource.setPassword(envitonment.getProperty("jdbc.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(getDataSource());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(getProperties());
        factoryBean.setPackagesToScan("web");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getEntityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", envitonment.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", envitonment.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", envitonment.getProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

}
