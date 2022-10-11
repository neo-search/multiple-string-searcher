package org.neosearch.stringsearcher.trie;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import org.neosearch.stringsearcher.Emit;
import org.neosearch.stringsearcher.EmitHandler;
import org.neosearch.stringsearcher.FragmentToken;
import org.neosearch.stringsearcher.MatchToken;
import org.neosearch.stringsearcher.StringSearcher;
import org.neosearch.stringsearcher.StringSearcherConfig;
import org.neosearch.stringsearcher.StringSearcherPrepare;
import org.neosearch.stringsearcher.Token;
import org.neosearch.stringsearcher.trie.handler.DefaultEmitHandler;
import org.neosearch.stringsearcher.trie.handler.StatefulEmitHandler;
import org.neosearch.stringsearcher.trie.interval.IntervalTree;
import org.neosearch.stringsearcher.trie.interval.Intervalable;
import org.neosearch.stringsearcher.trie.util.ListElementRemoval;
import org.neosearch.stringsearcher.trie.util.ListElementRemoval.RemoveElementPredicate;

/**
 * A trie implementation, based on the Aho-Corasick white paper, Bell
 * technologies: http://cr.yp.to/bib/1975/aho.pdf
 * <p>
 *
 * The payload trie adds the possibility to specify emitted payloads for each
 * added keyword.
 * 
 * @author Daniel Beck
 * @param <T> The type of the supplied of the payload
 */
public class Trie<T> implements StringSearcher<T>, StringSearcherPrepare<T> {

    private final StringSearcherConfig trieConfig;

    private final State<T> rootState;

    public Trie(final StringSearcherConfig trieConfig) {
        this.trieConfig = trieConfig;
        this.rootState = new State<>();
    }

    /**
     * Used by the builder to add a text search keyword with a emit payload.
     *
     * @param keyword The search term to add to the list of search terms.
     * @param emit    the payload to emit for this search term.
     * @throws NullPointerException if the keyword is null.
     */
    public void addSearchString(String keyword, T emit) {
        if (keyword.isEmpty()) {
            return;
        }

        if (isCaseInsensitive()) {
            keyword = keyword.toLowerCase();
        }

        addState(keyword).addEmit(new Payload<T>(keyword, emit));
    }

    /**
     * Used by the builder to add a text search keyword.
     *
     * @param keyword The search term to add to the list of search terms.
     * @throws NullPointerException if the keyword is null.
     */
    public void addSearchString(String keyword) {
        if (keyword.isEmpty()) {
            return;
        }

        if (isCaseInsensitive()) {
            keyword = keyword.toLowerCase();
        }

        addState(keyword).addEmit(new Payload<T>(keyword, null));
    }

    public void addSearchStrings(String... keywords) {
        for (String keyword : keywords)
            addSearchString(keyword);

    }

    public void addSearchStrings(Collection<String> keywords) {
        for (String keyword : keywords)
            addSearchString(keyword);
    }

    private State<T> addState(final String keyword) {
        return getRootState().addState(keyword);
    }

    /**
     * Tokenizes the specified text and returns the emitted outputs.
     * 
     * @param text The text to tokenize.
     */
    public Collection<Token<T>> tokenize(final String text) {
        final Collection<Token<T>> tokens = new ArrayList<>();
        final Collection<Emit<T>> collectedEmits = parseText(text);
        int lastCollectedPosition = -1;

        for (final Emit<T> emit : collectedEmits) {
            if (emit.getStart() - lastCollectedPosition > 1) {
                tokens.add(createFragment(emit, text, lastCollectedPosition));
            }

            tokens.add(createMatch(emit, text));
            lastCollectedPosition = emit.getEnd();
        }

        if (text.length() - lastCollectedPosition > 1) {
            tokens.add(createFragment(null, text, lastCollectedPosition));
        }

        return tokens;
    }

    private Token<T> createFragment(final Emit<T> emit, final String text, final int lastCollectedPosition) {
        return new FragmentToken<T>(text.substring(lastCollectedPosition + 1, emit == null ? text.length() : emit.getStart()));
    }

    private Token<T> createMatch(Emit<T> emit, String text) {
        return new MatchToken<T>(text.substring(emit.getStart(), emit.getEnd() + 1), emit);
    }

    /**
     * Tokenizes a specified text and returns the emitted outputs.
     * 
     * @param text The character sequence to tokenize.
     * @return A collection of emits.
     */
    public Collection<Emit<T>> parseText(final CharSequence text) {
        return parseText(text, new DefaultEmitHandler<T>());
    }

    /**
     * Tokenizes the specified text by using a custom EmitHandler and returns the
     * emitted outputs.
     * 
     * @param text        The character sequence to tokenize.
     * @param emitHandler The emit handler that will be used to parse the text.
     * @return A collection of emits.
     */
    @SuppressWarnings("unchecked")
    public Collection<Emit<T>> parseText(final CharSequence text, final StatefulEmitHandler<T> emitHandler) {
        parseText(text, (EmitHandler<T>) emitHandler);

        final List<Emit<T>> collectedEmits = emitHandler.getEmits();

        if (trieConfig.isInWordCharacter() != null) {
            removePartialMatches(text, collectedEmits);
        }
        if (!trieConfig.isAllowOverlaps()) {
            IntervalTree intervalTree = new IntervalTree((List<Intervalable>) (List<?>) collectedEmits);
            intervalTree.removeOverlaps((List<Intervalable>) (List<?>) collectedEmits);
        }

        return collectedEmits;
    }

