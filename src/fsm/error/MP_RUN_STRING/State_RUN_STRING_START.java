/*
    Created by Christina Dunning
 */

package fsm.error.MP_RUN_STRING;

import core.State;


public class State_RUN_STRING_START extends State {
    private static State state;
    private State_RUN_STRING_START() {
        state = this;                           // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_RUN_STRING_START();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(c == '\'') {
                context.changeState(State_RUN_STRING_MIDDLE.getState());
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
