package h10.visitor;

import java.util.Objects;

/**
 * A {@link VisitorElement} is an element that can be visited and keeps track of the number of times it has been
 * visited.
 *
 * @param <T> the type of the element
 * @author Nhan Huynh
 */
public class VisitorElement<T> {

    /**
     * The wrapped element which can be visited.
     */
    private final T element;

    /**
     * The number of times the element has been visited.
     */
    private int visited = 0;

    /**
     * Creates a new {@link VisitorElement} with the given element.
     *
     * @param element the element to wrap
     */
    public VisitorElement(T element) {
        this.element = element;
    }

    /**
     * Returns the wrapped element without visiting it.
     *
     * @return the wrapped element
     */
    public T peek() {
        return element;
    }

    /**
     * Returns the wrapped element and visits it.
     *
     * @return the wrapped element
     */
    public T get() {
        visit();
        return element;
    }

    /**
     * Visits the wrapped element.
     */
    public void visit() {
        visited++;
    }

    /**
     * Returns the number of times the wrapped element has been visited.
     *
     * @return the number of times the wrapped element has been visited
     */
    public int visited() {
        return visited;
    }

    /**
     * Reduces the number of times the wrapped element has been visited by the given count.
     *
     * @param count the number of times to reduce the visited count by
     */
    public void reduce(int count) {
        visited = Math.max(0, visited - count);
    }

    /**
     * Resets the number of times the wrapped element has been visited to 0.
     */
    public void reset() {
        visited = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VisitorElement<?> that = (VisitorElement<?>) o;
        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, visited);
    }

    @Override
    public String toString() {
        return "{%s, visited=%d}".formatted(element, visited);
    }
}
