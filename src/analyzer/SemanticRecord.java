package analyzer;

import analyzer.operations.Operator;
import com.sun.org.apache.xpath.internal.Arg;
import core.Token;
import sun.net.www.content.text.plain;
import symbolTable.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Christina on 4/14/2015.
 */
public class SemanticRecord {
    private Operator operator;
    private Type type;
    private Argument leftOperand;
    private Argument rightOperand;
    private String label;
    private Token token;

    public SemanticRecord(Argument leftOperand, Token operator, Argument rightOperand) {
        token = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.label = null;
        this.operator = Analyzer.opLookup(operator);
        setReturnType(operator);
    }
    public SemanticRecord(Token operator, Argument rightOperand) {
        token = operator;
        this.rightOperand = rightOperand;
        this.operator = Analyzer.opLookup(operator);
        this.label = null;
        this.leftOperand = null;
    }
    // set the operator directly
    public SemanticRecord(Argument leftOperand, Operator operator, Argument rightOperand, Type type) {
        this.rightOperand = rightOperand;
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.type = type;
    }
    public SemanticRecord() {
        operator = null;
        type = null;
        leftOperand = null;
        rightOperand = null;
        label = null;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public void setLabel(String label) { this.label = label;}

    public String getLabel() { return label;}

    public Argument getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(Argument leftOperand) {
        this.leftOperand = leftOperand;
        setReturnType(token);
    }

    public Argument getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Argument rightOperand) {
        this.rightOperand = rightOperand;
    }

    public void genCasts() {
        if(type!=Type.BOOLEAN && type!=null) {
            if(rightOperand!=null)
            if (rightOperand.getType() != type) {
                rightOperand.castType(type);
            }
            if(leftOperand!=null)
            if (leftOperand.getType() != type) {
                leftOperand.castType(type);
            }
        }
    }

    private void setReturnType(Token operator) {
        if(operator==null || leftOperand == null)
            return;
        switch (operator) {
            case MP_MINUS:
            case MP_PLUS:
            case MP_TIMES:
            case MP_MOD:
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
            case MP_DIV:
                type = Type.INTEGER;
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
        if (this.token == null)
            return false;
        else return this.token.isAtomic();
    }
    public String toString() {
        return String.format("Operator %s from Token %s, Type %s. Left arg %s, Right Arg %s ", operator, token, type, leftOperand, rightOperand);
    }
    public void printRecord() {
        System.out.println(this);
    }

    public Argument genExpression() throws SemanticException {
        genCasts();
        operator.performOp(leftOperand,rightOperand,label);
        return new Argument(type);
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
