package org.spring.seek.config;

import javax.sql.DataSource;
import org.spring.seek.repository.impl.SeekPaginationRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Change me.
 *
 * @author Elliot Ball
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.spring.seek.*", repositoryBaseClass = SeekPaginationRepositoryImpl.class)
@EntityScan(basePackages = "org.spring.seek.*")
public class TestConfig {

  @Autowired
  public DataSource dataSource;
}
