package org.spring.seek.db.vendor.h2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.seek.config.H2Config;
import org.spring.seek.config.TestConfig;
import org.spring.seek.domain.SeekRequest;
import org.spring.seek.repository.SeekPaginationRepository;
import org.spring.seek.sample.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Test implementation of the {@link SeekPaginationRepository} where the table under test makes use
 * of a single primary key.
 *
 * @author Elliot Ball
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("h2")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestConfig.class,
    H2Config.class})
@Sql(scripts = {"classpath:customer-sample-data.sql"})
public class SingleAnnotationBasicTypeTest {

  @Autowired
  private CustomerRepository repository;

  @Test
  void testCanSeekThroughAllCustomerRecordsWithDefaultAscendingOrder() {
    var lastSeenCustomerId = new AtomicLong(0L);
    var seekRequest = new SeekRequest(10);

    var seekResult = repository.findAll(seekRequest);

    do {
      assertThat(seekResult).isNotNull();
      assertThat(seekResult.getResult().size()).isEqualTo(10);

      seekResult.getResult().forEach(customer -> {
        assertThat(customer.getId()).isEqualTo(lastSeenCustomerId.incrementAndGet());
      });

      seekResult = repository.findAll(seekResult.getNextPage(10));
    } while (seekResult.hasResults());

    // We'll increment one past the full result set size
    assertThat(lastSeenCustomerId.longValue()).isEqualTo(100L);
  }

  @Test
  void testCanSeekThroughAllCustomerRecordsInDescendingOrder() {
    var lastSeenCustomerId = new AtomicLong(100L);
    var seekRequest = new SeekRequest(Direction.DESC, 10);

    var seekResult = repository.findAll(seekRequest);

    do {
      assertThat(seekResult).isNotNull();
      assertThat(seekResult.getResult().size()).isEqualTo(10);

      seekResult.getResult().forEach(customer -> {
        assertThat(customer.getId()).isEqualTo(lastSeenCustomerId.getAndDecrement());
      });

      seekResult = repository.findAll(seekResult.getNextPage(10));
    } while (seekResult.hasResults());

    assertThat(lastSeenCustomerId.longValue()).isEqualTo(0L);
  }

  @Test
  void testCanSeekThroughAllCustomerRecordsWithLargeLimit() {
    var largeLimit = 100_000;

    var seekRequest = new SeekRequest(Direction.DESC, largeLimit);
    var seekResult = repository.findAll(seekRequest);

    assertThat(seekResult).isNotNull();
    assertThat(seekResult.getResult().size()).isEqualTo(100);

    seekResult = repository.findAll(seekResult.getNextPage(largeLimit));
    assertThat(seekResult).isNotNull();
    assertThat(seekResult.getResult().size()).isEqualTo(0);
  }
}