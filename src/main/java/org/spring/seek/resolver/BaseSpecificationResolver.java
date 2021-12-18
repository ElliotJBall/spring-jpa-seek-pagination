package org.spring.seek.resolver;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;
import javax.persistence.metamodel.SingularAttribute;
import org.spring.seek.domain.SeekRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * Base specification resolver. Converts any fields annotated with {@link
 * org.spring.seek.annotation.SeekPaginationField} into the Spring {@link Specification} to generate
 * the SEEK query.
 *
 * @author Elliot Ball
 */
public class BaseSpecificationResolver {

  public static <T> Specification<T> resolve(
      final Map<Field, SingularAttribute<? super T, ?>> seekFields, final SeekRequest seekRequest) {
    // FIXME: How do we safely create the embedded specification without the true conjunction?
    Specification<T> embeddedSpec = Specification
        .where((innerRoot, innerQuery, innerCriteriaBuilder) -> innerCriteriaBuilder
            .conjunction());

    for (var seekField : seekFields.values()) {
      embeddedSpec = embeddedSpec.and(fieldToSpec(seekField, seekRequest));
    }

    return embeddedSpec;
  }

  private static <T> Specification<T> fieldToSpec(
      @NonNull final SingularAttribute<? super T, ?> seekField, final SeekRequest seekRequest) {
    switch (seekField.getType().getPersistenceType()) {
      case BASIC:
        return new BasicSpecificationResolver().toSpec(seekField, seekRequest);
      case EMBEDDABLE:
      default:
        throw new IllegalArgumentException(MessageFormat.format(
            "Attribute persistence type: {0} not supported by the Seek pagination repository",
            seekField.getType().getPersistenceType()));
    }
  }
}
