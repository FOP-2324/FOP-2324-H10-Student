package h10.utils;

import h10.ListItem;
import h10.visitor.Visitable;
import h10.visitor.VisitorElement;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.List;

/**
 * Utility class for tutor assertions.
 *
 * @author Nhan Huynh
 */
public final class TutorAssertions {

    /**
     * This class should not be instantiated.
     */
    private TutorAssertions() {

    }

    /**
     * Returns the identity hash code of the given item.
     *
     * @param item the item to get the identity hash code of
     * @return the identity hash code of the given item
     */
    public static String identityHashCode(ListItem<?> item) {
        return item.getClass().getName() + "@" + System.identityHashCode(item);
    }

    /**
     * Returns the identity hash code of the given element.
     *
     * @param element the element to get the identity hash code of
     * @return the identity hash code of the given element
     */
    public static String identityHashCode(VisitorElement<?> element) {
        return element.peek().getClass() + "@" + System.identityHashCode(element.peek());
    }

    /**
     * Asserts that the given input is copied to the given output.
     *
     * @param input   the input of the operation to check
     * @param output  the output of the operation to check
     * @param context the context builder for the test
     */
    public static void assertAsCopy(Visitable<?> input, Visitable<?> output, Context.Builder<?> context) {
        List<String> otherIdentityHashCodes = output.underlyingStream().map(TutorAssertions::identityHashCode).toList();
        input.underlyingStream()
            .map(TutorAssertions::identityHashCode)
            .forEach(current -> otherIdentityHashCodes.forEach(other ->
                    Assertions2.assertNotEquals(
                        current, other,
                        context.add("Node before operation", current)
                            .add("Node after operation", other)
                            .build(),
                        r -> "Node %s was not copied, got %s".formatted(current, other)
                    )
                )
            );
    }

    /**
     * Asserts that the given input is copied to the given output.
     *
     * @param input   the input of the operation to check
     * @param output  the output of the operation to check
     * @param context the context builder for the test
     */
    public static void assertInPlace(Visitable<?> input, Visitable<?> output, Context.Builder<?> context) {
        List<String> otherIdentityHashCodes = output.underlyingStream().map(TutorAssertions::identityHashCode).toList();
        input.stream()
            .map(TutorAssertions::identityHashCode)
            .forEach(current -> {
                if (otherIdentityHashCodes.stream().anyMatch(current::equals)) {
                    Assertions2.fail(
                        context.add("Node before operation", current)
                            .add("Node after operation", "Not found")
                            .build(),
                        r -> "Cannot find the same node after the operation. Node was probably copied.");
                }
            });
    }
}
