//Created by Hunter Lapp
package fsm.MP_FIXED_LIT;

import core.State;

public class State_FIXED_LIT_WHOLE extends State {
    private static State state;
    private State_FIXED_LIT_WHOLE() {
        state = this;                   // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_FIXED_LIT_WHOLE();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(Character.isDigit(c)) {
                context.changeState(State_FIXED_LIT_WHOLE.getState());
            }
             else {
            	 if (c =='.')
	            context.changeState(State_FIXED_LIT_DECIMAL.getState());
                else {
                     context.setInvalid();
                 }
	        }

        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}
