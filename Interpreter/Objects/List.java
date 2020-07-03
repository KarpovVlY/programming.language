package com.company.Interpreter.Objects;

import com.company.Interpreter.Interpreter;
import com.company.Interpreter.Service.FullRPN;
import com.company.Interpreter.Service.LanguageList;
import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;

public class List
{

    private final LanguageList<Token> languageList;

    public List()
    {
        languageList = new LanguageList<>();
    }
//////1 - l:-() и наоборот

    public Token executeFunction(String functionName, ArrayList<Object> functionArgs, boolean isInExpression, Interpreter interpreter) throws Exception {

        ArrayList<ArrayList<Object>> separatedArgs = separateArgs(functionArgs);

        if (!isInExpression)
        {
            if (functionName.compareTo("addBackward") == 0)
            {
                if (separatedArgs != null)
                {
                    if (separatedArgs.size() == 1)
                    {
                        languageList.addBackward(new FullRPN(separatedArgs.get(0), interpreter).getResult());
                        return null;
                    }
                    else
                        throw new Exception("Ошибка при вызове addBackward : получено " + separatedArgs.size() + " аргументов, ожидалось - 1");
                }
                else
                    throw new Exception("Ошибка при вызове addBackward : получено 0 аргументов, ожидалось - 1");
            }
            else if (functionName.compareTo("add") == 0)
            {
                if (separatedArgs != null)
                {
                    if (separatedArgs.size() == 2)
                    {
                        languageList.add(new FullRPN(separatedArgs.get(0), interpreter).getResult(),
                                Integer.parseInt(new FullRPN(separatedArgs.get(1), interpreter).getResult().getTokenValue()));
                        return null;
                    }
                    else
                        throw new Exception("Ошибка при вызове add : получено " + separatedArgs.size() + " аргументов, ожидалось - 2");
                }
                else
                    throw new Exception("Ошибка при вызове add : получено 0 аргументов, ожидалось - 2");
            }
            else if (functionName.compareTo("set") == 0)
            {
                if (separatedArgs != null)
                {
                    if (separatedArgs.size() == 2)
                    {
                        languageList.set(new FullRPN(separatedArgs.get(0), interpreter).getResult(),
                                Integer.parseInt(new FullRPN(separatedArgs.get(1), interpreter).getResult().getTokenValue()));
                        return null;
                    }
                    else
                        throw new Exception("Ошибка при вызове set : получено " + separatedArgs.size() + " аргументов, ожидалось - 2");
                }
                else
                    throw new Exception("Ошибка при вызове set : получено 0 аргументов, ожидалось - 2");
            }
            else if (functionName.compareTo("remove") == 0)
            {
                if (separatedArgs != null)
                {
                    if (separatedArgs.size() == 1)
                    {
                        languageList.remove(Integer.parseInt(new FullRPN(separatedArgs.get(0), interpreter).getResult().getTokenValue()));
                        return null;
                    }
                    else
                        throw new Exception("Ошибка при вызове remove : получено " + separatedArgs.size() + " аргументов, ожидалось - 1");
                }
                else
                    throw new Exception("Ошибка при вызове remove : получено 0 аргументов, ожидалось - 1");
            }
        }


        if (functionName.compareTo("get") == 0)
        {
            if (separatedArgs != null)
            {
                if (separatedArgs.size() == 1)
                    return languageList.get(Integer.parseInt(new FullRPN(separatedArgs.get(0), interpreter).getResult().getTokenValue()));
                else
                    throw new Exception("Ошибка при вызове get : получено " + separatedArgs.size() + " аргументов, ожидалось - 1");
            }
            else
                throw new Exception("Ошибка при вызове get : получено 0 аргументов, ожидалось - 1");
        }
        else if (functionName.compareTo("getSize") == 0)
        {
            if (separatedArgs == null)
                return new Token(Integer.toString(languageList.getSize()), TokenType.DIGIT);
            else
                throw new Exception("Ошибка при вызове getSize : получено " + separatedArgs.size() + " аргументов, ожидалось - 0");
        }
        else
            throw new Exception("Некорректный вызов функции");
    }



    private ArrayList<ArrayList<Object>> separateArgs(ArrayList<Object> functionArgs)
    {
        if(functionArgs != null)
        {
            ArrayList<ArrayList<Object>> separatedObjects = new ArrayList<>();

            ArrayList<Object> buffer = new ArrayList<>();
            Object o;

            for(int i = 0 ; i < functionArgs.size() ; i ++)
            {
                o = functionArgs.get(i);

                if(o instanceof Token && ((Token) o).getTokenType() == TokenType.COMA)
                {
                    separatedObjects.add(buffer);
                    buffer = new ArrayList<>();
                }
                else
                    buffer.add(o);
            }

            separatedObjects.add(buffer);

            return separatedObjects;
        }
        else
            return null;
    }
}
