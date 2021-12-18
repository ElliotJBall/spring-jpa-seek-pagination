package org.spring.seek.domain;

import java.util.List;

/**
 * The result of a {@link SeekRequest}. Contains class holding the records retrieved from the
 * database.
 *
 * @author Elliot Ball
 */
public interface SeekResult<T> {

  List<T> getResult();

  boolean hasResults();

  SeekRequest getNextPage(int limit);
}
