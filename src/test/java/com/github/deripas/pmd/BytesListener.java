package com.github.deripas.pmd;

import java.util.concurrent.CompletableFuture;


public interface BytesListener {

    CompletableFuture<Void> onRecord(byte[] bytes);
}
