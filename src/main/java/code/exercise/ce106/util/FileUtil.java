package code.exercise.ce106.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public final class FileUtil {

    private FileUtil() {
    }

    /**
     * Return {@link Reader} descriptor of file
     *
     * <h5>Assumptions</h5>
     * <ul>
     *     <li>charset is UTF-8</li>
     * </ul>
     *
     * @param filename File name
     * @return {@link Reader} descriptor of file
     */
    public static Supplier<Reader> createResource(String filename) {
        return () -> {
            try {
                return new FileReader(filename, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

}
