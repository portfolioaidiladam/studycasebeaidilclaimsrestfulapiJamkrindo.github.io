package studycaseaplikasiapi.springbeaidil.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "studycaseaplikasiapi.springbeaidil.repository.penampungan",
        entityManagerFactoryRef = "penampunganEntityManager",
        transactionManagerRef = "penampunganTransactionManager"
)
public class PenampunganDbConfig {

    @Bean(name = "penampunganDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.penampungan")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "penampunganEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("studycaseaplikasiapi.springbeaidil.entity.penampungan");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "penampunganTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("penampunganEntityManager") LocalContainerEntityManagerFactoryBean entityManager) {
        return new JpaTransactionManager(entityManager.getObject());
    }
}