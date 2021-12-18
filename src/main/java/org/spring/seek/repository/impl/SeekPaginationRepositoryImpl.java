package org.spring.seek.repository.impl;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Predicate;

import javax.persistence.metamodel.SingularAttribute;
import org.spring.seek.annotation.SeekPaginationField;
import org.spring.seek.domain.SeekResult;
import org.spring.seek.domain.SeekResultImpl;
import org.spring.seek.repository.SeekPaginationRepository;
import org.spring.seek.domain.SeekRequest;
import org.spring.seek.resolver.BaseSpecificationResolver;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * {@link SeekPaginationRepository} implementation class.
 *
 * @author Elliot Ball
 */
public class SeekPaginationRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID>
    implements SeekPaginationRepository<T, ID> {

  private static final Logger LOGGER = Logger
      .getLogger(SeekPaginationRepositoryImpl.class.getName());

  private final EntityManager entityManager;
  private final Map<Field, SingularAttribute<? super T, ?>> fieldToAttribute;


  public SeekPaginationRepositoryImpl(final JpaEntityInformation<T, ID> entityInformation,
      final EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;

    var domainType = entityInformation.getJavaType();
    var managedType = entityManager.getMetamodel().managedType(domainType);

    fieldToAttribute = new HashMap<>();

    for (Field field : entityInformation.getJavaType().getDeclaredFields()) {
      if (field.isAnnotationPresent(SeekPaginationField.class)) {
        fieldToAttribute.put(field, managedType.getSingularAttribute(field.getName()));
        break;
      }
    }

    Assert.notEmpty(fieldToAttribute, MessageFormat
        .format("Could not find SeekPaginationField annotation on entity: {0}", domainType));
  }

  @Override
  public SeekResult<T> findAll(@Nullable final Specification<T> specification,
      @Nullable final Sort sort,
      @NonNull final SeekRequest seekRequest) {
    var fullSpecification = buildSpecification(seekRequest, specification);
    var fullSort = buildSort(sort, seekRequest);

    var domainClass = getDomainClass();
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(domainClass);

    Assert.notNull(domainClass, "Domain class must not be null!");
    Assert.notNull(criteriaQuery, "CriteriaQuery must not be null!");

    var root = criteriaQuery.from(domainClass);
    criteriaQuery.select(root);

    if (fullSpecification != null) {
      Predicate predicate = fullSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);
      criteriaQuery.where(predicate);
    }

    criteriaQuery.orderBy(toOrders(fullSort, root, criteriaBuilder));

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery.setMaxResults(seekRequest.getLimit());

    if (typedQuery.getResultList() == null || typedQuery.getResultList().size() == 0) {
      return new SeekResultImpl<>(typedQuery.getResultList(), seekRequest.getDirection(),
          null);
    } else {
      var lastSeenId = getSeekIdFromEntity(
          typedQuery.getResultList().get(typedQuery.getResultList().size() - 1));

      return new SeekResultImpl<>(typedQuery.getResultList(), seekRequest.getDirection(),
          lastSeenId);
    }
  }

  @Override
  public SeekResult<T> findAll(final Specification<T> specification,
      final SeekRequest seekRequest) {
    return this.findAll(specification, null, seekRequest);
  }

  @Override
  public SeekResult<T> findAll(final Sort sort, final SeekRequest seekRequest) {
    return this.findAll(null, sort, seekRequest);
  }

  @Override
  public SeekResult<T> findAll(final SeekRequest seekRequest) {
    return this.findAll(null, null, seekRequest);
  }

  private Comparable<?> getSeekIdFromEntity(final T entity) {
    for (Field field : entity.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(SeekPaginationField.class)) {
        try {
          field.setAccessible(true);
          return (Comparable<?>) field.get(entity);
        } catch (IllegalAccessException e) {
          LOGGER.severe(MessageFormat
              .format("Failed to retrieve seek annotated field from entity: {}",
                  entity.getClass().getName()));
        }
      }
    }
    return null;
  }

  private Specification<T> buildSpecification(@NonNull final SeekRequest seekRequest,
      @Nullable final Specification<T> additionalSpecification) {
    // A request for the first page does not provide a 'lastSeenId'
    if (seekRequest.getLastSeenId() == null) {
      return additionalSpecification;
    }

    return BaseSpecificationResolver.resolve(fieldToAttribute, seekRequest)
        .and(additionalSpecification);
  }

  private Sort buildSort(@Nullable final Sort sort, @NonNull final SeekRequest seekRequest) {
    Sort fullSort;
    if (sort == null || sort.isUnsorted()) {
      fullSort = Sort.by(fieldToAttribute.values().stream().map(SingularAttribute::getName)
          .map(attributeName -> new Order(seekRequest.getDirection(), attributeName)).collect(
              Collectors.toList()));
    } else {
      // Check for sort on our designated seek field, if it's present use it, else default to descending order
      // Seek pagination requires we apply the sort on the same we're applying the predicate against so we need
      // to ignore the user if they attempt to query for unsorted records
      fullSort = sort;

      for (var seekSortField : fieldToAttribute.values().stream()
          .map(SingularAttribute::getName)
          .collect(Collectors.toList())) {
        if (sort.getOrderFor(seekSortField) == null) {
          fullSort = sort.and(Sort.by(seekRequest.getDirection(), seekSortField));
        }
      }
    }

    return fullSort;
  }
}
