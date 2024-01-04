package h10.rubric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import java.io.IOException;

/**
 * A utility class for {@link Rubric}s which provides additional operations on rubrics.
 *
 * @author Nhan Huynh
 * @see Rubric
 * @see RubricProvider
 */
public final class Rubrics {

    /**
     * This class cannot be instantiated.
     */
    private Rubrics() {

    }

    /**
     * Reads a rubric from the given path (JSON file).
     *
     * @param title the title of the rubric
     * @param path  the path to the JSON file
     * @return the rubric read from the given path
     */
    public static Rubric read(String title, String path) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Criterion[].class, new RubricDeserializer());
        mapper.registerModule(module);
        Criterion[] criteria;
        try {
            criteria = mapper.readValue(Rubrics.class.getClassLoader().getResource(path), Criterion[].class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return Rubric.builder()
            .title(title)
            .addChildCriteria(criteria)
            .build();
    }
}
