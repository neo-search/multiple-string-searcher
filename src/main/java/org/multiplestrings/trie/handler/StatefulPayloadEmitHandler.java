package org.multiplestrings.trie.handler;

import java.util.List;

import org.multiplestrings.trie.PayloadEmit;

public interface StatefulPayloadEmitHandler<T> extends PayloadEmitHandler<T>{
    List<PayloadEmit<T>> getEmits();
}
