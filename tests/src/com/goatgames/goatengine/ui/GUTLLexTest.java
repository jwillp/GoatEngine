package com.goatgames.goatengine.ui;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Lex Tests
 */
public class GUTLLexTest {

    @Test
    public void testLexAtoms() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("2");
        assertTrue(tokens.size == 2);

        tokens = lexer.lex(" 3 ");
        assertTrue(tokens.size == 2); // Lexer trims input
    }

    @Test
    public void testLexExpressionsWithSpaces() throws Exception{
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("@max(0,    200)  ");
        // 1  - @max
        // 2  - (
        // 3  - 0
        // 4  - ,
        // 5  -
        // 6  -
        // 7  -
        // 8  -
        // 9  - 200
        // 10 - )
        // 11 - EOF
        assertTrue(tokens.size == 11);
    }

    @Test
    public void testLexMacros() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        // 1 - @abs
        // 2 - (
        // 3 - 0
        // 4 - )
        // 5 - EOF
        tokens = lexer.lex("@abs(0)");
        assertTrue(tokens.size == 5);

        // 1 - @sum
        // 2 - (
        // 3 - 1
        // 4 - ,
        // 5 - 2
        // 6 - ,
        // 7 - 3
        // 8 - )
        // 9 - EOF
        tokens = lexer.lex("@sum(1,2,3)");
        assertTrue(tokens.size == 9);
    }

    @Test
    public void testLexStringLiterals() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("'a string Literal'");
        assertTrue(tokens.size == 2);

        tokens = lexer.lex("'incomplete string literal ");
        assertTrue(tokens.size == 1); // it should GAssert unclosed so only one token EOF

        // 1 - '@trim(this string literal contains a macro)'
        // 2 - EOF
        tokens = lexer.lex("'@trim(this string literal contains a macro)'");
        assertTrue(tokens.size == 2);

    }

    @Test
    public void testLexComplexExpression() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("@macro(atom,@macro()))"); // Do not forget EOF
        assertTrue(tokens.size == 10);


        tokens = lexer.lex("@macro(atom,@macro('literal',$var)))"); // Do not forget EOF
        assertTrue(tokens.size == 13);
    }

}
