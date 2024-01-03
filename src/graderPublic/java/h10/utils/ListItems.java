package h10.utils;

import com.google.common.collect.Streams;
import h10.ListItem;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A utility class for {@link ListItem}s which provides additional operations on lists.
 *
 * @author Nhan Huynh
 * @see ListItem
 */
public final class ListItems {

    /**
     * This class cannot be instantiated.
     */
    private ListItems() {
    }

    /**
     * Returns a {@link ListItem} containing the given elements.
     *
     * @param elements the elements to convert
     * @param <T>      the type of the elements in the list
     * @return a {@link ListItem} containing the given elements
     */
    @SafeVarargs
    public static <T> ListItem<T> of(T... elements) {
        return convert(List.of(elements));
    }

    /**
     * Converts the given elements to a {@link ListItem}.
     *
     * @param elements the elements to convert
     * @param <T>      the type of the elements in the list
     * @return a {@link ListItem} containing the given elements
     */
    public static <T> ListItem<T> convert(List<T> elements) {
        ListItem<T> head = null;
        ListItem<T> tail = null;
        for (T element : elements) {
            ListItem<T> item = new ListItem<>(element);
            if (head == null) {
                head = item;
            } else {
                tail.next = item;
            }
            tail = item;
        }
        return head;
    }

    /**
     * Returns an iterator of the elements in the given list item.
     *
     * @param head the list item to iterate
     * @param <T>  the type of the elements in the list item
     * @return an iterator of the elements in the given list item
     */
    public static <T> Iterator<T> iterator(ListItem<T> head) {
        return new Iterator<>() {
            private ListItem<T> current = head;

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
     * Returns a stream of the elements in the given list item.
     *
     * @param head the list item to stream
     * @param <T>  the type of the elements in the list item
     * @return a stream of the elements in the given list item
     */
    public static <T> Stream<T> stream(ListItem<T> head) {
        return Streams.stream(() -> iterator(head));
    }

    /**
     * Returns a {@link ListItem} which contains the elements in the given list item mapped by the given mapper.
     *
     * @param head   the list item to map
     * @param mapper the mapper to use
     * @param <T>    the type of the elements in the given list item
     * @param <R>    the type of the elements in the returned list item
     * @return a {@link ListItem} which contains the elements in the given list item mapped by the given mapper
     */
    public static <T, R> ListItem<R> map(ListItem<T> head, Function<? super T, ? extends R> mapper) {
        ListItem<R> newHead = null;
        ListItem<R> tail = null;
        for (ListItem<T> current = head; current != null; current = current.next) {
            ListItem<R> item = new ListItem<>(mapper.apply(current.key));
            if (newHead == null) {
                newHead = item;
            } else {
                tail.next = item;
            }
            tail = item;
        }
        return newHead;
    }
}
