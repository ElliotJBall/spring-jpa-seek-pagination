package org.spring.seek.resolver;

import javax.persistence.metamodel.SingularAttribute;
import org.spring.seek.domain.SeekRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * Change me.
 *
 * @author Elliot Ball
 */
public class BasicSpecificationResolver implements PersistenceTypeResolver {

  @Override
  @SuppressWarnings("unchecked")
  public <T> Specification<T> toSpec(final SingularAttribute<? super T, ?> seekField,
      final SeekRequest seekRequest) {
    return Specification.where(
        (root, query, criteriaBuilder) -> {
          if (seekRequest.getDirection().isAscending()) {
            return criteriaBuilder.greaterThan(root.get(seekField.getName()),
                (Comparable) seekRequest.getLastSeenId());
          } else {
            return criteriaBuilder
                .lessThan(root.get(seekField.getName()),
                    (Comparable) seekRequest.getLastSeenId());
          }
        });
  }
}
