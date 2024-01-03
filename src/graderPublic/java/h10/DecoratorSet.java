package h10;

import h10.utils.ListItems;
import h10.utils.Streamable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link MySet} that decorates another {@link MySet}. This class is useful for testing purposes where we want to
 * override certain methods of a {@link MySet} without having to override all methods.
 *
 * <p>All checks in the constructor are skipped since the underlying set must fulfill the properties anyway.
 *
 * @param <T> the type of the elements in the set
 * @author Nhan Huynh
 * @see MySet
 */
public class DecoratorSet<T> extends MySet<T> implements Iterable<T>, Streamable<T> {

    /**
     * The underlying set.
     */
    protected final MySet<T> underlying;

    /**
     * Creates a new {@link DecoratorSet} with the given underlying set.
     *
     * @param underlying the underlying set
     */
    public DecoratorSet(MySet<T> underlying) {
        super(underlying.head, underlying.cmp);
        this.underlying = underlying;
    }

    /**
     * Creates a new {@link DecoratorSet} with the given underlying set and comparator.
     *
     * @param underlying the underlying set
     * @param cmp        the comparator to use
     */
    public DecoratorSet(MySet<T> underlying, Comparator<? super T> cmp) {
        super(underlying.head, cmp);
        this.underlying = underlying;
    }

    @Override
    protected boolean isPairwiseDifferent(ListItem<T> head, Comparator<? super T> cmp) {
        // Do not check since underlying must fulfill the properties anyway
        return true;
    }

    @Override
    protected boolean isOrdered(ListItem<T> head, Comparator<? super T> cmp) {
        // Do not check since underlying must fulfill the properties anyway
        return true;
    }

    @Override
    public MySet<T> subset(Predicate<? super T> pred) {
        return delegate(s -> s.subset(pred));
    }

    protected <R> MySet<R> delegate(Function<MySet<T>, MySet<R>> f) {
        MySet<R> result = f.apply(underlying);

        // In-Place override head
        if (result instanceof MySetInPlace<R>) {
            head = underlying.head;
        }
        return result;
    }

    @Override
    public MySet<ListItem<T>> cartesianProduct(MySet<T> other) {
        return delegate(s -> s.cartesianProduct(other));
    }

    @Override
    public MySet<T> difference(MySet<T> other) {
        return delegate(s -> s.difference(other));
    }

    @Override
    protected MySet<T> intersectionListItems(ListItem<ListItem<T>> heads) {
        return delegate(s -> s.intersectionListItems(heads));
    }

    /**
     * Returns the head of the underlying set.
     *
     * @return the head of the underlying set
     */
    public ListItem<T> getHead() {
        return underlying.head;
    }

    /**
     * Sets the head of the underlying set.
     *
     * @param head the new head of the underlying set
     */
    public void setHead(ListItem<T> head) {
        this.head = head;
        this.underlying.head = head;
    }

    /**
     * Returns the comparator of the underlying set.
     *
     * @return the comparator of the underlying set
     */
    public Comparator<? super T> getCmp() {
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DecoratorSet<?> that = (DecoratorSet<?>) o;
        return Objects.equals(underlying, that.underlying);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlying);
    }

    @Override
    public Iterator<T> iterator() {
        return ListItems.iterator(head);
    }

    @Override
    public Stream<T> stream() {
        return ListItems.stream(head);
    }
}
