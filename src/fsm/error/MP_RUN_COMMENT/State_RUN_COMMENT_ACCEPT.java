package fsm.error.MP_RUN_COMMENT;

import core.State;
import core.keystates.AcceptState;

/**
 * Created by night on 2/16/2015.
 */
public class State_RUN_COMMENT_ACCEPT extends AcceptState {
    private static State state;
    private State_RUN_COMMENT_ACCEPT() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_RUN_COMMENT_ACCEPT();
        }
        else return state;
    }
    @Override
    public void read(char c) {
        context.setInvalid();
    }
}
