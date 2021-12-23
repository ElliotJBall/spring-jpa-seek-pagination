package org.spring.seek.domain;

import java.util.List;
import org.springframework.data.util.Streamable;

/**
 * The result of a {@link SeekRequest}. Contains class holding the records retrieved from the
 * database.
 *
 * @author Elliot Ball
 */
public interface SeekResult<T> {

  /**
   * Returns the content of the SEEK result content as a list.
   *
   * @return the content
   */
  List<T> getContent();

  /**
   * Returns the total number of elements returned by the {@link SeekResult}.
   *
   * @return the total number of elements in this {@link SeekResult}
   */
  long getTotalElements();

  /**
   * Returns true if the content returned from tge SEEK request has any content.
   *
   * @return true if the content list has any elements, else false
   */
  boolean hasResults();

  /**
   * Fetches the next 'page' of content with a given limit. Automatically determines the {@link
   * SeekRequest#getLastSeenId()} from the {@link SeekResult#getContent()}
   *
   * @param limit the maximum number of records to fetch
   * @return a new {@link SeekRequest} to fetch the next 'page'
   */
  SeekRequest getNextPage(int limit);
}
