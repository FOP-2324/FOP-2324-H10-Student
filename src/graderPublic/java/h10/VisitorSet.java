package h10;

import h10.utils.ListItems;
import h10.visitor.Visitable;
import h10.visitor.VisitorComparator;
import h10.visitor.VisitorElement;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link VisitorSet} is a {@link MySet} where the elements can be visited.
 *
 * <p>Please note that the visitation count of the elements is not preserved when converting a {@link MySet} to a
 * {@link VisitorSet} which is why you should use {@link VisitorSet#of(MySet, BiFunction)},
 * {@link VisitorSet#of(ListItem, Comparator, BiFunction)}  or {@link VisitorSet#convert(MySet, BiFunction)} if you
 * want to convert a {@link MySet} to a {@link VisitorSet}.
 *
 * @param <T> the type of the elements in the set
 * @author Nhan Huynh
 */
public class VisitorSet<T> extends DecoratorSet<VisitorElement<T>> implements Visitable<T> {

    /**
     * The converter used to convert the underlying set to a {@link VisitorSet}.
     */
    protected BiFunction<
        ListItem<VisitorElement<T>>,
        Comparator<? super VisitorElement<T>>,
        MySet<VisitorElement<T>>
        > converter;

    /**
     * Creates a new {@link VisitorSet} with the given underlying set.
     *
     * @param head      the head of the underlying set
     * @param cmp       the comparator to use
     * @param converter the converter used to convert the underlying set to a {@link VisitorSet}
     */
    protected VisitorSet(
        ListItem<T> head,
        Comparator<? super T> cmp,
        BiFunction<ListItem<VisitorElement<T>>, Comparator<? super VisitorElement<T>>, MySet<VisitorElement<T>>> converter
    ) {
        this(converter.apply(ListItems.map(head, VisitorElement::new), new VisitorComparator<>(cmp)), converter);
    }

    /**
     * Creates a new {@link VisitorSet} with the given underlying set.
     *
     * @param underlying the underlying set
     * @param converter  the converter used to convert the underlying set to a {@link VisitorSet}
     */
    protected VisitorSet(
        MySet<VisitorElement<T>> underlying,
        BiFunction<ListItem<VisitorElement<T>>, Comparator<? super VisitorElement<T>>, MySet<VisitorElement<T>>> converter
    ) {
        super(underlying);
        this.converter = converter;
    }

    /**
     * Creates a new {@link VisitorSet} with the given underlying set.
     *
     * @param underlying the underlying set
     * @param converter  the converter used to convert the underlying set to a {@link VisitorSet}
     * @param <T>        the type of the elements in the set
     * @return a new {@link VisitorSet} with the given underlying set
     */
    public static <T> VisitorSet<T> of(
        MySet<VisitorElement<T>> underlying,
        BiFunction<ListItem<VisitorElement<T>>, Comparator<? super VisitorElement<T>>, MySet<VisitorElement<T>>> converter
    ) {
        VisitorSet<T> wrapped = new VisitorSet<>(underlying, converter);
        VisitorFix<T> visitorFix = new VisitorFix<>(wrapped);
        visitorFix.apply();
        return wrapped;
    }

    /**
     * Creates a new {@link VisitorSet} with the given underlying set.
     *
     * @param head      the head of the underlying set
     * @param cmp       the comparator to use
     * @param converter the converter used to convert the underlying set to a {@link VisitorSet}
     * @param <T>       the type of the elements in the set
     * @return a new {@link VisitorSet} with the given underlying set
     */
    public static <T> VisitorSet<T> of(
        ListItem<T> head,
        Comparator<? super T> cmp,
        BiFunction<ListItem<VisitorElement<T>>, Comparator<? super VisitorElement<T>>, MySet<VisitorElement<T>>> converter
    ) {
        VisitorSet<T> wrapped = new VisitorSet<>(head, cmp, converter);
        VisitorFix<T> visitorFix = new VisitorFix<>(wrapped);
        visitorFix.apply();
        return wrapped;
    }

    /**
     * Creates a new {@link VisitorSet} with the given underlying set.
     *
     * @param underlying the underlying set
     * @param converter  the converter used to convert the underlying set to a {@link VisitorSet}
     * @param <T>        the type of the elements in the set
     * @return a new {@link VisitorSet} with the given underlying set
     */
    public static <T> VisitorSet<T> convert(
        MySet<T> underlying,
        BiFunction<ListItem<VisitorElement<T>>, Comparator<? super VisitorElement<T>>, MySet<VisitorElement<T>>> converter
    ) {
        VisitorSet<T> wrapped = new VisitorSet<>(underlying.head, underlying.cmp, converter);
        VisitorFix<T> visitorFix = new VisitorFix<>(wrapped);
        visitorFix.apply();
        return wrapped;
    }

    /**
     * Returns a deep copy of this {@link VisitorSet}.
     *
     * @return a deep copy of this {@link VisitorSet}
     */
    public MySet<VisitorElement<T>> deepCopy() {
        ListItem<VisitorElement<T>> newHead = ListItems.map(head, Function.identity());
        return converter.apply(newHead, cmp);
    }

    @Override
    public Iterator<T> peekIterator() {
        return VisitorSets.peekIterator(this);
    }

    @Override
    public Stream<T> peekStream() {
        return VisitorSets.peekStream(this);
    }

    @Override
    public Iterator<ListItem<VisitorElement<T>>> underlyingIterator() {
        return VisitorSets.underlyingIterator(this);
    }

    @Override
    public Stream<ListItem<VisitorElement<T>>> underlyingStream() {
        return VisitorSets.underlyingStream(this);
    }

    /**
     * A {@link VisitorFix} is a {@link VisitorSet} which fixes the visitation count of the elements in the set.
     *
     * @param <T> the type of the elements in the set
     */
    private static class VisitorFix<T> extends VisitorSet<T> {

        /**
         * The {@link VisitorSet} to fix.
         */
        private VisitorSet<T> toFix;

        /**
         * Creates a new {@link VisitorFix} with the given {@link VisitorSet} to fix.
         *
         * @param toFix the {@link VisitorSet} to fix
         */
        public VisitorFix(VisitorSet<T> toFix) {
            super(
                toFix.converter.apply(
                    ListItems.map(toFix.head, element -> new VisitorElement<>(element.peek())), toFix.cmp),
                toFix.converter
            );
            this.toFix = toFix;
        }

        /**
         * Fixes the visitation count of the elements in the set.
         */
        public void apply() {
            if (toFix == null) {
                return;
            }
            for (ListItem<VisitorElement<T>> current = toFix.head,
                 other = head; current != null && other != null;
                 current = current.next, other = other.next
            ) {
                current.key.reduce(other.key.visited());
            }
            toFix = null;
        }
    }
}
