package h10.utils;

import java.util.stream.Stream;

/**
 * Marks a class as streamable.
 *
 * @param <T> the type of the elements in the stream
 * @author Nhan Huynh
 */
public interface Streamable<T> {

    /**
     * Returns a stream of the elements in this object.
     *
     * @return a stream of the elements in this object
     */
    Stream<T> stream();
}
