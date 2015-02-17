package core;

/**
 * Created by Christina on 2/4/2015.
 */
public abstract class State {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }
    public void changeState(State s) {
        s.setContext(context);
        context.changeState(s);
    }

    public abstract void read(char c);
    public abstract boolean accepted();
}