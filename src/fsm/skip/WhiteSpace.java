package fsm.skip;

import core.State;
import core.keystates.AcceptState;
import scanner.Dispatcher;

/**
 * Created by Christina Dunning on 2/16/2015.
 */

public class WhiteSpace extends AcceptState {
    private static State state;
    private WhiteSpace() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new WhiteSpace();
        }
        else return state;
    }
    @Override
    public void read(char c) {
        if (!Character.isWhitespace(c)) context.setInvalid();
    }
}
