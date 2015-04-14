package parser;

import core.Token;
import scanner.Dispatcher;
import symbolTable.Kind;
import symbolTable.SymbolTable;
import symbolTable.TableEntry;
import symbolTable.Type;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Christina on 3/6/2015.
 */
public class Parser {

    private Dispatcher dispatcher;
    private Token lookahead;
    private LinkedList<SymbolTable> stack;
    private EntryBuilder tableEntry;
    private String parse;
    private int nest;

    public Parser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.stack = new LinkedList<SymbolTable>();
        this.parse = "";
        nest = 0;
        tableEntry = new EntryBuilder();
    }
    public void startParse() throws ParseException {
        lookahead = dispatcher.getToken();
        SystemGoal();
        System.out.println(parse);
        saveParse();
    }
    // copy trace to file
    private void saveParse() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("parsefile.txt"));
            writer.write(parse);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // assert token is a match.
    private void matchLookAhead(Token token) throws ParseException {

        if(lookahead!=token) {
            throw new ParseException(String.format("Parse error on line %s, col %s. Found %s, expected %s.", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getLexeme(), token.name()));
        }
        lookahead = dispatcher.getToken();
    }
    // generic error message when an unexpected token is discovered.
    private void LL1error() throws ParseException {
        throw new ParseException(String.format("Parse error on line %s, col %s. Unexpected token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getLexeme(), lookahead.name()));
    }
    // get entry to use during identifier conflict resolution
    private TableEntry getEntry(String lexeme) {
        Iterator<SymbolTable> iter = stack.descendingIterator();
        SymbolTable table;
        while(iter.hasNext()) {
            table = iter.next();
            if(table.hasEntry(lexeme))
            return table.getEntry(lexeme);
        }
        return null;
    }
    // create a new symbol table and add it to the stack
    private void createTable() throws ParseException {
        if(dispatcher.getToken()==Token.MP_IDENTIFIER) {
            stack.push(new SymbolTable(dispatcher.getLexeme(), nest));
            nest++;
        }
        else {
            throw new ParseException("Attempted to insert symbol table without identifier.");
        }
    }
    // remove top symbol table from the stack
    private void destroyTable() {
        stack.pop();
        nest--;
    }
    // add a row to the symbol table using information retrieved during parse.
    private void addEntry() {
        SymbolTable table = stack.peek();
        if(tableEntry.getName()==null) {
            System.out.println("Attempted to create unnamed symbol");
        }
        else if(tableEntry.getKind()==null) {
            System.out.println("Attempted to create unidentified symbol");
        }
        else if(tableEntry.getKind()==Kind.FUNCTION) {
            if(tableEntry.getMode()==null) {
                System.out.println("Mode missing in function identifier");
            }
            else {
                table.createNewEntry(tableEntry.getName(), tableEntry.getType(), tableEntry.getKind(), tableEntry.getMode(), tableEntry.getParams());
            }
        }
        else {
            table.createNewEntry(tableEntry.getName(), tableEntry.getType(), tableEntry.getKind(), tableEntry.getMode(), tableEntry.getParams());
        }
    }


    // save parse tree rules to file
    private void parseTree(String rule) {
        parse += rule;
        parse += " ";

    }

    /*************************************************
        Parse Stubs Below
     ************************************************/

    private void SystemGoal() throws ParseException {
        Program();
    }
    private void Program() throws ParseException {
        ProgramHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_PERIOD);
    }
    private void ProgramHeading() throws ParseException {
        matchLookAhead(Token.MP_PROGRAM);
        ProgramIdentifier();
        createTable();
    }
    private void Block() {}
    private void VariableDeclaration() {}
    private void VariableDeclarationPart() throws ParseException {
    }

    private void VariableDeclarationTail() throws ParseException {
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
    private void StatementTail() {
        switch (lookahead) {
            case MP_AND:
                break;
            default:
                break;
        }
    }
    private void Statement() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree("34");
                EmptyStatement();
                break;
            case MP_BEGIN:
                parseTree("35");
                CompoundStatement();
                break;
            case MP_READ:
                parseTree("36");
                ReadStatement();
                break;
            case MP_WRITE:
                parseTree("37");
                WriteStatement();
                break;
            case MP_IDENTIFIER:
                // Identifier kind conflict resolution
                String lex = dispatcher.getLexeme();
                TableEntry entry = getEntry(lex);
                if(entry.kind == Kind.PROCEDURE)
                    ProcedureStatement();   // procedure identifier
                else if(entry.kind == Kind.FUNCTION || entry.kind == Kind.VARIABLE) {
                    parseTree("38");
                    AssignmentStatement();
                }

                break;
            case MP_FOR:
                parseTree("42");
                ForStatement();
                break;
            case MP_IF:
                parseTree("39");
                IfStatement();
                break;
            case MP_REPEAT:
                parseTree("41");
                RepeatStatement();
                break;
            case MP_WHILE:
                parseTree("40");
                WhileStatement();
                break;

            default:
                LL1error();
        }

    }

    private void EmptyStatement() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree("44");
                break;
            default:
                LL1error();
        }
    }
    private void ReadStatement() throws ParseException {
        // complete
        parseTree("45");
        matchLookAhead(Token.MP_READ);
        matchLookAhead(Token.MP_LPAREN);
        ReadParameter();
        ReadParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ReadParameter() {
        // complete
        parseTree("48");
        VariableIdentifier();
    }
    private void ReadParameterTail() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
                parseTree("46");
                matchLookAhead(Token.MP_COMMA);
                ReadParameter();
                ReadParameterTail();
                break;
            case MP_RPAREN:
                parseTree("47");
                break;
            default:
                LL1error();
        }}
    private void WriteStatement() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_WRITE:
                parseTree("49");
                matchLookAhead(Token.MP_WRITE);
                matchLookAhead(Token.MP_LPAREN);
                WriteParameter();
                WriteParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_WRITELN:
                parseTree("50");
                matchLookAhead(Token.MP_WRITELN);
                matchLookAhead(Token.MP_LPAREN);
                WriteParameter();
                WriteParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            default:
                LL1error();
        }

    }
    private void WriteParameterTail() throws ParseException {
        // complete
    switch (lookahead) {
        case MP_COMMA:
            parseTree("51");
            matchLookAhead(Token.MP_COMMA);
            WriteParameter();
            WriteParameterTail();
        case MP_RPAREN:
            parseTree("52");
            break;
        default:
            LL1error();
    }
    }
    private void WriteParameter() throws ParseException {
        // complete
        parseTree("53");
        OrdinalExpression();
    }
    private void AssignmentStatement() {
        // complete
        String lex = dispatcher.getLexeme();
        TableEntry entry = getEntry(lex);
        if(entry.kind == Kind.FUNCTION) {
            parseTree("55");
            FunctionIdentifier();
        }
        else if (entry.kind == Kind.VARIABLE) {
            parseTree("54");
            VariableIdentifier();
        }
        else {
            System.out.println(" (AssignmentStatement) This code should be unreachable.");
        }
    }
    private void IfStatement() throws ParseException {
        // complete
        parseTree("56");
        matchLookAhead(Token.MP_IF);
        BooleanExpression();
        matchLookAhead(Token.MP_THEN);
        Statement();
        OptionalElsePart();
    }
    private void OptionalElsePart() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_ELSE:
                /* using greedy/nearest match for conflict resolution */
                parseTree("57");
                matchLookAhead(Token.MP_ELSE);
                Statement();
                break;
            case MP_SCOLON:
            case MP_END:
            case MP_UNTIL:
                parseTree("58");
                break;
            default:
                LL1error();
        }
    }
    private void RepeatStatement() throws ParseException {
        // complete
        parseTree("59");
        matchLookAhead(Token.MP_REPEAT);
        StatementSequence();
        matchLookAhead(Token.MP_UNTIL);
        BooleanExpression();
    }
    private void WhileStatement() throws ParseException {
        // complete
        parseTree("60");
        matchLookAhead(Token.MP_WHILE);
        BooleanExpression();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ForStatement() throws ParseException {
        // complete
        parseTree("61");
        matchLookAhead(Token.MP_FOR);
        ControlVariable();
        matchLookAhead(Token.MP_ASSIGN);
        InitialValue();
        StepValue();
        FinalValue();
        matchLookAhead(Token.MP_DO);
        Statement();
    }
    private void ControlVariable() {
        // complete
        parseTree("62");
        VariableIdentifier();
    }
    private void InitialValue() {
        // complete
        parseTree("63");
        OrdinalExpression();
    }
    private void StepValue() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_TO:
                parseTree("64");
                matchLookAhead(Token.MP_TO);
                break;
            case MP_DOWNTO:
                parseTree("65");
                matchLookAhead(Token.MP_DOWNTO);
                break;
            default:
                LL1error();
        }
    }
    private void FinalValue() {
        // complete
        parseTree("66");
        OrdinalExpression();
    }
    private void ProcedureStatement() throws ParseException {
        // complete
        parseTree("67");
        ProcedureIdentifier();
        OptionalActualParameterList();
    }
    private void OptionalActualParameterList() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_LPAREN:
                parseTree("68");
                matchLookAhead(Token.MP_LPAREN);
                ActualParameter();
                ActualParameterTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_SCOLON:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_END:
                parseTree("69");
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameterTail() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
                parseTree("70");
                matchLookAhead(Token.MP_COMMA);
                ActualParameter();
                ActualParameterTail();
                break;
            case MP_RPAREN:
                parseTree("71");
                break;
            default:
                LL1error();
        }
    }
    private void ActualParameter() throws ParseException {
        // complete
        parseTree("72");
        OrdinalExpression();
    }
    private void Expression() throws ParseException {
        // complete
        parseTree("73");
        SimpleExpression();
        OptionalRelationalPart();
    }
    private void OptionalRelationalPart() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_LPAREN:
            case MP_DOWNTO:
            case MP_DO:
            case MP_END:
            case MP_ELSE:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                parseTree("75");
                break;
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
                parseTree("74");
                RelationalOperator();
                SimpleExpression();
                break;
            default:
                LL1error();
        }
    }
    private void RelationalOperator() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_EQUAL:
                parseTree("76");
                matchLookAhead(Token.MP_EQUAL);
                break;
            case MP_LTHAN:
                parseTree("77");
                matchLookAhead(Token.MP_LTHAN);
                break;
            case MP_GTHAN:
                parseTree("78");
                matchLookAhead(Token.MP_GTHAN);
                break;
            case MP_LEQUAL:
                parseTree("79");
                matchLookAhead(Token.MP_LEQUAL);
                break;
            case MP_GEQUAL:
                parseTree("80");
                matchLookAhead(Token.MP_GEQUAL);
                break;
            case MP_NEQUAL:
                parseTree("81");
                matchLookAhead(Token.MP_NEQUAL);
                break;
            default:
                LL1error();
        }
    }
    private void SimpleExpression() throws ParseException {
        // complete
        parseTree("82");
        OptionalSign();
        Term();
        TermTail();
    }
    private void TermTail() throws ParseException {
        // complete
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
            case MP_DO:
            case MP_DOWNTO:
            case MP_ELSE:
            case MP_END:
            case MP_OR:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                parseTree("84");
                break;
            case MP_PLUS:
            case MP_MINUS:
                parseTree("83");
                AddingOperator();
                Term();
                TermTail();
                break;
            default:
                LL1error();
        }
    }
    private void OptionalSign() {}
    private void AddingOperator() {}
    private void Term() {}
    private void FactorTail() {}
    private void MultiplyingOperator() {}
    private void Factor() {}
    private void ProgramIdentifier() {}
    private void VariableIdentifier() {}
    private void ProcedureIdentifier() {}
    private void FunctionIdentifier() {}
    private void BooleanExpression() {}
    private void OrdinalExpression() {}
    private void IdentifierList() {}
    private void IdentifierTail() {}
}
