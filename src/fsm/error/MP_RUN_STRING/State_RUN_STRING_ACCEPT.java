package fsm.error.MP_RUN_STRING;

import core.State;
import core.keystates.AcceptState;

/**
 * Created by night on 2/16/2015.
 */
public class State_RUN_STRING_ACCEPT extends AcceptState {
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
        context.setInvalid();
    }
}
