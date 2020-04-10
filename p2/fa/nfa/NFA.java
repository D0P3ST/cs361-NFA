package fa.nfa;

import java.util.LinkedHashSet;
import java.util.Set;

public class NFA implements NFAInterface {

    private Set<NFAState> states;
	private NFAState start;
	private Set<Character> ordAbc;

    	/**
	 * 
	 * @return equivalent DFA
	 */
	public DFA getDFA(){
        states = new LinkedHashSet<DFAState>();
		ordAbc = new LinkedHashSet<Character>();
    }
	
	/**
	 * Return delta entries
	 * @param from - the source state
	 * @param onSymb - the label of the transition
	 * @return a set of sink states
	 */
	public Set<NFAState> getToState(NFAState from, char onSymb){

    }
	
	/**
	 * Traverses all epsilon transitions and determine
	 * what states can be reached from s through e
	 * @param s
	 * @return set of states that can be reached from s on epsilon trans.
	 */
	
	public Set<NFAState> eClosure(NFAState s){

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