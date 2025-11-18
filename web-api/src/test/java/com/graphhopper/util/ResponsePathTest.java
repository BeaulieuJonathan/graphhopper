package com.graphhopper.util;

import com.graphhopper.ResponsePath;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ResponsePathTest {

    @Test

    public void testHasErrorsWithMockedThrowable() {
        Throwable t = mock(Throwable.class);

        ResponsePath path = new ResponsePath();
        path.addError(t);

        assertTrue(path.hasErrors(),
                "ResponsePath should report an error when a mocked Throwable is added");
        assertEquals(1, path.getErrors().size());
        assertSame(t, path.getErrors().get(0));
    }

    @Test
    public void testGetDebugInfoWithMockedString() {
        ResponsePath path = new ResponsePath();

        path.addDebugInfo("init-debug");
        path.addDebugInfo("calc-debug");

        String result = path.getDebugInfo();

        assertEquals("init-debug;calc-debug", result);
    }
}
