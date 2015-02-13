package regex;

import MP_IDENTIFIER.State_ID_Empty;

import java.util.ArrayList;

/**
 * Created by night on 2/5/2015.
 * Aggregates the FSMs and selects an appropriate token at end of input
 */
public class Selector {
    private ArrayList<Context> machines;
    private ArrayList<Context> validMachines;

    protected void populate(ArrayList<Context> machines) {
        this.validMachines = new ArrayList<Context>();
        validMachines.addAll(machines);
    }
    // return true if some valid token exists
    public boolean read(char ch) {
        ArrayList<Context> invalids = new ArrayList<Context>();
        // run the next symbol in each FSM and remove any FSM from the list which reaches an invalid state.
        for(Context fsm : validMachines) {
            fsm.read(ch);
            if(fsm.invalid()) invalids.add(fsm);
        }
        validMachines.removeAll(invalids);
        if(validMachines.isEmpty()) return false;
        else return true;
    }
    public Token getToken() {
        Token token;
        if(validMachines.isEmpty()) {
            token = null;
        }
        else {
            token = validMachines.get(0).getToken();
        }
        return token;
    }

    public boolean hasToken() {
        return !validMachines.isEmpty();
    }
}
