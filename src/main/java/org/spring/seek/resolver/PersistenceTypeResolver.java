package org.spring.seek.resolver;

import javax.persistence.metamodel.SingularAttribute;
import org.spring.seek.domain.SeekRequest;
import org.springframework.data.jpa.domain.Specification;

/**
 * Marker interface for {@link javax.persistence.metamodel.Type.PersistenceType} to {@link
 * Specification} resolvers. The provided 'seekFields' are the fields annotated {@link
 * org.spring.seek.annotation.SeekPaginationField} on a given entity.
 *
 * @author Elliot Ball
 */
public interface PersistenceTypeResolver {

  <T> Specification<T> toSpec(SingularAttribute<? super T, ?> seekField, SeekRequest seekRequest);
}
