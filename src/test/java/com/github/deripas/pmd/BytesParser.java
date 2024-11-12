package com.github.deripas.pmd;

import java.util.concurrent.CompletableFuture;

public interface BytesParser<T> {

    CompletableFuture<T> parse(byte[] bytes);
}
