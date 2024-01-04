package h10.visitor;

import h10.ListItem;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A {@link Visitable} is a list data structure where the elements can be visited.
 *
 * @param <T> the type of the elements in the list
 * @author Nhan Huynh
 */
public interface Visitable<T> extends Iterable<VisitorElement<T>> {

    /**
     * Returns an iterator of the visitable elements.
     *
     * @return an iterator of the visitable elements.
     */
    @NotNull
    @Override
    Iterator<VisitorElement<T>> iterator();

    /**
     * Returns a stream of the visitable elements.
     *
     * @return a stream of the visitable elements
     */
    Stream<VisitorElement<T>> stream();

    /**
     * Returns an iterator of the visitable elements where we cannot change the visited state.
     *
     * @return an iterator of the visitable elements where we cannot change the visited state
     */
    Iterator<T> peekIterator();

    /**
     * Returns a stream of the visitable elements where we cannot change the visited state.
     *
     * @return a stream of the visitable elements where we cannot change the visited state
     */
    Stream<T> peekStream();

    /**
     * Returns an iterator of the underlying list items containing the visitable elements.
     *
     * @return an iterator of the underlying list items containing the visitable elements
     */
    Iterator<ListItem<VisitorElement<T>>> underlyingIterator();

    /**
     * Returns a stream of the underlying list items containing the visitable elements.
     *
     * @return a stream of the underlying list items containing the visitable elements
     */
    Stream<ListItem<VisitorElement<T>>> underlyingStream();
}
