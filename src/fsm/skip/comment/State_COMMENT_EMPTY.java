package fsm.skip.comment;

import core.State;
import core.keystates.RejectState;

/**
 * Created by Christina Dunning on 2/16/2015.
 */
public class State_COMMENT_EMPTY extends RejectState {
    private static State state;
    private State_COMMENT_EMPTY() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_COMMENT_EMPTY();
        }
        else return state;
    }
    @Override
    public void read(char c) {
        if(c=='{') context.changeState(State_COMMENT_MIDDLE.getState());
        else    context.setInvalid();
    }
}
