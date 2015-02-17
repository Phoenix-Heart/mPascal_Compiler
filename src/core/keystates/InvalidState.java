package core.keystates;

import core.State;

/**
 * Created by Christina on 2/4/2015.
 */
public class InvalidState extends State {
    private static InvalidState state;
    private InvalidState() {
        state = this;
    }
    public static State getState() {
        if(state==null) {
            return new InvalidState();
        }
        else {
            return state;
        }
    }

    @Override
    // this state is invalid, do not need to scan further
    public void read(char c) {
        System.out.println("Attempt to parse in an invalid state.");
    }

    @Override
    public boolean accepted() {
        return false;
    }
}
