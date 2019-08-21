package org.multiplestrings.trie.handler;

import java.util.List;

import org.multiplestrings.EmitHandler;
import org.multiplestrings.trie.Emit;

public interface StatefulEmitHandler<T> extends EmitHandler<T>{
    List<Emit<T>> getEmits();
}
