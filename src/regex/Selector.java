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
        this.machines = machines;
        this.validMachines = new ArrayList<Context>();
        validMachines.addAll(machines);
    }
    public void read(char ch) {
        ArrayList<Context> invalids = new ArrayList<Context>();
        // run the next symbol in each FSM and remove any FSM from the list which reaches an invalid state.
        for(Context fsm : validMachines) {
            if(!fsm.read(ch))
                invalids.add(fsm);
        }
        validMachines.removeAll(invalids);
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
    public void reset() {
        // reset the machines after a token is extracted, preparing for the next token.
        validMachines = new ArrayList<Context>();
        validMachines.addAll(machines);
    }
}
