package com.company.Lexer;

import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer
{
    private final ArrayList<Token> tokens;

    public Lexer(String line) throws Exception
    {
        tokens = new ArrayList<>();

        if (line.compareTo("") != 0)
            processAddictiveTokens(line);
    }

    private void processAddictiveTokens(String line) throws Exception
    {

        Pattern pattern = Pattern.compile
                ("(\\b(?:var|if|else|while|for|print|println|true|false|List)\\b)" +
                        "|([A-Za-z][A-Za-z0-9]*)|(\\d?\\d++)|(::)|(#)|([\\=\\>\\<\\!]?\\=)" +
                        "|([\\+\\-\\*\\/\\%]|[\\(\\)]|[\\:]|[\\>\\<]|[\\,])" +
                        "|(    )|(.)");

        Matcher matcher = pattern.matcher(line);


        while (matcher.find())
        {
            if (matcher.group(1) != null)
                tokens.add(new Token(matcher.group(1), checkCommands(matcher.group(1))));
            else if (matcher.group(2) != null)
                tokens.add(new Token(matcher.group(2), TokenType.ID));
            else if (matcher.group(3) != null)
                tokens.add(new Token(matcher.group(3), TokenType.DIGIT));
            else if (matcher.group(4) != null)
                tokens.add(new Token("CALL", TokenType.CALL));
            else if (matcher.group(5) != null)
                tokens.add(new Token("NEW", TokenType.NEW));
            else if (matcher.group(6) != null)
                tokens.add(new Token(matcher.group(6), checkEqual(matcher.group(6))));
            else if (matcher.group(7) != null)
                tokens.add(new Token(matcher.group(7), checkOperand(matcher.group(7))));
            else if (matcher.group(8) != null)
                tokens.add(new Token("TAB", TokenType.TAB));

            else if (matcher.group(9) != null)
            {
                //String a = matcher.group(6);

               // if (matcher.group(6).compareTo(" ") != 0 && matcher.group(6).compareTo("") != 0)
                    //throw new VI_Exception(matcher.group(6) + "unknown symbol");
            }
        }
    }


    private TokenType checkCommands(String value) throws Exception
    {
        if(value.compareTo("var") == 0)
            return TokenType.VARIABLE_DECLARATION;
        else if(value.compareTo("if") == 0)
            return TokenType.IF;
        else if(value.compareTo("else") == 0)
            return TokenType.ELSE;
        else if(value.compareTo("while") == 0)
            return TokenType.WHILE;
        else if(value.compareTo("do") == 0)
            return TokenType.DO;
        else if(value.compareTo("List") == 0)
            return TokenType.LIST;
        else if(value.compareTo("true") == 0)
            return TokenType.BOOLEAN;
        else if(value.compareTo("false") == 0)
            return TokenType.BOOLEAN;
        else if(value.compareTo("print") == 0)
            return TokenType.PRINT;
        else if(value.compareTo("println") == 0)
            return TokenType.PRINTLN;
        else
            throw new Exception("Неизвестный элемент : " + value);

    }

    private TokenType checkEqual(String value) throws Exception
    {
        if(value.compareTo("=") == 0)
            return TokenType.EQUAL;
        else if(value.compareTo("==") == 0)
            return TokenType.POSITIVE_EQUAL;
        else if(value.compareTo("!=") == 0)
            return TokenType.NEGATIVE_EQUAL;
        else if(value.compareTo(">=") == 0)
            return TokenType.MORE_EQUAL;
        else if(value.compareTo("<=") == 0)
            return TokenType.LESS_EQUAL;
        else
            throw new Exception("Неизвестный элемент : " + value);
    }

    private TokenType checkOperand(String value) throws Exception
    {
        if(value.compareTo("+") == 0)
            return TokenType.PLUS;
        else if(value.compareTo("-") == 0)
            return TokenType.MINUS;
        else if(value.compareTo("*") == 0)
            return TokenType.MULTIPLY;
        else if(value.compareTo("/") == 0)
            return TokenType.DIVINE;
        else if(value.compareTo("%") == 0)
            return TokenType.PERCENT;
        else if(value.compareTo("(") == 0)
            return TokenType.LEFT_BRACKET;
        else if(value.compareTo(")") == 0)
            return TokenType.RIGHT_BRACKET;
        else if(value.compareTo(":") == 0)
            return TokenType.COLON;
        else if(value.compareTo(">") == 0)
            return TokenType.MORE;
        else if(value.compareTo("<") == 0)
            return TokenType.LESS;
        else if(value.compareTo(",") == 0)
            return TokenType.COMA;
        else
            throw new Exception("Неизвестный элемент : " + value);

    }

    public ArrayList<Token> getTokens() { return tokens; }
}
