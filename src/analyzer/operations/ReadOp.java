package analyzer.operations;

import analyzer.Analyzer;
import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by night on 4/26/2015.
 */
public class ReadOp extends Operator {
    @Override
    public Type performOp(Argument leftArg, Argument rightArg) throws SemanticException {
        if(rightArg!=null) {
            throw new SemanticException("Invalid number of arguments. Expected 1, got 2.");
        }
        switch (leftArg.type) {
            case INTEGER:
                putLine("RD " + leftArg.symbol);
                break;
            case FLOAT:
                putLine("RDF " + leftArg.symbol);
                break;
            case STRING:
                putLine("RDS " + leftArg.symbol);
                break;
            default:
                throw new SemanticException("Unrecognized type: "+leftArg.type);
        }
        return leftArg.type;
    }
}