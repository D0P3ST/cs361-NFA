package fa.nfa;

import fa.State;

import java.util.HashMap;
import java.util.Set;

/**
 * April 10, 2020
 * Implementation of an NFA State
 * Contains transition functions for moving between this state and its neighbors
 * @author Zachary Gillenwater
 */
public class NFAState extends State {
    private HashMap<Character, Set<NFAState>> delta;
    private boolean isFinal;

    /**
     * Default constructor
     * @param name name of the state
     */
    public NFAState(String name) {
        initDefault(name);
        isFinal = false;
    }

    /**
     * Constructor which also sets the state type (final/non-final)
     * @param name name of the state
     * @param isFinal state type - true is final, false is nonfinal
     */
    public NFAState(String name, boolean isFinal) {
        initDefault(name);
        this.isFinal = isFinal;
    }

    private void initDefault(String name) {
        this.name = name;
        delta = new HashMap<Character, Set<NFAState>>();
    }

    /**
     * Gets whether or not the symbol is final
     * @return true if final, false otherwise
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Adds a transition from this state to an input state on the provided symbol
     * @param onSymb Symbol to transition on
     * @param toState State to transition to
     */
    public void addTransition (char onSymb, NFAState toState) {
        // If we there is no transition set for this symbol, make one
        if (delta.get(onSymb) == null) {
            delta.put(onSymb, Set.of(toState));
        }

        // Else, add to the set we already have
        else {
            delta.get(onSymb).add(toState);
        }
    }

    /**
     * Returns a set of states which can be transitioned to with the provided symbol
     * @param symb symbol to transition on
     * @return set of valid transition destinations
     */
    public Set<NFAState> getTo(char symb) {
        return delta.get(symb);
    }
}
