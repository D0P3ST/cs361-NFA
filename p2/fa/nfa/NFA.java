package fa.nfa;

import fa.dfa.DFA;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class NFA implements NFAInterface {

    private Set<NFAState> states = new HashSet<>();
	private NFAState start;
	private Set<Character> ordAbc = new HashSet<>();

	/**
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


	/* (non-Javadoc)
	 * @see p1.DFAInterface#addStartState(java.lang.String)
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
	/* (non-Javadoc)
	 * @see p1.DFAInterface#addState(java.lang.String)
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

	/* (non-Javadoc)
	 * @see p1.DFAInterface#addFinalState(java.lang.String)
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

	private void addState(NFAState s){
		states.add(s);
	}

	/* (non-Javadoc)
	 * @see p1.DFAInterface#addTransition(p1.State, char, p1.State)
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

	/** (non-Javadoc)
	 * @see p1.DFAInterface#toString()
	 **/
	@Override
	public String toString(){

		String s = "Q = { ";
		String fStates = "F = { ";
		for(NFAState state : states){
			s += state.toString();
			s +=" ";
			if(state.isFinal()){
				fStates +=state.toString();
				fStates += " ";
			}
		}
		s += "}\n";
		fStates += "}\n";
		s += "Sigma = { ";
		for(char c : ordAbc){
			s += c + " ";
		}
		s += "}\n";
		//create transition table
		s += "delta =\n"+String.format("%10s", "");;
		for(char c : ordAbc){
			s += String.format("%10s", c);
		}
		s+="\n";
		for(NFAState state : states){
			s += String.format("%10s",state.toString());
			for(char c : ordAbc){
				s += String.format("%10s", state.getTo(c).toString());
			}
			s+="\n";
		}
		//start state
		s += "q0 = " + start + "\n";
		s += fStates;
		return s;
	}


	@Override
	public Set<NFAState> getStates() {
		return states;
	}

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

	@Override
	public NFAState getStartState() {
		return start;
    }

	@Override
	public Set<Character> getABC() {
		return ordAbc;
	}

}