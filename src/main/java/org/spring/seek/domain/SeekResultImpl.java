package org.spring.seek.domain;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * {@link SeekRequest} default implementation.
 *
 * @author Elliot Ball
 */
public class SeekResultImpl<T> implements SeekResult<T> {

  @NonNull
  private final List<T> result;

  private final Sort.Direction previousSortDirection;

  @Nullable
  private final Comparable<?> lastSeenId;

  public SeekResultImpl(@NonNull final List<T> result, final Sort.Direction previousSortDirection,
      final Comparable<?> lastSeenId) {
    this.result = result;
    this.previousSortDirection = previousSortDirection;
    this.lastSeenId = lastSeenId;
  }

  @Override
  public List<T> getResult() {
    return result;
  }

  @Override
  public boolean hasResults() {
    return result != null && result.size() > 0;
  }

  @Override
  public SeekRequest getNextPage(final int limit) {
    return new SeekRequest(this.lastSeenId, this.previousSortDirection, limit);
  }
}
