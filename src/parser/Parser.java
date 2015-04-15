package parser;

import core.Token;
import scanner.Dispatcher;
import symbolTable.Kind;
import symbolTable.SymbolTable;
import symbolTable.TableEntry;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

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
    private EntryBuilder stackEntry;

    public Parser(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.stack = new LinkedList<SymbolTable>();
        this.parse = "";
        nest = 0;
        tableEntry = new EntryBuilder();
    }
    public void startParse() throws ParseException {
        lookahead = dispatcher.nextToken();
        SystemGoal();
        System.out.println(parse);
        saveParse();
    }
    // copy trace to file
    public void saveParse() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("parsefile.txt"));
            writer.write("## Trace File ##\n");
            writer.write(parse);
            writer.close();
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
        lookahead = dispatcher.nextToken();
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
        if(tableEntry.getName()!=null) {
            stack.push(new SymbolTable(tableEntry.getName(), nest));
            nest++;
        }
        else {
            throw new ParseException("Attempted to insert symbol table without identifier.");
        }
        tableEntry = new EntryBuilder();
    }
    // remove top symbol table from the stack
    private void destroyTable() {
        stack.pop();
        nest--;
    }
    // add a row to the symbol table using information retrieved during parse.
    private void addEntry() {
        // need to fill out logic, catch all errors & ensure program,procedure,function preserve entry for new table creation.
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
            tableEntry = new EntryBuilder();
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

    // section written by Andrew

    private void SystemGoal() throws ParseException {
        parseTree("1");
        Program();
    }
    private void Program() throws ParseException {
        ProgramHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_PERIOD);
    }
    private void ProgramHeading() throws ParseException {
        parseTree("2");
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

    // section written by Christina

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
    private void ReadParameter() throws ParseException {
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
    private void AssignmentStatement() throws ParseException {
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
    private void ControlVariable() throws ParseException {
        // complete
        parseTree("62");
        VariableIdentifier();
    }
    private void InitialValue() throws ParseException {
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
    private void FinalValue() throws ParseException {
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

    // section written by Hunter

    private void OptionalSign() {}
    private void AddingOperator() throws ParseException{
        switch(lookahead){
            case MP_PLUS:
                parseTree("88");
                matchLookAhead(Token.MP_PLUS);
                break;
            case MP_MINUS:
                parseTree("89");
                matchLookAhead(Token.MP_MINUS);
                break;
            default:
                LL1error();
        }
    }

    private void Term() throws ParseException{
        parseTree("91");
        Factor();
        FactorTail();
    }
    private void FactorTail() throws ParseException
    {
        switch(lookahead){
            case MP_FLOAT_DIVIDE:
            case MP_TIMES:
            case MP_MOD:
            case MP_AND:
                parseTree("92");
                MultiplyingOperator();
                Factor();
                FactorTail();
                break;
            case MP_DO:
            case MP_DOWNTO:
            case MP_ELSE:
            case MP_END:
            case MP_OR:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:
            case MP_EQUAL:
            case MP_GTHAN:
            case MP_LTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
            case MP_PLUS:
            case MP_MINUS:
                parseTree("93");
                break;
            default:
                LL1error();
        }
    }
    private void MultiplyingOperator() throws ParseException
    {
        switch(lookahead){
            case MP_TIMES:
                parseTree("94");
                matchLookAhead(Token.MP_TIMES);
                break;
            case MP_FLOAT_DIVIDE:
                parseTree("95");
                matchLookAhead(Token.MP_FLOAT_DIVIDE);
                break;
            case MP_DIV:
                parseTree("96");
                matchLookAhead(Token.MP_DIV);
                break;
            case MP_MOD:
                parseTree("97");
                matchLookAhead(Token.MP_MOD);
                break;
            case MP_AND:
                parseTree("98");
                matchLookAhead(Token.MP_AND);
                break;

            default:
                LL1error();
        }
    }

    private void Factor() throws ParseException
    {
        switch(lookahead){
            case MP_NOT:
                parseTree("104");
                matchLookAhead(Token.MP_NOT);
                Factor();
                break;
            case MP_LPAREN:
                parseTree("105");
                matchLookAhead(Token.MP_LPAREN);
                Expression();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_INTEGER_LIT:
                parseTree("99");
                matchLookAhead(Token.MP_INTEGER_LIT);
                break;
            case MP_FIXED_LIT:
                parseTree("?");
                matchLookAhead(Token.MP_FIXED_LIT);
                break;
            case MP_FLOAT_LIT:
                parseTree("100");
                matchLookAhead(Token.MP_FLOAT_LIT);
                break;
            case MP_TRUE:
                parseTree("102");
                break;
            case MP_FALSE:
                parseTree("103");
                break;
            case MP_IDENTIFIER:
                parseTree("106");
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setName(dispatcher.getLexeme());
                    tableEntry.setKind(Kind.PROGRAM);
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                break;
            default:
                LL1error();
        }
    }
    private void ProgramIdentifier() throws ParseException {
        parseTree("107");
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void VariableIdentifier() throws ParseException {
        parseTree("108");
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void ProcedureIdentifier() throws ParseException {
        parseTree("109");
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void FunctionIdentifier() throws ParseException {
        parseTree("110");
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);}
    private void BooleanExpression() throws ParseException {
        parseTree("111");
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);}
    private void OrdinalExpression() throws ParseException {
        parseTree("112");
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setName(dispatcher.getLexeme());
            tableEntry.setKind(Kind.PROGRAM);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
    }
    private void IdentifierList() throws ParseException {

        switch(lookahead){
            case MP_COMMA:
                parseTree("113");
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setName(dispatcher.getLexeme());
                    tableEntry.setKind(Kind.PROGRAM);
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
                break;
            default:
                LL1error();
        }
    }

    private void IdentifierTail() throws ParseException
    {
        switch(lookahead){
            case MP_COMMA:
                parseTree("114");
                matchLookAhead(Token.MP_COMMA);
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setName(dispatcher.getLexeme());
                    tableEntry.setKind(Kind.PROGRAM);
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
                break;
            case MP_COLON:
                parseTree("115");
                break;
            default:
                LL1error();
        }
    }
}
