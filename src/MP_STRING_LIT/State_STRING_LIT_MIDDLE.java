package MP_STRING_LIT;

import regex.State;
import MP_STRING_LIT.State_STRING_LIT_ACCEPT;


public class State_STRING_LIT_MIDDLE extends State {
    private static State state;
    private State_STRING_LIT_MIDDLE() {
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
            if(c == '\'' || c == '\n' || c =='\r') {
                context.changeState(regex.InvalidState.getState());
            }
             else {
            	 if (c == '\"')	{
	            context.changeState(State_STRING_LIT_ACCEPT.getState());
            	 }
            	 else {
            		 context.changeState(State_STRING_LIT_MIDDLE.getState());
            	 }
	        }
        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}
