package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class AssignOp extends Operator {
    @Override
    public Type performOp(Argument leftArg, Argument rightArg) throws SemanticException {
        if(leftArg.symbol==null || rightArg.symbol==null) {
            throw new SemanticException("Invalid argument");
        }
        if(!rightArg.inStack) {
            // cast
            putLine("PUSH "+leftArg.symbol);
        }
        else {
            putLine(String.format("POP %s", leftArg.symbol));
        }
        return leftArg.type;
    }
}