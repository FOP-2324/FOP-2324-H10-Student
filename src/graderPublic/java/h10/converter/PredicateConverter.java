package h10.converter;


import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * A {@link String} converter that converts a {@link String} to a {@link Predicate}.
 *
 * @param <T> the type of the predicate
 * @author Nhan Huynh
 */
public abstract class PredicateConverter<T> implements ArgumentConverter {

    @Override
    public Predicate<T> convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
        if (!(o instanceof String string)) {
            throw new ArgumentConversionException("Expected String, got " + o);
        }

        return parse(string);
    }

    /**
     * Parses the given string to a {@link Predicate}.
     *
     * @param string the string to parse
     * @return the parsed predicate
     */
    protected abstract Predicate<T> parse(String string);

    /**
     * A {@link PredicateConverter} that converts a {@link String} to a {@link Predicate} of {@link Integer}.
     * This converter supports the following operators:
     * <ul>
     *     <li>{@code ==}</li>
     *     <li>{@code !=}</li>
     *     <li>{@code <=}</li>
     *     <li>{@code >=}</li>
     *     <li>{@code <}</li>
     *     <li>{@code >}</li>
     *     <li>{@code %}</li>
     *     <li>Chained operations</li>
     * </ul>
     */
    public static class BasicIntMath extends PredicateConverter<Integer> {

        /**
         * The supported operators.
         */
        public static final Pattern OPERATORS = Pattern.compile("==|!=|<=|>=|<|>|%");

        /**
         * The pattern to match an integer.
         */
        private static final Pattern PATTERN_INT = Pattern.compile("-?\\d+");

        @Override
        protected Predicate<Integer> parse(String string) {
            Iterator<String> ops = OPERATORS.matcher(string).results().map(MatchResult::group).iterator();
            Iterator<Integer> operands = PATTERN_INT.matcher(string).results().map(MatchResult::group)
                .map(Integer::parseInt)
                .iterator();
            // Base
            String op = ops.next();
            int operand = operands.next();

            // Overwrite the toString method to improve the readability of the test output
            Predicate<Integer> predicate = new Predicate<Integer>() {
                @Override
                public boolean test(Integer x) {
                    return evaluate(x, operand, op);
                }

                @Override
                public String toString() {
                    return "x" + BasicIntMath.this.toString(operand, op);
                }
            };

            // Chained operations
            while (ops.hasNext()) {
                String nextOp = ops.next();
                int nextOperand = operands.next();
                Predicate<Integer> tmp = predicate;
                predicate = new Predicate<>() {
                    @Override
                    public boolean test(Integer x) {
                        return tmp.test(x) && evaluate(x, nextOperand, nextOp);
                    }

                    @Override
                    public String toString() {
                        return tmp + BasicIntMath.this.toString(nextOperand, nextOp);
                    }
                };
            }

            return predicate;
        }

        /**
         * Evaluates the given operation.
         *
         * @param x  the first operand
         * @param y  the second operand
         * @param op the operator
         * @return the result of the operation
         */
        private boolean evaluate(int x, int y, String op) {
            return switch (op) {
                case "==" -> x == y;
                case "!=" -> x != y;
                case "<=" -> x <= y;
                case ">=" -> x >= y;
                case "<" -> x < y;
                case ">" -> x > y;
                default -> throw new IllegalArgumentException("Unexpected value: " + op);
            };
        }

        /**
         * Returns a string representation of the given operation.
         *
         * @param y  the second operand
         * @param op the operator
         * @return a string representation of the given operation
         */
        private String toString(int y, String op) {
            return switch (op) {
                case "==" -> " == " + y;
                case "!=" -> " != " + y;
                case "<=" -> " <= " + y;
                case ">=" -> " >= " + y;
                case "<" -> " < " + y;
                case ">" -> " > " + y;
                default -> throw new IllegalArgumentException("Unexpected value: " + op);
            };
        }
    }
}
