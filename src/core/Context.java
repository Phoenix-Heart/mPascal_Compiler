package core;

import core.keystates.InvalidState;
import fsm.EXACT.ExactState;

/**
 * Created by Christina on 2/4/2015.
 */
public class Context {
    Token token;
    State current;
    State invalid;
    // create the context for an FSM with the given start state.
    public Context(State start, Token token) {
        current = start;
        current.setContext(this);
        this.token = token;
        invalid = InvalidState.getState();
    }
    // create the context for an FSM which matches the string exactly.
    public Context( String match, Token token) {
        invalid = InvalidState.getState();
        current = new ExactState(match);
        current.setContext(this);
        this.token = token;
    }
    public void setInvalid() {
        changeState(invalid);
    }

    public void changeState(State state) {
        state.setContext(this);
        current = state;
    }
    public boolean read(char c) {
        current.read(c);
        if(current.equals(invalid)) {
            return false;
        }
        else return true;
    }
    public Token getToken() {
        return token;
    }
    public boolean invalid() {
        if(current.equals(invalid)) {
            return true;
        }
        else
            return false;
    }
    public boolean accepted() {
        return current.accepted();
    }
}