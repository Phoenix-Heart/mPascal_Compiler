package fsm.skip.comment;

import core.State;
import core.keystates.AcceptState;

/**
 * Created by Christina Dunning on 2/16/2015.
 */
public class State_COMMENT_END extends AcceptState {
    private static State state;
    private State_COMMENT_END() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_COMMENT_END();
        }
        else return state;
    }
    @Override
    public void read(char c) {
        context.setInvalid();
    }
}
