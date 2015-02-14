package regex;

import MP_IDENTIFIER.State_ID_Empty;

import java.util.ArrayList;

/**
 * Created by Christina on 2/6/2015.
 */
public class alphaSelector extends Selector {
    public alphaSelector() {
        ArrayList<Context> machines = new ArrayList<Context>();

        machines.add( new Context("and", Token.MP_AND));
        machines.add( new Context("begin", Token.MP_BEGIN));
        machines.add( new Context("boolean", Token.MP_BOOLEAN));
        machines.add( new Context("div", Token.MP_DIV));
        machines.add( new Context("do", Token.MP_DO));
        machines.add( new Context("downto", Token.MP_DOWNTO));
        machines.add( new Context("else", Token.MP_ELSE));
        machines.add( new Context("end", Token.MP_END));
        machines.add( new Context("false", Token.MP_FALSE));
        machines.add( new Context("fixed", Token.MP_FIXED));
        machines.add( new Context("float", Token.MP_FLOAT));
        machines.add( new Context("for", Token.MP_FOR));
        machines.add( new Context("function", Token.MP_FUNCTION));
        machines.add( new Context("if", Token.MP_IF));
        machines.add( new Context("integer", Token.MP_INTEGER));
        machines.add( new Context("mod", Token.MP_MOD));
        machines.add( new Context("not", Token.MP_NOT));
        machines.add( new Context("or", Token.MP_OR));
        machines.add( new Context("procedure", Token.MP_PROCEDURE));
        machines.add( new Context("program", Token.MP_PROGRAM));
        machines.add( new Context("read", Token.MP_READ));
        machines.add( new Context("repeat", Token.MP_REPEAT));
        machines.add( new Context("string", Token.MP_STRING));
        machines.add( new Context("then", Token.MP_THEN));
        machines.add( new Context("true", Token.MP_TRUE));
        machines.add( new Context("to", Token.MP_TO));
        machines.add( new Context("type", Token.MP_TYPE));
        machines.add( new Context("until", Token.MP_UNTIL));
        machines.add( new Context("var", Token.MP_VAR));
        machines.add( new Context("while", Token.MP_WHILE));
        machines.add( new Context("write", Token.MP_WRITE));
        machines.add( new Context("writeln", Token.MP_WRITELN));

        machines.add( new Context(State_ID_Empty.getState(), Token.MP_IDENTIFIER));

        super.populate(machines);
    }
}
