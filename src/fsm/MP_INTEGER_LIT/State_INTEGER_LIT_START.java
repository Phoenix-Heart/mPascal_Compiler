// Written by Hunter
package fsm.MP_INTEGER_LIT;

import core.State;

public class State_INTEGER_LIT_START extends State {
    private static State state;
    private State_INTEGER_LIT_START() {
        state = this;                           // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_INTEGER_LIT_START();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(Character.isDigit(c)) {
                context.changeState(State_INTEGER_LIT.getState());
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


