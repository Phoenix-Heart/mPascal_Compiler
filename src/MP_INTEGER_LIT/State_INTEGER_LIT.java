package MP_INTEGER_LIT;

import regex.State;

public class State_INTEGER_LIT extends State {
	    private static State state;
	    private State_INTEGER_LIT() {
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
	                context.changeState(State_INTEGER_LIT.getState());
	            }
	        else {
	        	context.changeState(regex.InvalidState.getState());
	        }	
	    }

	    @Override
	    public boolean accepted() {
	        return true;
	    }

}
