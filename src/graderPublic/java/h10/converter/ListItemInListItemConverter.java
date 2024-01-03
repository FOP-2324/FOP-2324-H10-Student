package h10.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import h10.ListItem;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;

/**
 * A {@link JsonNode} converter that converts an {@link ArrayNode} to a nested {@link ListItem}.
 *
 * @param <T> the type of the elements in the list
 * @author Nhan Huynh
 */
public abstract class ListItemInListItemConverter<T> implements GenericArgumentConverter<T> {

    /**
     * The converter to convert the inner list.
     */
    protected final ListItemConverter<T> converter;

    /**
     * Creates a new {@link ListItemInListItemConverter} with the given converter.
     *
     * @param converter the converter to convert the inner list
     */
    public ListItemInListItemConverter(ListItemConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public ListItem<ListItem<T>> convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
        if (!(o instanceof ArrayNode outerNode)) {
            throw new ArgumentConversionException("Expected ArrayNode, got " + o);
        }
        ListItem<ListItem<T>> outerHead = null;
        ListItem<ListItem<T>> outerTail = null;
        for (JsonNode element : outerNode) {
            if (!(element instanceof ArrayNode innerNode)) {
                throw new ArgumentConversionException("Expected ArrayNode, got " + element);
            }
            ListItem<ListItem<T>> item = new ListItem<>(converter.convert(element, parameterContext));
            if (outerHead == null) {
                outerHead = item;
            } else {
                outerTail.next = item;
            }
            outerTail = item;
        }
        return outerHead;
    }

    @Override
    public T map(JsonNode node) {
        return converter.map(node);
    }

    /**
     * Maps a {@link JsonNode} to an int node.
     */
    public static class Int extends ListItemInListItemConverter<Integer> {

        public Int() {
            super(new ListItemConverter.Int());
        }
    }
}
