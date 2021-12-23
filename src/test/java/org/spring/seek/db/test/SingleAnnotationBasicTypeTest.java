package org.spring.seek.db.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicLong;
import org.spring.seek.config.TestConfig;
import org.spring.seek.domain.SeekRequest;
import org.spring.seek.repository.SeekPaginationRepository;
import org.spring.seek.sample.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;

/**
 * Test implementation of the {@link SeekPaginationRepository} where the table under test makes use
 * of a single primary key.
 *
 * @author Elliot Ball
 */
@SpringBootTest(classes = TestConfig.class)
@Sql(scripts = {"classpath:truncate.sql", "classpath:customer-sample-data.sql"})
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
      assertThat(seekResult.getContent().size()).isEqualTo(10);

      seekResult.getContent().forEach(customer -> {
        assertThat(customer.getId()).isEqualTo(lastSeenCustomerId.incrementAndGet());
      });

      seekResult = repository.findAll(seekResult.getNextPage(10));
    } while (seekResult.hasResults());

    assertThat(lastSeenCustomerId.longValue()).isEqualTo(100L);
  }

  @Test
  void testCanSeekThroughAllCustomerRecordsInDescendingOrder() {
    var lastSeenCustomerId = new AtomicLong(100L);
    var seekRequest = new SeekRequest(Direction.DESC, 10);

    var seekResult = repository.findAll(seekRequest);

    do {
      assertThat(seekResult).isNotNull();
      assertThat(seekResult.getContent().size()).isEqualTo(10);

      seekResult.getContent().forEach(customer -> {
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
    assertThat(seekResult.getContent().size()).isEqualTo(100);

    seekResult = repository.findAll(seekResult.getNextPage(largeLimit));
    assertThat(seekResult).isNotNull();
    assertThat(seekResult.getContent().size()).isEqualTo(0);
  }
}
