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
        assertTrue(tokens.size == 4);
    }

    @Test
    public void testLexMacros() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("2");
        assertTrue(tokens.size == 2);

        tokens = lexer.lex(" 3 ");
        assertTrue(tokens.size == 4);
    }

    @Test
    public void testLexStringLiterals() throws Exception {
        GUTLLexer lexer = new GUTLLexer();
        Array<Token> tokens;

        tokens = lexer.lex("'a String Literal'");
        assertTrue(tokens.size == 2);

        tokens = lexer.lex("'incomplete string literal ");
        assertTrue(tokens.size == 1); // it should GAssert unclosed so only one token EOF
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
