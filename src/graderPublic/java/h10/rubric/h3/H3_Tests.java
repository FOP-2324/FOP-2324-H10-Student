package h10.rubric.h3;

import h10.ListItem;
import h10.MySet;
import h10.MySetAsCopy;
import h10.MySetInPlace;
import h10.rubric.H3_or4_Tests;
import h10.rubric.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Defines a base class for testing a method for the H3 assignment. A subclass of this class needs
 * to implement at least {@link #getClassType()} and {@link #setProvider()} since the criteria for {@link MySetAsCopy}
 * and {@link MySetInPlace} are the same.
 *
 * @author Nhan Huynh
 */
@TestForSubmission
@DisplayName("H3 | difference(MySet)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public abstract class H3_Tests extends H3_or4_Tests {
    @Override
    protected BiFunction<MySet<Integer>, ListItem<MySet<Integer>>, MySet<Integer>> operation() {
        return (source, inputs) -> source.difference(inputs.key);
    }
}