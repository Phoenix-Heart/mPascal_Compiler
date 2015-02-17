package fsm.MP_IDENTIFIER;

import core.State;

/**
 * Created by Christina on 2/4/2015.
 * Represents a valid identifier that is not and cannot be a reserved word.
 */
public class State_IDENTIFIER_TRAILING_UNDERSCORE extends State {
    private static State state;
    private State_IDENTIFIER_TRAILING_UNDERSCORE() {
        state = this;
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_IDENTIFIER_TRAILING_UNDERSCORE();
        }
        else return state;
    }
    @Override
    public void read(char c) {
        if(!Character.isLetterOrDigit(c)) {

        }
    }

    @Override
    public boolean accepted() {
        return true;
    }
}
