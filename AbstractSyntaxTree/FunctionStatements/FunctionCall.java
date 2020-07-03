package com.company.AbstractSyntaxTree.FunctionStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.Lexer.Lexer;
import com.company.Parser.Parser;
import com.company.Service.ComplexStatement;
import com.company.Service.Token;
import com.company.Service.TokenType;

import java.util.ArrayList;

public class FunctionCall extends BaseStatement
{
    private Token functionId;
    private Token functionName;

    private ArrayList<Object> functionArgs;

    public FunctionCall(Token functionId)
    {
        this.functionId = functionId;
        astTokens = new ArrayList<>();
    }

    @Override
    public void process() throws Exception
    {

        if(astTokens.size() != 0)
        {
            functionArgs = new ArrayList<>();

            Token token;
            ArrayList<Token> tokens = new ArrayList<>();

            Token previous = null;
            for(int i = 0  ; i < astTokens.size() ; i ++)
            {
                token = (Token)astTokens.get(i);

                if(token.getTokenType() == TokenType.ID)
                {
                    previous = token;
                }
                else if(token.getTokenType() == TokenType.CALL)
                {
                    if(tokens.size() != 0 )
                    {
                        functionArgs.addAll(tokens);
                        tokens = new ArrayList<>();
                    }

                    i = processFunctionCall(previous, i);
                    previous = null;
                }

                else
                {
                    if(previous != null)
                    {
                        tokens.add(previous);
                        previous = null;
                    }
                    tokens.add(token);
                }

            }
            if(tokens.size() != 0 )
                functionArgs.addAll(tokens);
            if(previous != null)
                functionArgs.add(previous);


        }
    }


    private int processFunctionCall(Token functionId, int position) throws Exception
    {
        FunctionCall functionCall = new FunctionCall(functionId);

        functionCall.setFunctionName((Token) astTokens.get(position + 1));
        position+=3;

        Token currentToken;
        int[] brackets = {1, 0};

        int i;
        for(i = position ; i < astTokens.size() ; i ++)
        {
            currentToken = (Token) astTokens.get(i);

            if (currentToken.getTokenType() == TokenType.RIGHT_BRACKET)
                ++brackets[1];
            else if (currentToken.getTokenType() == TokenType.LEFT_BRACKET)
                ++brackets[0];

            if (brackets[0] == brackets[1])
                break;

            functionCall.addToken(astTokens.get(i));
        }

        functionCall.process();
        functionArgs.add(functionCall);

        return i;
    }




    public void setFunctionName(Token functionName) { this.functionName = functionName; }
    public Token getFunctionID() { return functionId; }

    public Token getFunctionName() { return functionName; }
    public ArrayList<Object> getFunctionArgs() { return functionArgs; }
}
