package com.company.Service;

public class Token
{
    private String tokenValue;
    private TokenType tokenType;

    public Token(String tokenValue, TokenType tokenType)
    {
        this.tokenValue = tokenValue;
        this.tokenType = tokenType;
    }

    public String getTokenValue() { return tokenValue; }
    public TokenType getTokenType() { return tokenType; }

}
