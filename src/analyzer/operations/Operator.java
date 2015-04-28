package analyzer.operations;

import analyzer.Argument;
import analyzer.MachineOp;
import analyzer.SemanticException;
import symbolTable.Type;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by night on 4/26/2015.
 */
public abstract class Operator {
    MachineOp op;
    Type[] types;
    int numArgs;
    private String writeFile = "generated_code.out";
    private BufferedWriter writer;
    private String endline = "\n";

    Operator() {
        try {
            writer = new BufferedWriter(new FileWriter(writeFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract Type performOp(Argument leftArg, Argument rightArg) throws SemanticException;

    // print single line out to a file.
    void putLine(String line) {
        try
        {
            writer.write(line + endline);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    public void closeFile()
    {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
