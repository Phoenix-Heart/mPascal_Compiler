package parser;

import core.Term;
import core.Token;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by night on 3/6/2015.
 */
public class Parser {
    private LinkedList stack;

    public void Parser() {
        stack = new LinkedList<Term>();
    }
    private void push(Term o) {
        stack.push(o);
    }
    private Term pop() {
        return (Term) stack.pop();
    }
    private Term peek() {
        return (Term) stack.peek();
    }
    private void matchLookAhead() {}
    private void VariableDeclaration() {}
    private void VariableDeclarationPart() throws ParseException {
        push(Term.MP_VAR);
        push(Term.VariableDeclaration);
        push(Term.MP_SCOLON);
        push(Term.VariableDeclarationTail);
        VariableDeclarationTail();
        matchLookAhead();
        VariableDeclaration();
        matchLookAhead();
    }

    private void VariableDeclarationTail() throws ParseException {
        if(!peek().equals(Term.VariableDeclarationTail)) {
            throw new ParseException("VariableDeclarationTail not found.");
        }
        pop();
        push(Term.VariableDeclaration);
        push(Term.MP_SCOLON);
    }


    private void Type() {    }
    private void ProcedureDeclaration() {    }
    private void FunctionDeclaration() {    }
    private void ProcedureHeading() {    }

    private void FunctionHeading() {         }
    private void OptionalFormalParameterList() {    }
    private void FormalParameterSectionTail() {    }
    private void FormalParameterSection() {    }
    private void ValueParameterSection() {}
    private void VariableParameterSection() {    }
    private void StatementPart() {    }
    private void CompoundStatement() {    }
    private void StatementSequence() {    }
    private void StatementTail() {    }
    private void Statement() {    }
    private void EmptyStatement() {    }
    private void ReadStatement() {    }
    private void ReadParameterTail() {    }
    private void WriteStatement() {    }
    private void WriteParameterTail() {    }
    private void WriteParametter() {    }
    private void IfStatement() {    }
    private void OptionalElsePart() {}
    private void RepeatStatement() {}
    private void WhileStatement() {}
    private void ForStatement() {}
    private void ControlVariable() {}
    private void InitialValue() {}
    private void StepValue() {}
    private void FinalValue() {}
    private void ProcedureStatement() {}
    private void OptionalActualParameterList() {}
    private void ActualParameterTail() {}
    private void ActualParameter() {}
    private void Expression() {}
    private void OptionalRelationalPart() {}
    private void RelationalOperator() {}
    private void SimpleExpression() {}
    private void TermTail() {}
    private void OptionalSign() {}
    private void AddingOperator() {}
    private void Term() {}
    private void FactorTail() {}
    private void MultiplyingOperator() {}
    private void Factor() {}
    private void ProgamIdenifier() {}
    private void VariableIdentifier() {}
    private void ProcedureIdentifier() {}
    private void FunctionIdentifier() {}
    private void BooleanExpression() {}
    private void OrdinalExpression() {}
    private void IdentifierList() {}
    private void IdentifierTail() {}
}
