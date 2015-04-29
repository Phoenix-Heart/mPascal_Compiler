package analyzer;

import analyzer.operations.Operator;
import core.Token;
import sun.net.www.content.text.plain;
import symbolTable.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Christina on 4/14/2015.
 */
public class SemanticRecord {
    private Token operator;
    private Type type;
    private Argument leftOperand;
    private Argument rightOperand;
    private String label;

    public SemanticRecord(Argument leftOperand, Token operator, Argument rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        this.label = null;
        setReturnType();
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public void setLabel(String label) { this.label = label;}

    public String getLabel() { return label;}

    public Argument getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(Argument leftOperand) {
        this.leftOperand = leftOperand;
    }

    public Argument getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Argument rightOperand) {
        this.rightOperand = rightOperand;
    }

    private void setReturnType() {
        switch (operator) {
            case MP_MINUS:
            case MP_PLUS:
            case MP_TIMES:
                if(leftOperand.getType()==Type.FLOAT || rightOperand.getType()==Type.FLOAT
                        || leftOperand.getType()==Type.FIXED || rightOperand.getType()==Type.FIXED) {
                    type = Type.FLOAT;
                }
                else {
                    type = Type.INTEGER;
                }
                break;
            case MP_FLOAT_DIVIDE:
                type = Type.FLOAT;
                break;
            case MP_AND:
            case MP_OR:
            case MP_GTHAN:
            case MP_GEQUAL:
            case MP_EQUAL:
            case MP_NEQUAL:
            case MP_NOT:
            case MP_LTHAN:
            case MP_LEQUAL:
                type = Type.BOOLEAN;
                break;
            // not an expression, does not evaluate to a type
            case MP_ASSIGN:
            case MP_READ:
            case MP_WRITE:
            case MP_WRITELN:
                type = null;
                break;
        }
    }


    public Boolean isSimple()
    {
        if (this.operator == null)
            return false;
        else return this.operator.isAtomic();
    }

    public void printRecord()
    {
        printRecordHelper(this);
    }

    public void printRecordHelper(SemanticRecord op)
    {
        System.out.println("RECORD");
        while(op.operator != null)
        {
            System.out.println(op.operator);
            System.out.println("leftSide");
            System.out.println(leftOperand);
            System.out.println("rightSide");
            System.out.println(rightOperand);
        }
    }

    public Argument genExpression() {
        Operator op = Analyzer.opLookup(operator);
        op.performOp(leftOperand,rightOperand,label);
        return new Argument(type);
    }

}
