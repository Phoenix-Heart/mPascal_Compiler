package MP_IDENTIFIER;

import regex.State;

/**
 * Created by night on 2/5/2015.
 */
public class State_ID_Empty extends State {
    private static State state;
    private State_ID_Empty() {
        state = this;                   // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_ID_Empty();
        }
        else return state;
    }
    /**
     * Created by Christina on 2/4/2015.
     * Represents a valid identifier that is not and cannot be a reserved word.
     */

        @Override
        public void read(char c) {
            if(Character.isLetter(c)) {
                context.changeState(State_IDENTIFIER.getState());
            }
             else if(c=='_') {
                    context.changeState(State_IDENTIFIER_TRAILING_UNDERSCORE.getState());
            }
        }

        @Override
        public boolean accepted() {
            return false;
        }
    }