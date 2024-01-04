package h10.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.params.converter.ArgumentConverter;

/**
 * This interface provides a generic way to convert a {@link JsonNode} to a specific type.
 *
 * @param <T> the type to convert to
 * @author Nhan Huynh
 */
public interface GenericArgumentConverter<T> extends ArgumentConverter {

    T map(JsonNode node);
}
