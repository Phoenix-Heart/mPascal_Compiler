/*
    Created By Christina Dunning
 */
package fsm.error.MP_RUN_STRING;

import core.State;


public class State_RUN_STRING_MIDDLE extends State {
    private static State state;
    private State_RUN_STRING_MIDDLE() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_RUN_STRING_MIDDLE();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(c == '\n' || c =='\r') {
                context.changeState(State_RUN_STRING_ACCEPT.getState());
            }
            else if(c=='\'') {
                context.setInvalid();
            }
        }

	        @Override
	        public boolean accepted() {
	            return true;
	        }
}
