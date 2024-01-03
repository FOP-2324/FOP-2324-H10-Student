package h10.rubric;

import h10.ListItem;
import h10.MySet;
import h10.VisitorSet;
import h10.utils.ListItems;
import h10.visitor.VisitorElement;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This class is used to test the complex method implementation of the {@link MySet} interface.
 *
 * @author Nhan Huynh
 */
public abstract class ComplexTest extends SimpleTest {

    /**
     * Returns the operation to be tested.
     *
     * @return the operation to be tested
     */
    protected abstract BiFunction<VisitorSet<Integer>, VisitorSet<Integer>, MySet<VisitorElement<Integer>>> operation();

    /**
     * The tested source set.
     */
    protected @Nullable VisitorSet<Integer> source;

    /**
     * The result of the method call.
     */
    protected @Nullable VisitorSet<Integer> result;

    /**
     * The context builder for the test.
     */
    protected @Nullable Context.Builder<?> context;

    @AfterEach
    public void tearDown() {
        assertRequirement();
    }

    /**
     * Asserts the requirement of the test.
     */
    public abstract void assertRequirement();

    /**
     * Sets the source set, result set, and context builder for the test to validate.
     *
     * @param source  the source set
     * @param result  the result set
     * @param context the context builder
     */
    protected void afterCheck(VisitorSet<Integer> source, VisitorSet<Integer> result, Context.Builder<?> context) {
        this.source = source;
        this.result = result;
        this.context = context;
    }

    /**
     * Tests whether the result of the operation is correct.
     *
     * @param sourceHead   the head of the source set
     * @param otherHead    the head of the other set
     * @param expectedHead the head of the expected set
     */
    protected void assertEqualElements(
        ListItem<Integer> sourceHead,
        ListItem<Integer> otherHead,
        ListItem<Integer> expectedHead
    ) {
        VisitorSet<Integer> source = visit(sourceHead);
        VisitorSet<Integer> other = visit(otherHead);
        VisitorSet<Integer> expected = visit(expectedHead);
        Context.Builder<?> builder = contextBuilder().add("Source", source.toString())
            .add("Other", other.toString())
            .add("Expected", expected.toString());

        VisitorSet<Integer> result = visit(operation().apply(source, other));
        builder.add("Result", result.toString());
        Assertions2.assertEquals(expected, result, builder.build(),
            r -> "Expected set %s, but given %s".formatted(expected, result));

        afterCheck(source, result, builder);
    }

    public void testNotAddElements(
        ListItem<Integer> sourceHead,
        ListItem<Integer> otherHead,
        ListItem<Integer> expectedHead
    ) {
        assertEqualElements(sourceHead, otherHead, expectedHead);
    }

    public void testXSmallerY(
        ListItem<Integer> sourceHead,
        ListItem<Integer> otherHead,
        Integer[] sourceVisitation,
        Integer[] otherVisitation
    ) {
        assertPointers(sourceHead, otherHead, sourceVisitation, otherVisitation);
    }

    protected void assertPointers(
        ListItem<Integer> sourceHead,
        ListItem<Integer> otherHead,
        Integer[] sourceVisitation,
        Integer[] otherVisitation
    ) {
        VisitorSet<Integer> source = visit((ListItem<Integer>) null);
        source.setHead(ListItems.map(sourceHead, VisitorElement::new));
        VisitorSet<Integer> other = visit((ListItem<Integer>) null);
        other.setHead(ListItems.map(otherHead, VisitorElement::new));

        // Used for in-place check
        VisitorSet<Integer> sourceCopy = visit(source.deepCopy());
        VisitorSet<Integer> otherCopy = visit(other.deepCopy());

        Context.Builder<?> builder = contextBuilder().add("Source before", source.toString())
            .add("Other before", other.toString());

        // Use MockedConstruction to disable the constructor of MySet where the visitation will be falsely counted
        try (MockedConstruction<?> construction = Mockito.mockConstruction(getClassType())) {
            MySet<VisitorElement<Integer>> result = source.difference(other);
            builder.add("Result", result.toString());
            builder.add("Source after", source.toString());
            builder.add("Other after", other.toString());
            builder.add("Source Visitation", List.of(sourceVisitation));
            builder.add("Other Visitation", List.of(otherVisitation));
            Context context = builder.build();
            assertVisitation(sourceCopy, sourceVisitation, "Source", context);
            assertVisitation(otherCopy, otherVisitation, "Other", context);
        }

        afterCheck(source, other, builder);
    }

    /**
     * Asserts whether the visitation of the given set is correct.
     *
     * @param set                the set to be checked
     * @param expectedVisitation the expected visitation
     * @param setName            the name of the set
     * @param context            the context to be used
     */
    protected void assertVisitation(
        VisitorSet<Integer> set,
        Integer[] expectedVisitation,
        String setName,
        Context context
    ) {
        Iterator<VisitorElement<Integer>> it = set.iterator();
        for (Integer expected : expectedVisitation) {
            if (!it.hasNext()) {
                break;
            }
            VisitorElement<Integer> element = it.next();
            if (expected == 1) {
                Assertions2.assertTrue(element.visited() != 0, context,
                    r -> "Expected %s (%s) to be visited, but was not visited".formatted(element, setName));
            } else {
                Assertions2.assertTrue(element.visited() == 0, context,
                    r -> "Expected %s (%s) not to be visited, but was visited".formatted(element, setName));
            }
        }
    }
}
