package h10.rubric;

import h10.ListItem;
import h10.MySet;
import h10.VisitorSet;
import h10.utils.Links;
import h10.visitor.VisitorElement;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * This class is used to test the simple method implementation of the {@link MySet} interface.
 *
 * @author Nhan Huynh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SimpleTest {

    /**
     * The default comparator used for the testing.
     */
    public static final Comparator<Integer> DEFAULT_COMPARATOR = new Comparator<>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }

        @Override
        public String toString() {
            return "o1 <= o2";
        }
    };


    /**
     * The type of the class to be tested.
     */
    private @Nullable TypeLink type;

    /**
     * The method to be tested.
     */
    private @Nullable MethodLink method;

    /**
     * Initializes needed information for the testing before all tests.
     */
    @BeforeAll
    public void globalSetup() {
        type = Links.getTypeLink(getClassType());
        method = Links.getMethodLink(type, getMethodName());
    }

    /**
     * Returns the type of the class to be tested.
     *
     * @return the type of the class to be tested
     */
    public abstract Class<?> getClassType();

    /**
     * Returns the name of the method to be tested.
     *
     * @return the name of the method to be tested
     */
    public abstract String getMethodName();

    /**
     * Returns the type of the class to be tested.
     *
     * @return the type of the class to be tested
     */
    public TypeLink getType() {
        if (type == null) {
            throw new IllegalStateException("Type not initialized");
        }
        return type;
    }

    /**
     * Returns the method to be tested.
     *
     * @return the method to be tested
     */
    public MethodLink getMethod() {
        if (method == null) {
            throw new IllegalStateException("Method not initialized");
        }
        return method;
    }

    /**
     * Returns a converter used to map the given list elements and comperator to the corresponding set to test.
     *
     * @return a converter used to map the given list elements and comperator to the corresponding set to test
     */
    protected abstract BiFunction<
        ListItem<VisitorElement<Integer>>,
        Comparator<? super VisitorElement<Integer>>,
        MySet<VisitorElement<Integer>>
        > converter();

    /**
     * Maps the given list to a {@link VisitorSet} using the default comparator and converter.
     *
     * @param head the head of the list to be mapped
     * @return the mapped set
     */
    public VisitorSet<Integer> visit(ListItem<Integer> head) {
        return VisitorSet.of(head, DEFAULT_COMPARATOR, converter());
    }

    /**
     * Maps the given set to a {@link VisitorSet}.
     *
     * @param set the set to be mapped
     * @return the mapped set
     */
    public VisitorSet<Integer> visit(MySet<VisitorElement<Integer>> set) {
        return VisitorSet.of(set, converter());
    }

    /**
     * Returns a {@link Context.Builder} with the method to be tested as the subject.
     *
     * @return a {@link Context.Builder} with the method to be tested as the subject
     */
    public Context.Builder<?> contextBuilder() {
        return Assertions2.contextBuilder().subject(getMethod());
    }
}
