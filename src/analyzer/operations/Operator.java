package analyzer.operations;

import analyzer.Argument;
import analyzer.SemanticException;
import symbolTable.Type;

/**
 * Created by Christina on 4/26/2015.
 */
public abstract class Operator {
    Type[] types;
    public Operator(Type[] types) {
        this.types = types;
    }
    public void performOp(Argument leftArg, Argument rightArg, String label) {
        try {
            if(leftArg!=null)
            checkType(leftArg.getType());
            if(rightArg!=null)
            checkType(rightArg.getType());
            if(leftArg!=null && rightArg != null) {
                if (!leftArg.typeEquals(rightArg))
                    throw new SemanticException(String.format("Incompatible types passed, %s and %s.",leftArg.getType(),rightArg.getType()));
            }
            Op(leftArg, rightArg, label);
        }
        catch (SemanticException e) {
            e.printStackTrace();
        }
    }
    protected abstract void Op(Argument leftArg, Argument rightArg, String label) throws SemanticException;
    private void checkType(Type type) throws SemanticException {
        for(Type t : types) {
            if(t.equals(type)) {
                return;
            }
        }
        throw new SemanticException(String.format("Type mismatch. Type %s is invalid."));
    }
    protected void assertValue(Object obj, boolean exists) throws SemanticException {
        // generate exception when assertion fails. obj is null if and only if exists is false.
        if((obj==null)==exists) {
            throw new SemanticException("Invalid argument given for this Operator. "+obj.toString());
        }
    }
}
