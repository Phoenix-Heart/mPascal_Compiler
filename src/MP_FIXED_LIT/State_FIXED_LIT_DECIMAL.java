package MP_FIXED_LIT;

import regex.State;

public class State_FIXED_LIT_DECIMAL extends State {
    private static State state;
    private State_FIXED_LIT_DECIMAL() {
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_FIXED_LIT_DECIMAL();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(Character.isDigit(c)) {
                context.changeState(State_FIXED_LIT_FRACTIONAL.getState());
            }
             else {
	            context.changeState(regex.InvalidState.getState());
	        }
        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}