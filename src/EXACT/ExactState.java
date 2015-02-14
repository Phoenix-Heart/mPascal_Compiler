package EXACT;

import regex.InvalidState;
import regex.State;

import java.util.LinkedList;

/**
 * Created by Christina on 2/5/2015.
 * Discovers an exact match by tracking state internally.
 */
public class ExactState extends State {
    LinkedList<Character> states;
    public ExactState(String name) {
        states = new LinkedList<Character>();
        for(char c : name.toCharArray()) {
            states.add(new Character(c));
        }
    }
    public void read(char c) {
        if(states.isEmpty()) {
            changeState(InvalidState.getState());
        }
        else if(!states.remove().equals(c)) {
            changeState(InvalidState.getState());
        }
    }

    @Override
    public boolean accepted() {
        return (states.isEmpty());
    }
}
