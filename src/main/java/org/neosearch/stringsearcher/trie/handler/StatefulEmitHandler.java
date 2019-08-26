package org.neosearch.stringsearcher.trie.handler;

import java.util.List;

import org.neosearch.stringsearcher.EmitHandler;
import org.neosearch.stringsearcher.trie.Emit;

public interface StatefulEmitHandler<T> extends EmitHandler<T>{
    List<Emit<T>> getEmits();
}
