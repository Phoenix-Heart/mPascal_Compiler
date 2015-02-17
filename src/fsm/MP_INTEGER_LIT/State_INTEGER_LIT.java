// Written by Hunter
package fsm.MP_INTEGER_LIT;

import core.State;
import core.keystates.InvalidState;

public class State_INTEGER_LIT extends State {
	    private static State state;
	    private State_INTEGER_LIT() {
			state = this;
		}
	    // states are singletons
	    public static State getState() {
	        if(state==null) {
	            return new State_INTEGER_LIT();
	        }
	        else return state;
	    }
	    @Override
	    public void read(char c) {
	        if(Character.isDigit(c)) {
				// do not need to change state
	             //   context.changeState(State_INTEGER_LIT.getState());
	            }
	        else {
	        	context.changeState(InvalidState.getState());
	        }	
	    }

	    @Override
	    public boolean accepted() {
	        return true;
	    }

}