    /**
     * Returns true if the text contains contains one of the search terms. Else,
     * returns false.
     * 
     * @param text Specified text.
     * @return true if the text contains one of the search terms. Else, returns
     *         false.
     */
    public boolean containsMatch(final CharSequence text) {
        return firstMatch(text) != null;
    }

    /**
     * Tokenizes the specified text by using a custom EmitHandler and returns the
     * emitted outputs.
     * 
     * @param text        The character sequence to tokenize.
     * @param emitHandler The emit handler that will be used to parse the text.
     */

    public void parseText(final CharSequence text, final EmitHandler<T> emitHandler) {
        State<T> currentState = getRootState();

        for (int position = 0; position < text.length(); position++) {
            Character character = text.charAt(position);

            // TODO: Maybe lowercase the entire string at once?
            if (trieConfig.isCaseInsensitive()) {
                character = Character.toLowerCase(character);
            }

            currentState = getState(currentState, character);
            if (storeEmits(position, currentState, emitHandler) && trieConfig.isStopOnHit()) {
                return;
            }
        }
    }

    /**
     * The first matching text sequence.
     *
     * @param text The text to search for keywords.
     * @return null if no matches found.
     */
    public Emit<T> firstMatch(final CharSequence text) {
        if (!trieConfig.isAllowOverlaps()) {
            // Slow path. Needs to find all the matches to detect overlaps.
            final Collection<Emit<T>> parseText = parseText(text);

            if (parseText != null && !parseText.isEmpty()) {
                return parseText.iterator().next();
            }
        } else {
            // Fast path. Returns first match found.
            State<T> currentState = getRootState();

            for (int position = 0; position < text.length(); position++) {
                Character character = text.charAt(position);

                // TODO: Lowercase the entire string at once?
                if (trieConfig.isCaseInsensitive()) {
                    character = Character.toLowerCase(character);
                }

                currentState = getState(currentState, character);
                Collection<Payload<T>> payloads = currentState.emit();

                if (payloads != null && !payloads.isEmpty()) {
                    for (final Payload<T> payload : payloads) {
                        final Emit<T> emit = new Emit<>(position - payload.getKeyword().length() + 1, position,
                                payload.getKeyword(), payload.getData());
                        if (trieConfig.isOnlyWholeWords()) {
                            if (!isPartialMatch(text, emit)) {
                                return emit;
                            }
                        } else {
                            return emit;
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean isPartialMatch(final CharSequence searchText, final Emit<T> emit) {
        return (emit.getStart() != 0 && trieConfig.isInWordCharacter().test(searchText.charAt(emit.getStart() - 1)))
                || (emit.getEnd() + 1 != searchText.length() && trieConfig.isInWordCharacter().test(searchText.charAt(emit.getEnd() + 1)));
    }

    private void removePartialMatches(final CharSequence searchText, final List<Emit<T>> collectedEmits) {

        final RemoveElementPredicate<Emit<T>> predicate = new RemoveElementPredicate<Emit<T>>() {

            @Override
            public boolean remove(Emit<T> emit) {
                return isPartialMatch(searchText, emit);
            }

        };

        ListElementRemoval.removeIf(collectedEmits, predicate);
    }

    private State<T> getState(State<T> currentState, final Character character) {
        State<T> newCurrentState = currentState.nextState(character);

        while (newCurrentState == null) {
            currentState = currentState.failure();
            newCurrentState = currentState.nextState(character);
        }

        return newCurrentState;
    }

    public Trie<T> build() {
        final Queue<State<T>> queue = new ArrayDeque<>();
        final State<T> startState = getRootState();

        // First, set the fail state of all depth 1 states to the root state
        for (State<T> depthOneState : startState.getStates()) {
            depthOneState.setFailure(startState);
            queue.add(depthOneState);
        }

        // Second, determine the fail state for all depth > 1 state
        while (!queue.isEmpty()) {
            final State<T> currentState = queue.remove();

            for (final Character transition : currentState.getTransitions()) {
                State<T> targetState = currentState.nextState(transition);
                queue.add(targetState);

                State<T> traceFailureState = currentState.failure();
                while (traceFailureState.nextState(transition) == null) {
                    traceFailureState = traceFailureState.failure();
                }

                final State<T> newFailureState = traceFailureState.nextState(transition);
                targetState.setFailure(newFailureState);
                targetState.addEmit(newFailureState.emit());
            }
        }
        return this;
    }

    private boolean storeEmits(final int position, final State<T> currentState, final EmitHandler<T> emitHandler) {
        boolean emitted = false;
        final Collection<Payload<T>> payloads = currentState.emit();

        // TODO: The check for empty might be superfluous.
        if (payloads != null && !payloads.isEmpty()) {
            for (final Payload<T> payload : payloads) {
                emitted = emitHandler.emit(new Emit<T>(position - payload.getKeyword().length() + 1, position,
                        payload.getKeyword(), payload.getData())) || emitted;

                if (emitted && trieConfig.isStopOnHit()) {
                    break;
                }
            }
        }

        return emitted;
    }

    private boolean isCaseInsensitive() {
        return trieConfig.isCaseInsensitive();
    }

    private State<T> getRootState() {
        return this.rootState;
    }

}
