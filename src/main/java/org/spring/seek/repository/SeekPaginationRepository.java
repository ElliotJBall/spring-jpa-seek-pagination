package org.spring.seek.repository;

import org.spring.seek.domain.SeekRequest;
import org.spring.seek.domain.SeekResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Change me.
 *
 * @author Elliot Ball
 */
@NoRepositoryBean
public interface SeekPaginationRepository<T, ID> extends Repository<T, ID> {

  SeekResult<T> findAll(@Nullable Specification<T> specification, @Nullable Sort sort,
      @NonNull SeekRequest seekRequest);

  SeekResult<T> findAll(@Nullable Specification<T> specification, @NonNull SeekRequest seekRequest);

  SeekResult<T> findAll(@Nullable Sort sort, @NonNull SeekRequest seekRequest);

  SeekResult<T> findAll(@NonNull SeekRequest seekRequest);

}
