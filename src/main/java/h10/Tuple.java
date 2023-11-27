package h10;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * Tuple class for holding two values.
 *
 * @param first  the first value of the tuple
 * @param second the second value of the tuple
 * @param <F>    the type of the first value
 * @param <S>    the type of the second value
 * @author Lars Waßmann, Nhan Huynh
 */
@DoNotTouch
public record Tuple<F, S>(F first, S second) {
}
