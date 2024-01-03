package h10.visitor;

import java.util.Comparator;

/**
 * A {@link Comparator} for {@link VisitorElement}s. Each time a comparison is made, the underlying comparator is used
 * and the visitation count of the elements is increased.
 *
 * @param underlying the underlying comparator to use
 * @param <T>        the type of the elements to compare
 * @author Nhan Huynh
 */
public record VisitorComparator<T>(Comparator<? super T> underlying) implements Comparator<VisitorElement<T>> {

    @Override
    public int compare(VisitorElement<T> o1, VisitorElement<T> o2) {
        return underlying.compare(o1.get(), o2.get());
    }

    @Override
    public String toString() {
        return underlying.toString();
    }
}
