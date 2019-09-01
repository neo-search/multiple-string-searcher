package org.neosearch.stringsearcher.trie.handler;

import java.util.List;

import org.neosearch.stringsearcher.Emit;
import org.neosearch.stringsearcher.EmitHandler;

public interface StatefulEmitHandler<T> extends EmitHandler<T>{
    List<Emit<T>> getEmits();
}
