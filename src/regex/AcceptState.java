package regex;

/**
 * Created by night on 2/4/2015.
 * A default accept state that can be used for simple Finite State Machines which have a single accept state and no loop behavior.
 */
public class AcceptState extends State {
    private static State state;
    private AcceptState() {
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new AcceptState();
        }
        else {
            return state;
        }
    }
    public boolean accepted() {
        return true;
    }
    // this is a basic Accept state that does not read any more input.
    public void read(char c) {
    }
}
