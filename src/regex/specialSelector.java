package regex;

import java.util.ArrayList;

/**
 * Created by night on 2/6/2015.
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
        super.populate(machines);
    }
}
