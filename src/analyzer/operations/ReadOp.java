package analyzer.operations;

import analyzer.Analyzer;
import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public class ReadOp extends Operator {
    public ReadOp(Type[] types) {
        super(types);
    }
    @Override
    protected String Op(Argument leftArg, Argument rightArg, String label) throws SemanticException {
        // assert that only the appropriate parameters are defined. e.g. leftArg is used and the others are null
        assertValue(leftArg, true);
        assertValue(rightArg, false);
        assertValue(label, false);

        switch (leftArg.getType()) {
            case INTEGER:
                return String.format("RD %s", leftArg);
            case FLOAT:
                return String.format("RDF %s",leftArg);
            case STRING:
                return String.format("RDF %s",leftArg);
            default:
                throw new SemanticException("Unrecognized type: "+leftArg.getType());
        }
    }
}