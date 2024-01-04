package h10.rubric.h1;

import h10.ListItem;
import h10.VisitorSet;
import h10.converter.ListItemConverter;
import h10.converter.PredicateConverter;
import h10.rubric.SimpleTest;
import h10.utils.ListItems;
import h10.utils.TestConstants;
import h10.visitor.VisitorElement;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@TestForSubmission
@DisplayName("H1 | subset(MySet)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H1_TestsPublic extends SimpleTest {

    protected static final String TEST_RESOURCE_PATH = "h1/";

    protected static final String METHOD_NAME = "subset";

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

    @Override
    public String getMethodName() {
        return METHOD_NAME;
    }

    @AfterEach
    public void tearDown() {
        assertRequirement();
        assertVisitation();
    }

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
     * Asserts that each node in the source set is visited only once.
     */
    public void assertVisitation() {
        Assumptions.assumeTrue(source != null);
        Assumptions.assumeTrue(context != null);

        // Nodes must be visited only once
        List<VisitorElement<Integer>> failed = source.stream()
            .filter(element -> element.visited() > 1)
            .toList();
        Assertions2.assertTrue(
            failed.isEmpty(),
            context.add("Nodes visited more than once", failed).build(),
            r -> "Expected no visited node more than once, got %s.".formatted(failed)
        );
    }

    /**
     * Asserts the requirement of the test.
     */
    public abstract void assertRequirement();

    @Order(0)
    @DisplayName("Die Methode subset(MySet) nimmt  Elemente in die Ergebnismenge nicht auf, falls das Prädikat nicht "
        + "erfüllt wird.")
    @ParameterizedTest(name = "Elements = {0}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion1_testcase1.json",
        TEST_RESOURCE_PATH + "criterion1_testcase2.json",
        TEST_RESOURCE_PATH + "criterion1_testcase3.json",
    })
    public void testPredicateFalse(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> head
    ) {
        VisitorSet<Integer> source = visit(head);
        // Drop all elements
        Predicate<? super VisitorElement<Integer>> predicate = new Predicate<>() {
            @Override
            public boolean test(VisitorElement<Integer> element) {
                // Explicitly call this to mark the node as visited
                element.visit();
                return false;
            }

            @Override
            public String toString() {
                return "Drop all";
            }
        };
        Context.Builder<?> context = contextBuilder()
            .add("Comparator", DEFAULT_COMPARATOR)
            .add("Predicate", predicate)
            .add("Source", source.toString());
        VisitorSet<Integer> result = visit(source.subset(predicate));
        context.add("Result", result.toString());

        // Since we the predicate drop all elements, the head of the set must be null
        Assertions2.assertFalse(
            result.iterator().hasNext(),
            context.build(),
            r -> "Subset should be empty, but got %s.".formatted(result)
        );
        afterCheck(source, result, context);
    }

    @Order(1)
    @DisplayName("Die Methode subset(MySet) gibt das korrekte Ergebnis für eine komplexe Eingabe zurück.")
    @ParameterizedTest(name = "Elements = {0}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion2_testcase1.json",
        TEST_RESOURCE_PATH + "criterion2_testcase2.json",
        TEST_RESOURCE_PATH + "criterion2_testcase3.json",
    })
    public void testPredicateComplex(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> head,
        @ConvertWith(PredicateConverter.BasicIntMath.class) @Property("predicate") Predicate<Integer> predicate,
        @ConvertWith(ListItemConverter.Int.class) @Property("expected") ListItem<Integer> expected
    ) {
        VisitorSet<Integer> source = visit(head);
        Predicate<VisitorElement<Integer>> test = new Predicate<>() {
            @Override
            public boolean test(VisitorElement<Integer> x) {
                return predicate.test(x.get());
            }

            @Override
            public String toString() {
                return predicate.toString();
            }
        };

        Context.Builder<?> context = contextBuilder()
            .add("Comparator", DEFAULT_COMPARATOR)
            .add("Predicate", test)
            .add("Source", source.toString());

        VisitorSet<Integer> result = visit(source.subset(test));
        context.add("Result", result.toString());

        Iterator<Integer> expectedIt = ListItems.iterator(expected);
        Iterator<Integer> actualIt = result.peekIterator();

        while (expectedIt.hasNext() && actualIt.hasNext()) {
            int e = expectedIt.next();
            int a = actualIt.next();
            Assertions2.assertEquals(
                e, a,
                context.add("Expected node", e).add("Actual node", a).build(),
                r -> "Expected node %s, but was %s".formatted(e, a)
            );
        }

        // The expected size must be equal to actual size
        Assertions2.assertFalse(expectedIt.hasNext(), context.build(),
            r -> "Expected list contains more element than actual list");
        Assertions2.assertFalse(actualIt.hasNext(), context.build(),
            r -> "Actual list contains more element than expected list");

        afterCheck(source, result, context);
    }
}
