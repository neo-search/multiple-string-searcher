package org.multiplestrings.trie.handler;

import org.multiplestrings.trie.PayloadEmit;

public interface PayloadEmitHandler<T> {
    boolean emit(PayloadEmit<T> emit);
}
