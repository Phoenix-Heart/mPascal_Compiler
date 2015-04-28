package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import com.sun.org.apache.xpath.internal.Arg;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class WriteLnOp extends Operator {
    @Override
    public Type performOp(Argument leftArg, Argument rightArg) throws SemanticException {
        if(rightArg.symbol!=null) {
            throw new SemanticException("Invalid number of arguments. Expected 1, Received 2");
        }
        if(leftArg.inStack) {
            putLine("WRTLNS " + leftArg.symbol);
        }
        else {
            putLine("WRTLN " + leftArg.symbol);
        }
        return leftArg.type;
    }
}