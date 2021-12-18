package org.spring.seek.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * A {@link SeekRequest} allows you to construct a new Seek request based on the last seen id, the
 * sort direction to apply to the {@link org.spring.seek.annotation.SeekPaginationField} and a limit
 * on the number of results.
 *
 * @author Elliot Ball
 */
public class SeekRequest {

  private final Comparable<?> lastSeenId;
  private final Sort.Direction direction;
  private final int limit;

  public SeekRequest(@Nullable final Comparable<?> lastSeenId,
      @Nullable final Sort.Direction direction, final int limit) {
    Assert.isTrue(limit > 0L, "The limit must be greater than zero!");

    this.lastSeenId = lastSeenId;
    this.direction = direction == null ? Direction.ASC : direction;
    this.limit = limit;
  }

  public SeekRequest(final Sort.Direction direction, final int limit) {
    this(null, direction, limit);
  }

  public SeekRequest(final int limit) {
    this(null, null, limit);
  }


  public Comparable<?> getLastSeenId() {
    return lastSeenId;
  }

  public Direction getDirection() {
    return direction;
  }

  public int getLimit() {
    return limit;
  }
}
