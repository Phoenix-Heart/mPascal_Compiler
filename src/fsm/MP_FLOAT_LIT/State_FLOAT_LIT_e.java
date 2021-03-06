//Created by Hunter Lapp
package fsm.MP_FLOAT_LIT;

import core.State;

public class State_FLOAT_LIT_e extends State {
    private static State state;
    private State_FLOAT_LIT_e() {
        state = this;                   // added by Christina
    }
    // states are singletons
    public static State getState() {
        if(state==null) {
            return new State_FLOAT_LIT_e();
        }
        else return state;
    }

        @Override
        public void read(char c) {
            if(c == '+' || c == '-') {
                context.changeState(State_FLOAT_LIT_SIGN.getState());
            }
             else {
	            context.setInvalid();
	        }
        }

	        @Override
	        public boolean accepted() {
	            return false;
	        }
}