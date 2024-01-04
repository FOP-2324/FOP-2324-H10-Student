package h10.rubric.h2;

import h10.ListItem;
import h10.MySet;
import h10.MySetInPlace;
import h10.converter.ListItemConverter;
import h10.converter.ListItemInListItemConverter;
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

import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

@TestForSubmission
@DisplayName("H2.2 | In-Place")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public class H2_2_TestsPublic extends H2_TestsPublic {

    @Override
    public Class<?> getClassType() {
        return MySetInPlace.class;
    }

    @Override
    protected BiFunction<
        ListItem<VisitorElement<Integer>>,
        Comparator<? super VisitorElement<Integer>>,
        MySet<VisitorElement<Integer>>
        > converter() {
        return MySetInPlace::new;
    }

    @Order(0)
    @DisplayName("Die Methode cartesianProduct(MySet) gibt das korrekte Ergebnis für simple Eingaben zurück.")
    @ParameterizedTest(name = "Source = {0}, Other {1}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion1_testcase1.json",
        TEST_RESOURCE_PATH + "criterion1_testcase2.json",
    })
    @Override
    public void testSimple(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> head,
        @ConvertWith(ListItemConverter.Int.class) @Property("otherHead") ListItem<Integer> otherHead,
        @ConvertWith(ListItemInListItemConverter.Int.class) @Property("expected") ListItem<ListItem<Integer>> expected
    ) {
        super.testSimple(head, otherHead, expected);
    }
}
