package fsm.error.MP_RUN_STING;

import core.State;
import core.keystates.InvalidState;


public class State_RUN_STRING_ACCEPT extends State {
    private static State state;
    private State_RUN_STRING_ACCEPT() {
        state = this;                           // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_RUN_STRING_ACCEPT();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            context.changeState(InvalidState.getState());
        }

	    @Override
	    public boolean accepted() {
	        return true;
	    }
}
