package EXACT;

import regex.InvalidState;
import regex.State;

/**
 * Created by night on 2/5/2015.
 * Discovers an exact match by tracking state internally.
 */
public class ExactState extends State {
    private final char[] name;
    private int index;
    public ExactState(String name) {
        this.name = name.toCharArray();
        index = 0;
    }
    public void read(char c) {
        if(name.length>index-1) {
            changeState(InvalidState.getState());
        }
        if(name[index]!=c) {
            changeState(InvalidState.getState());
        }
        index++;
    }

    @Override
    public boolean accepted() {
        return (index==name.length);
    }
}
