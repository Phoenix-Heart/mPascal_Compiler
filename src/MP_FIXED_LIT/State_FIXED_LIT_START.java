package MP_FIXED_LIT;

import regex.State;
import MP_FIXED_LIT.State_FIXED_LIT_WHOLE;

public class State_FIXED_LIT_START extends State {
    private static State state;
    private State_FIXED_LIT_START() {
        state = this;                   // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_FIXED_LIT_START();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(Character.isDigit(c)) {
                context.changeState(State_FIXED_LIT_WHOLE.getState());
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


