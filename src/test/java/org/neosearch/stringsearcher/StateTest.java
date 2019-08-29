package org.neosearch.stringsearcher;

import org.junit.Test;
import org.neosearch.stringsearcher.trie.State;

import static junit.framework.Assert.assertEquals;

public class StateTest {

    @Test
    public void constructSequenceOfCharacters() {
        State<String> rootState = new State<String>();
        rootState.addState('a').addState('b').addState('c');
        State<String> currentState = rootState.nextState('a');
        assertEquals(1, currentState.getDepth());
        currentState = currentState.nextState('b');
        assertEquals(2, currentState.getDepth());
        currentState = currentState.nextState('c');
        assertEquals(3, currentState.getDepth());
    }

}
