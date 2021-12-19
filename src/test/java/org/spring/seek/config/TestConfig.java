package org.spring.seek.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.spring.seek.repository.impl.SeekPaginationRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Test configuration class.
 *
 * @author Elliot Ball
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.spring.seek.sample.repository", repositoryBaseClass = SeekPaginationRepositoryImpl.class)
public class TestConfig {

  @Profile(value = "default")
  @Bean
  public DataSource defaultDataSource() {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl(
        "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;");
    dataSource.setUsername("h2");
    dataSource.setPassword("password");
    return dataSource;
  }

  @Profile(value = "postgres")
  @Bean
  public DataSource postgresDataSource() {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:tc:postgresql:alpine:///testdb?TC_DAEMON=true");
    dataSource.setUsername("postgres");
    dataSource.setPassword("password");
    return dataSource;
  }

  @Profile(value = "mysql")
  @Bean
  public DataSource mysqlDataSource()
  {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:tc:mysql:latest:///testdb?TC_DAEMON=true");
    dataSource.setUsername("mysql");
    dataSource.setPassword("password");
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
    var em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("org.spring.seek.sample.domain");
    em.setJpaVendorAdapter(jpaVendorAdapter());
    em.setJpaProperties(additionalProperties());
    return em;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    var jpaVendorAdaptor = new HibernateJpaVendorAdapter();
    jpaVendorAdaptor.setGenerateDdl(true);
    jpaVendorAdaptor.setShowSql(true);
    return jpaVendorAdaptor;
  }

  @Bean
  public PlatformTransactionManager transactionManager(
      final EntityManagerFactory entityManagerFactory,
      final DataSource dataSource) {
    var transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    transactionManager.setDataSource(dataSource);

    return transactionManager;
  }

  private Properties additionalProperties() {
    var properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    return properties;
  }
}
