package MP_STRING_LIT;

import regex.State;
import MP_STRING_LIT.State_STRING_LIT_ACCEPT;


public class State_STRING_LIT_MIDDLE extends State {
    private static State state;
    private State_STRING_LIT_MIDDLE() {
        state = this;                           // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_STRING_LIT_MIDDLE();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(c == '\n' || c =='\r') {
                context.changeState(regex.InvalidState.getState());
            }
             else {
            	 if (c == '\'')	{
	            context.changeState(State_STRING_LIT_ACCEPT.getState());
            	 }
            	 //else {
                     // do not need to change state
            		 //context.changeState(State_STRING_LIT_MIDDLE.getState());
            	 //}
	        }
        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}
