package org.neosearch.stringsearcher.trie;

import java.util.*;

/**
 * <p>
 * A state has various important tasks it must attend to:
 * </p>
 * <p>
 * <ul>
 * <li>success; when a character points to another state, it must return that
 * state</li>
 * <li>failure; when a character has no matching state, the algorithm must be
 * able to fall back on a state with less depth</li>
 * <li>emits; when this state is passed and keywords have been matched, the
 * matches and their payloads must be 'emitted' so that they can be used later
 * on.</li>
 * </ul>
 * <p>
 * <p>
 * The root state is special in the sense that it has no failure state; it
 * cannot fail. If it 'fails' it will still parse the next character and start
 * from the root node. This ensures that the algorithm always runs. All other
 * states always have a fail state.
 * </p>
 *
 * @author Daniel Beck
 */
public class State<T> {

    /**
     * effective the size of the keyword
     */
    private final int depth;

    /**
     * only used for the root state to refer to itself in case no matches have been
     * found
     */
    private final State<T> rootState;

    /**
     * referred to in the white paper as the 'goto' structure. From a state it is
     * possible to go to other states, depending on the character passed.
     */
    private final Map<Character, State<T>> success = new HashMap<>();

    /**
     * If no matching states are found, the failure state will be returned
     */
    private State<T> failure;

    /**
     * Whenever this state is reached, it will emit the matches keywords for future
     * reference
     */
    private Set<Payload<T>> emits;

    /**
     * Constructs a root state with depth equals to 0.
     */
    public State() {
        this(0);
    }

    /**
     * Constructs a state with the specified depth.
     * 
     * @param depth Depth of the node.
     */
    public State(final int depth) {
        this.depth = depth;
        this.rootState = depth == 0 ? this : null;
    }

    /**
     * Given the specified character, tries to compute the next state.
     * 
     * @param character       Parameter used to compute the next state.
     * @param ignoreRootState if true, the root state is set as a side effect to
     *                        this method.
     */
    private State<T> nextState(final Character character, final boolean ignoreRootState) {
        State<T> nextState = this.success.get(character);

        if (!ignoreRootState && nextState == null && this.rootState != null) {
            nextState = this.rootState;
        }

        return nextState;
    }

    public State<T> nextState(final Character character) {
        return nextState(character, false);
    }

    public State<T> nextStateIgnoreRootState(Character character) {
        return nextState(character, true);
    }

    public State<T> addState(String keyword) {
        State<T> state = this;

        for (final Character character : keyword.toCharArray()) {
            state = state.addState(character);
        }

        return state;
    }

    public State<T> addState(Character character) {
        State<T> nextState = nextStateIgnoreRootState(character);
        if (nextState == null) {
            nextState = new State<T>(this.depth + 1);
            this.success.put(character, nextState);
        }
        return nextState;
    }

    public int getDepth() {
        return this.depth;
    }

    /**
     * Adds a payload to be emitted for this state.
     * 
     * @param emit Payload to be emitted.
     */
    public void addEmit(Payload<T> payload) {
        if (this.emits == null) {
            this.emits = new TreeSet<>();
        }
        this.emits.add(payload);
    }

    /**
     * Adds a collection of payloads to be emitted for this state.
     * 
     * @param emits Collection of payloads to be emitted.
     */
    public void addEmit(Collection<Payload<T>> emits) {
        for (Payload<T> emit : emits) {
            addEmit(emit);
        }
    }

    /**
     * Returns a collection of emitted payloads for this state.
     * 
     * @return Collection of emitted payloads.
     */
    public Collection<Payload<T>> emit() {
        return this.emits == null ? Collections.<Payload<T>>emptyList() : this.emits;
    }

    public State<T> failure() {
        return this.failure;
    }

    public void setFailure(State<T> failState) {
        this.failure = failState;
    }

    public Collection<State<T>> getStates() {
        return this.success.values();
    }

    public Collection<Character> getTransitions() {
        return this.success.keySet();
    }
}