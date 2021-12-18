package org.spring.seek.sample.repository;

import org.spring.seek.repository.SeekPaginationRepository;
import org.spring.seek.sample.domain.Customer;
import org.springframework.stereotype.Repository;


/**
 * Reference {@link SeekPaginationRepository} implementation using the {@link Customer} JPA entity.
 *
 * @author Elliot Ball
 */
@Repository
public interface CustomerRepository extends SeekPaginationRepository<Customer, Long> {

}
