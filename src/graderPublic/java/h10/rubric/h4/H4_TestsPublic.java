package h10.rubric.h4;

import h10.ListItem;
import h10.MySet;
import h10.VisitorSet;
import h10.converter.ListItemConverter;
import h10.rubric.ComplexTest;
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
import org.tudalgo.algoutils.tutor.general.conversion.ArrayConverter;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

@TestForSubmission
@DisplayName("H4 | intersectionListItems(ListItem)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H4_TestsPublic extends ComplexTest {

    protected static final String TEST_RESOURCE_PATH = "h4/";

    protected static final String METHOD_NAME = "intersectionListItems";

    @Override
    public String getMethodName() {
        return METHOD_NAME;
    }

    @Override
    protected BiFunction<VisitorSet<Integer>, VisitorSet<Integer>, MySet<VisitorElement<Integer>>> operation() {
        return MySet::intersection;
    }

    @Order(0)
    @DisplayName("Die Methode intersectionListItems(ListItem) nimmt ein Element auf, falls das Element in allen "
        + "Mengen enthalten ist.")
    @ParameterizedTest(name = "Source = {0}, Other = {1}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion1_testcase1.json",
        TEST_RESOURCE_PATH + "criterion1_testcase2.json",
    })
    public void testNotAddElements(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> sourceHead,
        @ConvertWith(ListItemConverter.Int.class) @Property("other") ListItem<Integer> otherHead,
        @ConvertWith(ListItemConverter.Int.class) @Property("expected") ListItem<Integer> expectedHead
    ) {
        super.testNotAddElements(sourceHead, otherHead, expectedHead);
    }

    @Order(1)
    @DisplayName("Die Methode intersectionListItems(ListItem) setzt die Laufvariable von der Menge M korrekt auf das "
        + "nächste Element, falls x < y gilt für x in M und y in N.")
    @ParameterizedTest(name = "Source = {0}, Other = {1}, Source Visitation = {2}, Other Visitation = {3}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion2_testcase1.json",
        TEST_RESOURCE_PATH + "criterion2_testcase2.json",
    })
    public void testXSmallerY(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> sourceHead,
        @ConvertWith(ListItemConverter.Int.class) @Property("other") ListItem<Integer> otherHead,
        @ConvertWith(ArrayConverter.Auto.class) @Property("sourceVisitation") Integer[] sourceVisitation,
        @ConvertWith(ArrayConverter.Auto.class) @Property("otherVisitation") Integer[] otherVisitation
    ) {
        super.testXSmallerY(sourceHead, otherHead, sourceVisitation, otherVisitation);
    }

    @Order(2)
    @DisplayName("Die Methode intersectionListItems(ListItem) setzt die Laufvariable von der Menge M korrekt auf das "
        + "nächste Element, falls x > y gilt für x in M und y in N.")
    @ParameterizedTest(name = "Source = {0}, Other = {1}, Source Visitation = {2}, Other Visitation = {3}")
    @JsonClasspathSource({
        TEST_RESOURCE_PATH + "criterion3_testcase1.json",
        TEST_RESOURCE_PATH + "criterion3_testcase2.json",
    })
    public void testXGreaterY(
        @ConvertWith(ListItemConverter.Int.class) @Property("head") ListItem<Integer> sourceHead,
        @ConvertWith(ListItemConverter.Int.class) @Property("other") ListItem<Integer> otherHead,
        @ConvertWith(ArrayConverter.Auto.class) @Property("sourceVisitation") Integer[] sourceVisitation,
        @ConvertWith(ArrayConverter.Auto.class) @Property("otherVisitation") Integer[] otherVisitation
    ) {
        assertPointers(sourceHead, otherHead, sourceVisitation, otherVisitation);
    }
}
