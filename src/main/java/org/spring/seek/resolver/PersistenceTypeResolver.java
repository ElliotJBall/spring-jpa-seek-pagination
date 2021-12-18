package org.spring.seek.resolver;

import javax.persistence.metamodel.SingularAttribute;
import org.spring.seek.domain.SeekRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * Change me.
 *
 * @author Elliot Ball
 */
public interface PersistenceTypeResolver {

  <T> Specification<T> toSpec(SingularAttribute<? super T, ?> seekField, SeekRequest seekRequest);
}
