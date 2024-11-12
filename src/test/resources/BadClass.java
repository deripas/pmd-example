package com.github.deripas.pmd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

/**
 * Test.
 */
@ExtendWith(MockitoExtension.class)
class SomeTest {

    @Test
    void test() {
        final BytesListener listener = createListener(
            (bytes) -> CompletableFuture.completedFuture("HI!")
        );
        Assertions.assertNotNull(listener.onRecord(new byte[0]));
    }

    private static BytesListener createListener(
        BytesParser<String> parser
    ) {
        return bytes -> parser
            .parse(bytes)
            .thenAccept(System.out::println);
    }
}
