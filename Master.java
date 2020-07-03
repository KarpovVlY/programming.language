package com.company;

import com.company.AbstractSyntaxTree.BaseStatements.BlockStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.ElseStatement;
import com.company.AbstractSyntaxTree.ConditionStatements.IfStatement;
import com.company.AbstractSyntaxTree.CyclesStatements.WhileStatement;
import com.company.Interpreter.Interpreter;
import com.company.Lexer.Lexer;
import com.company.Parser.Parser;
import com.company.Service.ComplexStatement;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Master
{
    Master()
    {
        try
        {
            ArrayList<String> result = readFile();

            ComplexStatement masterComplexStatement;
            Interpreter interpreter = new Interpreter(null);

            int line;
            for(line = 0 ; line < result.size() ; line ++)
            {
                masterComplexStatement = new Parser(new Lexer(result.get(line)).getTokens()).getParserResult();

                if(masterComplexStatement.getStatement() != null)
                {
                    if(!masterComplexStatement.isAvailableBlock())
                    {
                        interpreter.execute(masterComplexStatement.getStatement());
                    }
                    else
                    {
                        BlockStatement currentStatement = (BlockStatement) masterComplexStatement.getStatement();

                        if(currentStatement instanceof IfStatement || currentStatement instanceof ElseStatement
                                || currentStatement instanceof WhileStatement)
                        {
                            ++line;

                            ComplexStatement complexStatement;

                            for ( ; line < result.size() ; line ++)
                            {
                                complexStatement = new Parser(new Lexer(result.get(line)).getTokens()).getParserResult();

                                if(complexStatement.getStatement() != null)
                                {
                                    if(complexStatement.getTabOrder() == masterComplexStatement.getTabOrder())
                                    {
                                        if(complexStatement.getStatement() instanceof ElseStatement)
                                        {
                                            if(currentStatement instanceof IfStatement)
                                                currentStatement.process();
                                            else
                                                throw new Exception("Ошибка : else может идти только после if");
                                        }
                                        else
                                        {
                                            --line;
                                            break;
                                        }

                                    }
                                    else
                                        currentStatement.addComplexStatement(complexStatement);
                                }
                            }

                            currentStatement.process();
                            interpreter.execute(currentStatement);
                        }
                        else
                            throw new Exception("Непредвиденная ошибка");
                    }
                }
            }
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
    }

    private ArrayList<String> readFile()
    {
        ArrayList<String> stringArray = new ArrayList<>();

        try
        {
            FileReader fr = new FileReader("/home/vladislav/Work/Projects/NIKA/src/com/company/text");
            Scanner scan = new Scanner(fr);

            while (scan.hasNextLine())
                stringArray.add(scan.nextLine());

            fr.close();
        } catch (Exception ignored) { }

        return stringArray;
    }
}
