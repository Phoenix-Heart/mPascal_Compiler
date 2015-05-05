package parser;

import core.Token;
import dispatch.Dispatcher;
import symbolTable.*;
import analyzer.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Christina on 3/6/2015.
 */
public class Parser {

    private Dispatcher dispatcher;
    private Token lookahead;
    private TableStack stack = TableStack.getStack();
    private EntryBuilder tableEntry;
    private String parse;
    private int nest;
    private HashMap rules = RuleLookup.getRules();
    private Analyzer analyzer;
    private String filename;

    public Parser(Dispatcher dispatcher, String filename) {
        this.dispatcher = dispatcher;
        this.parse = "";
        nest = 0;
        tableEntry = new EntryBuilder();
        analyzer = new Analyzer(filename);
        this.filename = filename + ".parse";
    }
    public void startParse() throws ParseException {
        lookahead = dispatcher.nextToken();
        SystemGoal();
        //System.out.println(parse);
        saveParse();
    }
    // copy trace to file
    public void saveParse() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
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
            throw new ParseException(String.format("Parse error on line %s, col %s. Found %s, expected %s.", dispatcher.getLine(), dispatcher.getColumn(),lookahead, token));
        }
        lookahead = dispatcher.nextToken();
    }
    // generic error message when an unexpected token is discovered.
    private void LL1error() throws ParseException {
        throw new ParseException(String.format("Parse error on line %s, col %s. Unexpected token %s, lexeme %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken(), dispatcher.getLexeme()));
    }
    // handles null records in an Expression leaf node
    private Argument getNextArg(Argument nextArg, SemanticRecord record) {
        if(record==null) {
            return nextArg;
        }
        else if(nextArg!=null) {
            record.setLeftOperand(nextArg);
            return genExpression(record);
        }
        else {
            try {
                throw new SemanticException("No record or argument found.");
            } catch (SemanticException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private Argument genExpression(SemanticRecord record) {
        try {
            return record.genExpression();
        } catch (SemanticException e) {
            System.err.println(String.format("Error on line %s, col %s, Token %s, lexeme %s",
                    dispatcher.getLine(),dispatcher.getColumn(), dispatcher.getToken(), dispatcher.getLexeme()));
            System.err.println(record);
            e.printStackTrace();
        }
        return null;
    }

    // save parse tree rules to file
    private void parseTree(int rule) {
        String r = String.valueOf(rules.get(rule));
        parse += rule;
        parse += " ";
        parse += r;
        parse += "\n";
        //System.out.print(rule + " ");
        //System.out.println(r);

    }

    /*************************************************
        Parse Stubs Below
     ************************************************/

    // section written by Andrew

    private void SystemGoal() throws ParseException {
        analyzer.preSetup();
        parseTree(1);
        Program();
        analyzer.closeFile();
        matchLookAhead(Token.MP_EOF);
    }
    private void Program() throws ParseException {
        parseTree(2);
        String label = ProgramHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_PERIOD);
    }
    private String ProgramHeading() throws ParseException {
        parseTree(3);
        tableEntry.setKind(Kind.PROGRAM);
        matchLookAhead(Token.MP_PROGRAM);
        String id = ProgramIdentifier();
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
        return id;
    }
    private void Block() throws ParseException
    {
        parseTree(4);
        VariableDeclarationPart();
        ProcedureAndFunctionDeclarationPart();
        StatementPart();
        // unwind the symbol table stack at the end of a program/function/procedure block
        stack.pop();
    }

    private void VariableDeclarationPart() throws ParseException {
        switch (lookahead) {
            case MP_VAR:
                parseTree(5);
                tableEntry.setKind(Kind.VARIABLE);
                matchLookAhead(Token.MP_VAR);
                VariableDeclaration();
                matchLookAhead(Token.MP_SCOLON);
                VariableDeclarationTail();
                tableEntry = new EntryBuilder();
                break;
            case MP_PROCEDURE:
            case MP_FUNCTION:
            case MP_BEGIN:
                parseTree(6);
                break;
            default:
                LL1error();
        }
    }

    private void VariableDeclarationTail() throws ParseException
    {
        switch(lookahead){
            case MP_IDENTIFIER:
                parseTree(7);
                VariableDeclaration();
                matchLookAhead(Token.MP_SCOLON);
                VariableDeclarationTail();
                break;
            case MP_PROCEDURE:
            case MP_FUNCTION:
            case MP_BEGIN:
                parseTree(8);
                break;
            default:
                 LL1error();
        }
    }

    private void VariableDeclaration() throws ParseException    //need to use a list here for variables and set type for them.
    {
        parseTree(9);
        IdentifierList();
        matchLookAhead(Token.MP_COLON);
        Type();
        stack.addEntry(tableEntry);
        tableEntry = new EntryBuilder();
        tableEntry.setKind(Kind.VARIABLE);
    }

    private void Type() throws ParseException {
        switch (lookahead) {
            case MP_INTEGER:
                parseTree(10);
                tableEntry.setType(Type.INTEGER);
                matchLookAhead(Token.MP_INTEGER);
                break;
            case MP_FLOAT:
                parseTree(11);
                tableEntry.setType(Type.FLOAT);
                matchLookAhead(Token.MP_FLOAT);
                break;
            case MP_STRING:
                parseTree(12);
                tableEntry.setType(Type.STRING);
                matchLookAhead(Token.MP_STRING);
                break;
            case MP_BOOLEAN:
                parseTree(13);
                tableEntry.setType(Type.BOOLEAN);
                matchLookAhead(Token.MP_BOOLEAN);
                break;
            default:
                LL1error();
        }
    }

    private void ProcedureAndFunctionDeclarationPart() throws ParseException {
        switch (lookahead) {
            case MP_PROCEDURE:
                parseTree(14);
                ProcedureDeclaration();
                ProcedureAndFunctionDeclarationPart();
                break;
            case MP_FUNCTION:
                parseTree(15);
                FunctionDeclaration();
                ProcedureAndFunctionDeclarationPart();
                break;
            case MP_BEGIN:
                parseTree(16);
                break;
            default:
                LL1error();
        }
    }

    private void ProcedureDeclaration() throws ParseException
    {
        parseTree(17);
        ProcedureHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_SCOLON);
    }

    private void FunctionDeclaration() throws ParseException
    {
        parseTree(18);
        FunctionHeading();
        matchLookAhead(Token.MP_SCOLON);
        Block();
        matchLookAhead(Token.MP_SCOLON);
    }

    private void ProcedureHeading() throws ParseException
    {
        parseTree(19);
        tableEntry.setKind(Kind.PROCEDURE);
        matchLookAhead(Token.MP_PROCEDURE);
        String id = ProcedureIdentifier();
        OptionalFormalParameterList();
        boolean flag = stack.addEntry(tableEntry);
        if(flag)
        {
            tableEntry = new EntryBuilder();
        }
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
    }

    private void FunctionHeading() throws ParseException
    {
        parseTree(20);
        tableEntry.setKind(Kind.FUNCTION);
        matchLookAhead(Token.MP_FUNCTION);
        String id =  FunctionIdentifier();
        OptionalFormalParameterList();
        matchLookAhead(Token.MP_COLON);
        Type();
        boolean flag = stack.addEntry(tableEntry);
        if(flag)
        {
            tableEntry = new EntryBuilder();
        }
        stack.createTable(tableEntry);
        tableEntry = new EntryBuilder();
    }

    private void OptionalFormalParameterList() throws ParseException
    {
        switch(lookahead){
            case MP_LPAREN:
                parseTree(21);
                matchLookAhead(Token.MP_LPAREN);
                FormalParameterSection();
                FormalParameterSectionTail();
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_COLON:
            case MP_SCOLON:
                parseTree(22);
                break;
            default:
                LL1error();
        }
    }

    private void FormalParameterSectionTail() throws ParseException
    {
        switch(lookahead){
            case MP_SCOLON:
                parseTree(23);
                matchLookAhead(Token.MP_SCOLON);
                FormalParameterSection();
                FormalParameterSectionTail();
                break;
            case MP_RPAREN:
                parseTree(24);
                break;
            default:
                LL1error();
        }
    }

    private void FormalParameterSection() throws ParseException
    {
        switch(lookahead){
            case MP_IDENTIFIER:
                parseTree(25);
                ValueParameterSection();
                break;
            case MP_VAR:
                parseTree(26);
                VariableParameterSection();
                break;
            default:
                LL1error();
        }
    }

    private void ValueParameterSection() throws ParseException
    {
        parseTree(27);
        EntryBuilder temp = tableEntry;
        tableEntry = new EntryBuilder();
        tableEntry.setKind(Kind.PARAMETER);
        IdentifierList();
        matchLookAhead(Token.MP_COLON);
        Type();
        // hacky stuff follows
        int offset = 0;
        for (String lexeme : tableEntry.getLexemes()) {
            temp.addParam(new TableEntry(lexeme, offset, tableEntry));
            offset++;
        }
        tableEntry = temp;
    }

    private void VariableParameterSection() throws ParseException
    {
        switch(lookahead) {
            case MP_VAR:
                parseTree(28);
                EntryBuilder temp = tableEntry;
                tableEntry = new EntryBuilder();
                tableEntry.setKind(Kind.VARIABLE);
                matchLookAhead(Token.MP_VAR);
                IdentifierList();
                matchLookAhead(Token.MP_COLON);
                Type();
                // hacky stuff follows
                int offset = 0;
                for (String lexeme : tableEntry.getLexemes()) {
                    temp.addParam(new TableEntry(lexeme, offset, tableEntry));
                    offset++;
                }
                tableEntry = temp;
                break;
            default:
                LL1error();
        }
    }

    private void StatementPart() throws ParseException
    {
        parseTree(29);
        CompoundStatement();
    }

    private void CompoundStatement() throws ParseException
    {
        parseTree(30);
        matchLookAhead(Token.MP_BEGIN);
        StatementSequence();
        matchLookAhead(Token.MP_END);
    }

    private void StatementSequence() throws ParseException
    {
        parseTree(31);
        Statement();
        StatementTail();
    }

    private void StatementTail() throws ParseException {
        switch (lookahead) {
            case MP_END:
            case MP_UNTIL:
                parseTree(33);
                break;
            case MP_SCOLON:
                parseTree(32);
                matchLookAhead(Token.MP_SCOLON);
                Statement();
                StatementTail();
                break;
            default:
                LL1error();
        }
    }

    // section written by Christina

    private void Statement() throws ParseException {

        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree(34);
                EmptyStatement();
                break;
            case MP_BEGIN:
                parseTree(35);
                CompoundStatement();
                break;
            case MP_READ:
                parseTree(36);
                ReadStatement();
                break;
            case MP_WRITE:
            case MP_WRITELN:
                parseTree(37);
                WriteStatement();
                break;
            case MP_IDENTIFIER:
                // Identifier kind conflict resolution
                TableEntry entry;
                try
                {
                   entry = stack.getEntry(dispatcher.getLexeme());
                } catch (ParseException p)
                {
                    throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }
                try
                {
                    Kind myKind = entry.kind;
                } catch (NullPointerException np)
                {
                    throw new ParseException(String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }

                if(entry.kind == Kind.PROCEDURE) {
                    parseTree(43);
                    ProcedureStatement();   // procedure identifier
                }
                else if(entry.kind == Kind.VARIABLE || entry.kind == Kind.FUNCTION) {
                    parseTree(38);
                    AssignmentStatement();
                }
                break;
            case MP_IF:
                parseTree(39);
                IfStatement();
                break;
            case MP_WHILE:
                parseTree(40);
                WhileStatement();
                break;
            case MP_REPEAT:
                parseTree(41);
                RepeatStatement();
                break;
            case MP_FOR:
                parseTree(42);
                ForStatement();
                break;
            default:
                LL1error();
        }

    }

    private void EmptyStatement() throws ParseException {

        switch (lookahead) {
            case MP_SCOLON:
            case MP_ELSE:
            case MP_END:
            case MP_UNTIL:
                parseTree(44);
                break;
            default:
                LL1error();
        }
    }
    private void ReadStatement() throws ParseException {

        parseTree(45);
        matchLookAhead(Token.MP_READ);
        matchLookAhead(Token.MP_LPAREN);
        ReadParameter();
        ReadParameterTail();
        matchLookAhead(Token.MP_RPAREN);
    }
    private void ReadParameterTail() throws ParseException {

        switch (lookahead) {
            case MP_COMMA:
                parseTree(46);
                matchLookAhead(Token.MP_COMMA);
                ReadParameter();
                ReadParameterTail();
                break;
            case MP_RPAREN:
                parseTree(47);
                break;
            default:
                LL1error();
        }
    }
    private void ReadParameter() throws ParseException {
        parseTree(48);
        String id = VariableIdentifier();
        // semantic analysis step
        Argument record =  new Argument(id);
        try {
            analyzer.opLookup(Token.MP_READ).performOp(record,null,null);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
    }
    private void WriteStatement() throws ParseException {
        SemanticRecord record = new SemanticRecord();
        Argument arg;
        record.setOperator(Analyzer.opLookup(Token.MP_WRITE));
        record.setToken(lookahead);
        switch (lookahead) {
            case MP_WRITE:
                parseTree(49);
                matchLookAhead(Token.MP_WRITE);
                matchLookAhead(Token.MP_LPAREN);
                arg = WriteParameter();
                record.setLeftOperand(arg);
                WriteParameterTail(record);
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_WRITELN:
                parseTree(50);
                matchLookAhead(Token.MP_WRITELN);
                matchLookAhead(Token.MP_LPAREN);
                arg = WriteParameter();
                record.setLeftOperand(arg);
                WriteParameterTail(record);
                matchLookAhead(Token.MP_RPAREN);
                break;
            default:
                LL1error();
        }
    }
    private void WriteParameterTail(SemanticRecord record) throws ParseException {
        Argument arg;
        switch (lookahead) {
            case MP_COMMA:
                parseTree(51);
                matchLookAhead(Token.MP_COMMA);
                genExpression(record);
                Argument currentArg = WriteParameter();
                record.setLeftOperand(currentArg);
                WriteParameterTail(record);
                break;
            case MP_RPAREN:
                record.setOperator(Analyzer.opLookup(record.getToken()));
                genExpression(record);
                parseTree(52);
                break;
        default:
            LL1error();
        }
    }
    private Argument WriteParameter() throws ParseException {
        parseTree(53);
        return OrdinalExpression();
    }
    private void AssignmentStatement() throws ParseException {

        TableEntry entry;
        try
        {
            entry = stack.getEntry(dispatcher.getLexeme());
        } catch (ParseException p)
        {
            throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
        }
        if(entry.kind == Kind.FUNCTION) {
            parseTree(55);
            FunctionIdentifier();
            matchLookAhead(Token.MP_ASSIGN);
            Expression();

        }
        else if (entry.kind == Kind.VARIABLE) {
            parseTree(54);
            String id = VariableIdentifier();
            matchLookAhead(Token.MP_ASSIGN);
            Expression();
            analyzer.genAssign(id);
        }
        else {
            throw new ParseException(" (AssignmentStatement) This code should be unreachable.");
        }

    }
    private void IfStatement() throws ParseException {

        parseTree(56);
        matchLookAhead(Token.MP_IF);
        Argument arg = BooleanExpression();
        String label = analyzer.genIfStart();
        matchLookAhead(Token.MP_THEN);
        Statement();
        analyzer.genIfEnd(label);
        OptionalElsePart();
    }
    private void OptionalElsePart() throws ParseException {

        switch (lookahead) {
            case MP_ELSE:
                /* using greedy/nearest match for conflict resolution */
                parseTree(57);
                matchLookAhead(Token.MP_ELSE);
                Statement();
                break;
            case MP_SCOLON:
            case MP_END:
            case MP_UNTIL:
                parseTree(58);
                break;
            default:
                LL1error();
        }
    }
    private void RepeatStatement() throws ParseException {

        parseTree(59);
        matchLookAhead(Token.MP_REPEAT);
        String l1 = analyzer.genRepeatUntilStart();
        StatementSequence();
        matchLookAhead(Token.MP_UNTIL);
        Argument arg = BooleanExpression();
        analyzer.genRepeatUntilEnd(l1);
    }
    private void WhileStatement() throws ParseException {

        parseTree(60);
        matchLookAhead(Token.MP_WHILE);
        ArrayList<String> labels = analyzer.genWhileStart();
        Argument arg = BooleanExpression();
        analyzer.genWhileMiddle(labels);
        matchLookAhead(Token.MP_DO);
        Statement();
        analyzer.genWhileEnd(labels);
    }
    private void ForStatement() throws ParseException {

        parseTree(61);
        matchLookAhead(Token.MP_FOR);
        String Cvar = ControlVariable();
        matchLookAhead(Token.MP_ASSIGN);
        Argument arg = InitialValue();
        String l1 = analyzer.genForStart(Cvar, arg);
        Token op = StepValue();
        Argument endArg = FinalValue();
        String l2 = analyzer.genForMiddle(Cvar, op, endArg, l1);
        matchLookAhead(Token.MP_DO);
        Statement();
        analyzer.genForEnd(Cvar, op, l1, l2);
    }
    private String ControlVariable() throws ParseException {

        parseTree(62);
        return VariableIdentifier();
    }
    private Argument InitialValue() throws ParseException {

        parseTree(63);
        return OrdinalExpression();
    }
    private Token StepValue() throws ParseException {
        Token token = lookahead;
        switch (lookahead) {
            case MP_TO:
                parseTree(64);
                matchLookAhead(Token.MP_TO);
                break;
            case MP_DOWNTO:
                parseTree(65);
                matchLookAhead(Token.MP_DOWNTO);
                break;
            default:
                LL1error();
        }
        return token;
    }
    private Argument FinalValue() throws ParseException {
        parseTree(66);
        return OrdinalExpression();
    }
    private void ProcedureStatement() throws ParseException {
        parseTree(67);
        String label = ProcedureIdentifier();
        List<Argument> params = OptionalActualParameterList();
    }
    private List<Argument> OptionalActualParameterList() throws ParseException {
        List<Argument> argList = new ArrayList<Argument>();
        switch (lookahead) {
            case MP_LPAREN:
                parseTree(68);
                matchLookAhead(Token.MP_LPAREN);
                ActualParameter(argList);
                ActualParameterTail(argList);
                matchLookAhead(Token.MP_RPAREN);
                break;
            case MP_SCOLON:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_END:
                parseTree(69);
                break;
            default:
                LL1error();
        }
        return argList;
    }
    private void ActualParameterTail(List<Argument> argList) throws ParseException {

        switch (lookahead) {
            case MP_COMMA:
                parseTree(70);
                matchLookAhead(Token.MP_COMMA);
                ActualParameter(argList);
                ActualParameterTail(argList);
                break;
            case MP_RPAREN:
                parseTree(71);
                break;
            default:
                LL1error();
        }
    }
    // appends arguments to list
    private void ActualParameter(List<Argument> argList) throws ParseException {
        parseTree(72);
        Argument newArg = OrdinalExpression();
        argList.add(newArg);
    }
    private Argument Expression() throws ParseException {
        parseTree(73);
        Argument arg = SimpleExpression();
        SemanticRecord record = OptionalRelationalPart();
        return getNextArg(arg,record);
    }
    private SemanticRecord OptionalRelationalPart() throws ParseException {
        switch (lookahead) {
            case MP_COMMA:
            case MP_SCOLON:
            case MP_RPAREN:     // added for fix
            case MP_LPAREN:
            case MP_DOWNTO:
            case MP_DO:
            case MP_END:
            case MP_ELSE:
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                parseTree(75);
                break;
            case MP_EQUAL:
            case MP_LTHAN:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_GEQUAL:
            case MP_NEQUAL:
                parseTree(74);
                Token op = RelationalOperator();
                Argument arg = SimpleExpression();
                return new SemanticRecord(op,arg);
            default:
                LL1error();
        }
        return null;
    }
    private Token RelationalOperator() throws ParseException {
        Token token = lookahead;
        switch (lookahead) {
            case MP_EQUAL:
                parseTree(76);
                matchLookAhead(Token.MP_EQUAL);
                break;
            case MP_LTHAN:
                parseTree(77);
                matchLookAhead(Token.MP_LTHAN);
                break;
            case MP_GTHAN:
                parseTree(78);
                matchLookAhead(Token.MP_GTHAN);
                break;
            case MP_LEQUAL:
                parseTree(79);
                matchLookAhead(Token.MP_LEQUAL);
                break;
            case MP_GEQUAL:
                parseTree(80);
                matchLookAhead(Token.MP_GEQUAL);
                break;
            case MP_NEQUAL:
                parseTree(81);
                matchLookAhead(Token.MP_NEQUAL);
                break;
            default:
                LL1error();
        }
        return token;
    }
    private Argument SimpleExpression() throws ParseException {
        parseTree(82);
        Token token = OptionalSign();
        Argument newArg = Term();
        if(token==Token.MP_MINUS) {   // generate negation
            SemanticRecord signRecord = new SemanticRecord(newArg, token, null);
            signRecord.setOperator(analyzer.getNeg());  // manually operator to negate
            signRecord.setType(newArg.getType());
            newArg = genExpression(signRecord);        // reassign argument
        }
        SemanticRecord record = TermTail();
        return getNextArg(newArg, record);
    }
    private SemanticRecord TermTail() throws ParseException {
        Token op;
        SemanticRecord record;
        Argument leftArg, rightArg;
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
            case MP_THEN:
            case MP_TO:
            case MP_UNTIL:
                parseTree(84);
                break;
            case MP_OR:
            case MP_PLUS:
            case MP_MINUS:
                parseTree(83);
                op = AddingOperator();
                leftArg= Term();
                record = TermTail();
                rightArg = getNextArg(leftArg,record);   // generates expression if applicable and gets the next rightArg
                return new SemanticRecord(op,rightArg);
            default:
                LL1error();
        }
        return null;
    }

    // section written by Hunter

    private Token OptionalSign() throws ParseException {
        switch(lookahead){
            case MP_FALSE:
            case MP_NOT:
            case MP_TRUE:
            case MP_IDENTIFIER:
            case MP_INTEGER_LIT:
            case MP_FLOAT_LIT:
            case MP_STRING_LIT:
            case MP_LPAREN:
                parseTree(87);
                break;
            case MP_PLUS:
                parseTree(85);
                matchLookAhead(Token.MP_PLUS);
                return Token.MP_PLUS;
            case MP_MINUS:
                parseTree(86);
                matchLookAhead(Token.MP_MINUS);
                return Token.MP_MINUS;
            default:
                LL1error();
        }
        return null;
    }
    private Token AddingOperator() throws ParseException{
        switch(lookahead) {
            case MP_PLUS:
                parseTree(88);
                matchLookAhead(Token.MP_PLUS);
                return Token.MP_PLUS;
            case MP_MINUS:
                parseTree(89);
                matchLookAhead(Token.MP_MINUS);
                return Token.MP_MINUS;
            case MP_OR:
                parseTree(90);
                matchLookAhead(Token.MP_OR);
                return Token.MP_OR;
            default:
                LL1error();
                return null;
        }
    }

    private Argument Term() throws ParseException {
        parseTree(91);
        Argument leftArg = Factor();
        SemanticRecord record = FactorTail();
        return getNextArg(leftArg,record);   // generates expression if applicable and gets the next rightArg
    }
    private SemanticRecord FactorTail() throws ParseException
    {
        switch(lookahead){
            case MP_FLOAT_DIVIDE:
            case MP_DIV:
            case MP_TIMES:
            case MP_MOD:
            case MP_AND:
                parseTree(92);
                Token op = MultiplyingOperator();
                Argument newArg = Factor();
                SemanticRecord record = FactorTail();
                Argument nextArg = getNextArg(newArg, record);   // generates expression if applicable and gets the next rightArg
                return new SemanticRecord(op, nextArg);         // returns a record with missing leftArg
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
                parseTree(93);
                break;
            default:
                LL1error();
        }
        return null;
    }
    private Token MultiplyingOperator() throws ParseException
    {
        Token temp = lookahead;
        switch(lookahead){
            case MP_TIMES:
                parseTree(94);
                matchLookAhead(Token.MP_TIMES);
                break;
            case MP_FLOAT_DIVIDE:
                parseTree(95);
                matchLookAhead(Token.MP_FLOAT_DIVIDE);
                break;
            case MP_DIV:
                parseTree(96);
                matchLookAhead(Token.MP_DIV);
                break;
            case MP_MOD:
                parseTree(97);
                matchLookAhead(Token.MP_MOD);
                break;
            case MP_AND:
                parseTree(98);
                matchLookAhead(Token.MP_AND);
                break;

            default:
                LL1error();
        }
        return temp;
    }

    private Argument Factor() throws ParseException
    {
        String id = null;
        String lit = dispatcher.getLexeme();

        switch(lookahead){
            case MP_NOT:
                parseTree(104);
                matchLookAhead(Token.MP_NOT);
                Argument arg = Factor();
                if(!arg.isInStack()) {
                    try {
                        arg.genPush();
                    } catch (SemanticException e) {
                        e.printStackTrace();
                    }
                }
                SemanticRecord record = new SemanticRecord(arg,Token.MP_NOT,null);
                return genExpression(record);
            case MP_LPAREN:
                parseTree(105);
                matchLookAhead(Token.MP_LPAREN);
                arg = Expression();
                matchLookAhead(Token.MP_RPAREN);
                return arg;
            case MP_INTEGER_LIT:
                parseTree(99);
                matchLookAhead(Token.MP_INTEGER_LIT);
                return new Argument(lit,Type.INTEGER);
            case MP_FIXED_LIT:
                //parseTree(0);
                matchLookAhead(Token.MP_FIXED_LIT);
                return new Argument(lit,Type.FIXED);
            case MP_FLOAT_LIT:
                parseTree(100);
                matchLookAhead(Token.MP_FLOAT_LIT);
                return new Argument(lit,Type.FLOAT);
            case MP_STRING_LIT:
                parseTree(101);
                matchLookAhead(Token.MP_STRING_LIT);
                return new Argument(lit.replace("\'", "\""),Type.STRING);
            case MP_TRUE:
                parseTree(102);
                matchLookAhead(Token.MP_TRUE);
                return new Argument(lit,Type.BOOLEAN);
            case MP_FALSE:
                parseTree(103);
                matchLookAhead(Token.MP_FALSE);
                return new Argument(lit,Type.BOOLEAN);
            case MP_IDENTIFIER:
                TableEntry entry;
                try
                {
                    entry = stack.getEntry(dispatcher.getLexeme());
                } catch (ParseException p)
                {
                    throw new ParseException(p.getMessage() + String.format("Line %s, Col %s, Token %s", dispatcher.getLine(), dispatcher.getColumn(), dispatcher.getToken().toString()));
                }
                if(entry.kind == Kind.VARIABLE) {
                    parseTree(116);
                    id = VariableIdentifier();
                }
                else if (entry.kind == Kind.FUNCTION) {
                    parseTree(106);
                    id = FunctionIdentifier();
                    OptionalActualParameterList();
                }
                break;
            default:
                LL1error();
        }
        if(id!=null) {
            return new Argument(id);
        }
        else {
            return null;
        }
    }
    private String ProgramIdentifier() throws ParseException {
        parseTree(107);
        String lexeme = dispatcher.getLexeme();
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setLexeme(lexeme);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
        return lexeme;
    }
    private String VariableIdentifier() throws ParseException {
        parseTree(108);
        String lexeme = dispatcher.getLexeme();
        if(lookahead==Token.MP_IDENTIFIER) {                // the table entry needs to be updated before running our match.
            tableEntry.setLexeme(lexeme);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
        return lexeme;
    }
    // use return value for label production
    private String ProcedureIdentifier() throws ParseException {
        parseTree(109);
        String lexeme = dispatcher.getLexeme();
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setLexeme(lexeme);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
        return lexeme;
    }
    // use return value for label production and function return value/identifier
    private String FunctionIdentifier() throws ParseException {
        parseTree(110);
        String lexeme = dispatcher.getLexeme();
        if(lookahead==Token.MP_IDENTIFIER) {
            tableEntry.setLexeme(lexeme);
        }
        matchLookAhead(Token.MP_IDENTIFIER);
        return lexeme;
    }
    private Argument BooleanExpression() throws ParseException {
                parseTree(111);
                // add type checking
                return Expression();
    }
    private Argument OrdinalExpression() throws ParseException {
                parseTree(112);
                // add type checking
                return Expression();
    }
    private void IdentifierList() throws ParseException {
                parseTree(113);
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setLexeme(dispatcher.getLexeme());
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
    }
    private void IdentifierTail() throws ParseException
    {
        switch(lookahead){
            case MP_COMMA:
                parseTree(114);
                matchLookAhead(Token.MP_COMMA);
                if(lookahead==Token.MP_IDENTIFIER) {
                    tableEntry.setLexeme(dispatcher.getLexeme());
                }
                matchLookAhead(Token.MP_IDENTIFIER);
                IdentifierTail();
                break;
            case MP_COLON:
                parseTree(115);
                break;
            default:
                LL1error();
        }
    }
}