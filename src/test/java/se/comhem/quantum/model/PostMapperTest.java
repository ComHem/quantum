package se.comhem.quantum.model;

import org.junit.Test;

public class PostMapperTest {

    @Test
    public void shouldNotThrowExceptionWhenValidatingMapper() throws Exception {
        new PostMapper().validate();
    }
}