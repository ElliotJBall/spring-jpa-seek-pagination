package org.spring.seek.repository;

import org.spring.seek.annotation.SeekPaginationField;
import org.spring.seek.domain.SeekRequest;
import org.spring.seek.domain.SeekResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * The Spring repository exposing the 'findAll' methods to perform the SEEK pagination. Extend this
 * repository with your own over the entity you wish to perform the SEEK pagination for.
 *
 * @author Elliot Ball
 */
@NoRepositoryBean
public interface SeekPaginationRepository<T, ID> extends Repository<T, ID> {

  /**
   * SEEK pagination allowing additional specifications/predicates and sorting to be applied.
   *
   * Note - Attempt to provide additional sorting on fields marked with the {@link
   * SeekPaginationField} will be ignored/overridden to preserve the SEEK pagination behaviour
   *
   * @param specification additional specifications/predicates
   * @param sort additional sorting
   * @param seekRequest the SEEK pagination request
   * @return the result of the SEEK request
   */
  SeekResult<T> findAll(@Nullable Specification<T> specification, @Nullable Sort sort,
      @NonNull SeekRequest seekRequest);

  /**
   * SEEK pagination allowing additional specifications/predicates to be applied.
   *
   * @param specification additional specifications/predicates
   * @param seekRequest the SEEK pagination request
   * @return the result of the SEEK request
   */
  SeekResult<T> findAll(@Nullable Specification<T> specification, @NonNull SeekRequest seekRequest);

  /**
   * SEEK pagination allowing additional sorting behaviour to be applied.
   *
   * Note - Attempt to provide additional sorting on fields marked with the {@link
   * SeekPaginationField} will be ignored/overridden to preserve the SEEK pagination behaviour
   *
   * @param sort additional sorting
   * @param seekRequest the SEEK pagination request
   * @return the result of the SEEK request
   */
  SeekResult<T> findAll(@Nullable Sort sort, @NonNull SeekRequest seekRequest);

  /**
   * SEEK pagination. Use this method if you do not require any additional filtering/sorting on the
   * data you're querying.
   *
   * @param seekRequest the SEEK pagination request
   * @return the result of the SEEK request
   */
  SeekResult<T> findAll(@NonNull SeekRequest seekRequest);
}
