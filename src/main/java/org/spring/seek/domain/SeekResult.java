package org.spring.seek.domain;

import java.util.List;

/**
 * Change me.
 *
 * @author Elliot Ball
 */
public interface SeekResult<T> {

  List<T> getResult();

  boolean hasResults();

  SeekRequest getNextPage(int limit);
}
