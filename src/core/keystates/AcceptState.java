package core.keystates;

import core.State;

/**
 * Created by Christina Dunning on 2/16/2015.
 */
public abstract class AcceptState extends State {
    @Override
    public boolean accepted() {
        return true;
    }
}
