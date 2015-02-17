package core.select;

import core.Context;
import core.Selector;
import core.Token;
import fsm.MP_FIXED_LIT.State_FIXED_LIT_START;
import fsm.MP_FLOAT_LIT.State_FLOAT_LIT_START;
import fsm.MP_INTEGER_LIT.State_INTEGER_LIT_START;
import fsm.MP_STRING_LIT.State_STRING_LIT_START;
import fsm.error.MP_RUN_STRING.State_RUN_STRING_START;

import java.util.ArrayList;

/**
 * Created by Christina Dunning on 2/6/2015.
 */
public class specialSelector extends Selector {
    public specialSelector() {
        ArrayList<Context> machines = new ArrayList<Context>();
        machines.add( new Context(":=", Token.MP_ASSIGN));
        machines.add( new Context(":", Token.MP_COLON));
        machines.add( new Context(",", Token.MP_COMMA));
        machines.add( new Context("=", Token.MP_EQUAL));
        machines.add( new Context("/", Token.MP_FLOAT_DIVIDE));
        machines.add( new Context(">=", Token.MP_GEQUAL));
        machines.add( new Context(">", Token.MP_GTHAN));
        machines.add( new Context("<=", Token.MP_LEQUAL));
        machines.add( new Context("(", Token.MP_LPAREN));
        machines.add( new Context("<", Token.MP_LTHAN));
        machines.add( new Context("-", Token.MP_MINUS));
        machines.add( new Context("<>", Token.MP_NEQUAL));
        machines.add( new Context(".", Token.MP_PERIOD));
        machines.add( new Context("+", Token.MP_PLUS));
        machines.add( new Context(")", Token.MP_RPAREN));
        machines.add( new Context(";", Token.MP_SCOLON));
        machines.add( new Context("*", Token.MP_TIMES));
        machines.add(new Context(State_FIXED_LIT_START.getState(), Token.MP_FIXED_LIT));
        machines.add(new Context(State_FLOAT_LIT_START.getState(),Token.MP_FLOAT_LIT));
        machines.add(new Context(State_INTEGER_LIT_START.getState(), Token.MP_INTEGER_LIT));
        machines.add(new Context(State_STRING_LIT_START.getState(), Token.MP_STRING_LIT));
        machines.add(new Context(State_RUN_STRING_START.getState(),Token.MP_RUN_STRING));

        super.populate(machines);
    }
}
