package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class VideoUrlValidatorTest {

    private VideoUrlValidator videoUrlValidator;

    @BeforeEach
    void setUp() {
        videoUrlValidator = new VideoUrlValidator();
    }

    @Test
    void shouldReturnValidVideoUrl() {
        List<String> errors = videoUrlValidator.validate("https://www.youtube.com/watch?v=h9jCrpSUpv8");

        assertEquals(0, errors.size());
    }

    @Test
    void shouldReturnValidVideoUrl_Shared() {
        List<String> errors = videoUrlValidator.validate("https://youtu.be/h9jCrpSUpv8?si=fctu4zrSrEuIGp8l");

        assertEquals(0, errors.size());
    }

    @Test
    void shouldReturnValidVideoUrl_SharedWithoutParams() {
        List<String> errors = videoUrlValidator.validate("https://youtu.be/h9jCrpSUpv8");

        assertEquals(0, errors.size());
    }

    @Test
    void shouldReturnValidVideoUrl_Embed() {
        List<String> errors = videoUrlValidator.validate("https://www.youtube.com/embed/h9jCrpSUpv8?si=E5SnXr1HXwtam9xz");

        assertEquals(0, errors.size());
    }

    @Test
    void shouldReturnNullBlank_NullUrl() {
        List<String> errors = videoUrlValidator.validate(null);

        assertEquals(1, errors.size());
    }

}