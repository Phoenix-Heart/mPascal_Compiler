package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by night on 4/26/2015.
 */
public class WriteOp extends Operator {
    @Override
    public Type performOp(Argument leftArg, Argument rightArg) throws SemanticException {
        if(rightArg.symbol!=null) {
            throw new SemanticException("Invalid number of arguments. Expected 1, Received 2");
        }
        if(leftArg.inStack) {
            putLine("WRTS " + leftArg.symbol);
        }
        else {
            putLine("WRT " + leftArg.symbol);
        }
        return leftArg.type;
    }
}