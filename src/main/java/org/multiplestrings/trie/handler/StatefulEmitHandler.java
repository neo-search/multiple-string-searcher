package org.multiplestrings.trie.handler;

import java.util.List;

import org.multiplestrings.trie.Emit;

public interface StatefulEmitHandler extends EmitHandler {
    List<Emit> getEmits();
}
