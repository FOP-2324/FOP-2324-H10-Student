package h10.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import h10.ListItem;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;

/**
 * A {@link JsonNode} converter that converts an {@link ArrayNode} to a {@link ListItem}.
 *
 * @param <T> the type of the elements in the list
 * @author Nhan Huynh
 */
public abstract class ListItemConverter<T> implements GenericArgumentConverter<T> {

    @Override
    public ListItem<T> convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
        if (!(o instanceof ArrayNode arrayNode)) {
            throw new ArgumentConversionException("Expected ArrayNode, got " + o);
        }
        ListItem<T> head = null;
        ListItem<T> tail = null;
        for (JsonNode element : arrayNode) {
            ListItem<T> item = new ListItem<>(map(element));
            if (head == null) {
                head = item;
            } else {
                tail.next = item;
            }
            tail = item;
        }
        return head;
    }

    /**
     * Maps a {@link JsonNode} to an int node.
     */
    public static class Int extends ListItemConverter<Integer> {

        @Override
        public Integer map(JsonNode node) {
            if (!(node instanceof IntNode intNode)) {
                throw new ArgumentConversionException(
                    "Element %s in array is not an integer".formatted(node.getNodeType().getClass())
                );
            }
            return intNode.asInt();
        }
    }
}
