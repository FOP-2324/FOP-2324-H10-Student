package h10.rubric.h2;

import h10.ListItem;
import h10.MySet;
import h10.Sets;
import h10.VisitorSet;
import h10.converter.ListItemConverter;
import h10.converter.ListItemInListItemConverter;
import h10.rubric.SimpleTest;
import h10.utils.ListItems;
import h10.utils.TestConstants;
import h10.visitor.VisitorElement;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@TestForSubmission
@DisplayName("H2 | cartesianProduct(MySet)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H2_TestsPublic extends SimpleTest {

    protected static final String TEST_RESOURCE_PATH = "h2/";

    protected static final String METHOD_NAME = "cartesianProduct";

    @Override
    public String getMethodName() {
        return METHOD_NAME;
    }

    @SafeVarargs
    protected final <T> ListItem<VisitorElement<T>> of(T... values) {
        ListItem<VisitorElement<T>> head = null;
        ListItem<VisitorElement<T>> tail = null;
        for (T value : values) {
            ListItem<VisitorElement<T>> node = new ListItem<>(new VisitorElement<>(value));
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
        }
        return head;
    }

    @Order(0)
    @DisplayName("Die Methode cartesianProduct(MySet) gibt das korrekte Ergebnis für simple Eingaben zurück.")
    @ParameterizedTest(name = "Source = {0}, Other {1}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion1_testcase1.json",
        TEST_RESOURCE_PATH + "criterion1_testcase2.json",
    })
    public void testSimple(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> head,
        @ConvertWith(ListItemConverter.Int.class) @Property("otherHead") ListItem<Integer> otherHead,
        @ConvertWith(ListItemInListItemConverter.Int.class) @Property("expected") ListItem<ListItem<Integer>> expected
    ) {
        assertTuples(head, otherHead, expected);
    }

    private void assertTuples(
        ListItem<Integer> head,
        ListItem<Integer> otherHead,
        ListItem<ListItem<Integer>> expected
    ) {
        VisitorSet<Integer> source = visit(head);
        VisitorSet<Integer> other = visit(otherHead);
        Context.Builder<?> builder = contextBuilder()
            .add("Source", source.toString())
            .add("Other", other.toString());
        MySet<ListItem<VisitorElement<Integer>>> result = source.cartesianProduct(other);
        builder.add("Result", result.toString());

        ListItems.stream(expected).forEach(tuple -> {
            if (Sets.stream(result)
                .noneMatch(actual -> Objects.equals(tuple.key, actual.key.peek())
                    && tuple.next != null && actual.next != null
                    && Objects.equals(tuple.next.key, actual.next.key.peek()))) {
                Assertions2.fail(
                    builder.add("Expected tuple", tuple.toString()).build(),
                    r -> "Expected tuple %s not found in result".formatted(tuple.toString())
                );
            }
        });
    }
}
