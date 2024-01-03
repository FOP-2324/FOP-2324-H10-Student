package h10;

import com.google.common.collect.Streams;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * A utility class for {@link MySet}s which provides additional operations on sets.
 *
 * @author Nhan Huynh
 * @see MySet
 */
public final class Sets {

    /**
     * This class cannot be instantiated.
     */
    private Sets() {
    }

    /**
     * Returns an iterator of the elements in the given set.
     *
     * @param set the set to iterate
     * @param <T> the type of the elements in the set
     * @return an iterator of the elements in the given set
     */
    public static <T> Iterator<T> iterator(MySet<T> set) {
        return new Iterator<>() {
            private ListItem<T> current = set.head;

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T element = current.key;
                current = current.next;
                return element;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }
        };
    }

    /**
     * Returns a stream of the elements in the given set.
     *
     * @param set the set to stream
     * @param <T> the type of the elements in the set
     * @return a stream of the elements in the given set
     */
    public static <T> Stream<T> stream(MySet<T> set) {
        return Streams.stream(() -> iterator(set));
    }
}
