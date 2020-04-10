package fa.nfa;

import fa.dfa.DFA;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * NFA represents a Non-Deterministic Finite Automaton
 * It holds a start state, final states, and normal states and can be converted to an equivalent DFA
 *
 * @author Chris Miller
 * @author Zachary Gillenwater
 */
public class NFA implements NFAInterface {

    private Set<NFAState> states = new HashSet<>();
	private NFAState start;
	private Set<Character> ordAbc = new HashSet<>();

	/**
	 * Converts the NFA to an equivalent DFA using a breadth-first algorithm
	 *
	 * @return equivalent DFA
	 */
	public DFA getDFA(){
        DFA equivalent = new DFA();
		Queue<Set<NFAState>> stateQueue = new LinkedBlockingQueue<>();
		Set<String> visitedNodes = new HashSet<>();

		stateQueue.add(Set.of(this.start));
		equivalent.addStartState(Set.of(this.start).toString());
		while (stateQueue.size() > 0) {
			Set<NFAState> currStates = stateQueue.remove();

			for (char c : this.ordAbc) {
				Set<NFAState> childStates;
				if (c != 'e') {
					childStates = getChildren(currStates, c);

					for (NFAState s : childStates) {
						childStates.addAll(eClosure(s));
					}

					if (!visitedNodes.contains(childStates.toString())) {
						boolean statesAreFinal = false;
						for (NFAState n : childStates) {
							if (n.isFinal()) {
								statesAreFinal = true;
							}
						}

						if (statesAreFinal) {
							equivalent.addFinalState(childStates.toString());
						} else {
							equivalent.addState(childStates.toString());
						}

						stateQueue.add(childStates);
						visitedNodes.add(childStates.toString());
					}
					equivalent.addTransition(currStates.toString(), c, childStates.toString());
				}
			}

		}
        return equivalent;
    }

	/**
	 * Convenience function to retrieve a combined set of all NFAStates reachable
	 * by all NFAStates given in the input set, with the input symbol.
	 * @param states Set of NFAStates to find the children of
	 * @param onSymb Symbol to transition on
	 * @return Combined set of all states reachable by all input states on the input symbol
	 */
    public Set<NFAState> getChildren(Set<NFAState> states, char onSymb) {
		Set<NFAState> children = new HashSet<>();

		for (NFAState n : states) {
			if (n.getTo(onSymb) != null) {
				children.addAll(n.getTo(onSymb));
			}
		}
		return children;
	}
	
	/**
	 * Return delta entries
	 * @param from - the source state
	 * @param onSymb - the label of the transition
	 * @return a set of sink states
	 */
	public Set<NFAState> getToState(NFAState from, char onSymb){
		return from.getTo(onSymb);
    }
	
	/**
	 * Traverses all epsilon transitions and determine
	 * what states can be reached from s through e
	 * @param s
	 * @return set of states that can be reached from s on epsilon trans.
	 */
	public Set<NFAState> eClosure(NFAState s){
		Set<NFAState> states = new HashSet<>();
		if (s.getTo('e') != null) {
			for (NFAState n : s.getTo('e')) {
				states.add(n);
				if (!states.contains(n)) {
					states.addAll(this.eClosure(n));
				}
			}
		}
		return states;
    }


	/**
	 * Add a state as the start state to the NFA
	 * @param name is the label of the start state
	 */
	@Override
	public void addStartState(String name){
		NFAState s = checkIfExists(name);
		if(s == null){
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
		start = s;
	}

	/**
	 * Add a normal state to the NFA by name
	 * @param name is the label of the state
	 */
	@Override
	public void addState(String name){
		NFAState s = checkIfExists(name);
		if( s == null){
			s = new NFAState(name);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}

	/**
	 * Add a final state to the NFA
	 * @param name is the label of the state
	 */
	@Override
	public void addFinalState(String name){
		NFAState s = checkIfExists(name);
		if( s == null){
			s = new NFAState(name, true);
			addState(s);
		} else {
			System.out.println("WARNING: A state with name " + name + " already exists in the DFA");
		}
	}

	/**
	 * Add a state to the NFA from an NFAState
	 * @param s
	 */
	private void addState(NFAState s){
		states.add(s);
	}

	/**
	 * Add a transition to the NFA state
	 * @param fromState is the label of the state where the transition starts
	 * @param onSymb is the symbol from the DFA's alphabet.
	 * @param toState is the label of the state where the transition ends
	 */
	@Override
	public void addTransition(String fromState, char onSymb, String toState){
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if(from == null){
			System.err.println("ERROR: No DFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null){
			System.err.println("ERROR: No DFA state exists with name " + toState);
			System.exit(2);
		}
		from.addTransition(onSymb, to);
		
		if(!ordAbc.contains(onSymb)){
			ordAbc.add(onSymb);
		}
	}

	/**
	 * Check if a state with such name already exists
	 * @param name
	 * @return null if no state exist, or DFAState object otherwise.
	 */
	private NFAState checkIfExists(String name){
		NFAState ret = null;
		for(NFAState s : states){
			if(s.getName().equals(name)){
				ret = s;
				break;
			}
		}
		return ret;
	}

	/**
	 * Returns a list of all states in the NFA
	 * @return Set of NFAStates in the NFA
	 */
	@Override
	public Set<NFAState> getStates() {
		return states;
	}

	/**
	 * Returns a list of all final states in the NFA
	 * @return Set of final NFAStates in the NFA
	 */
	@Override
	public Set<NFAState> getFinalStates() {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for(NFAState s : states){
			if(s.isFinal()){
				ret.add(s);
			}
		}
		return ret;
	}

	/**
	 * Returns the starting state of the NFA
	 * @return Starting NFAState
	 */
	@Override
	public NFAState getStartState() {
		return start;
    }

	/**
	 * Returns the alphabet recognized by the NFA
	 * @return Alphabet as a Set of Character
	 */
	@Override
	public Set<Character> getABC() {
		return ordAbc;
	}

}