package MP_STRING_LIT;

import regex.State;
import MP_STRING_LIT.State_STRING_LIT_ACCEPT;


public class State_STRING_LIT_START extends State {
    private static State state;
    private State_STRING_LIT_START() {
        state = this;                           // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_STRING_LIT_START();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(c == '\'') {
                context.changeState(State_STRING_LIT_MIDDLE.getState());
            }
             else {
	            context.setInvalid();
	        }
        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}
