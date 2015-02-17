package core.select;

import core.Context;
import core.Selector;
import fsm.skip.WhiteSpace;
import fsm.skip.comment.State_COMMENT_EMPTY;

import java.util.ArrayList;

/**
 * Created by Christina Dunning on 2/16/2015.
 */
public class skipSelector extends Selector {
    public skipSelector() {
        ArrayList<Context> machines = new ArrayList<Context>();
        machines.add(new Context(WhiteSpace.getState(), null));
        machines.add(new Context(State_COMMENT_EMPTY.getState(),null));
        super.populate(machines);
    }
}
