package com.goatgames.goatengine.ui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.ui.Token.TokenType;
import com.goatgames.goatengine.ui.macros.Macro;
import com.goatgames.goatengine.utils.GAssert;


/*
 * Goat engine Ui Template Language (GUTL) interpreter class.
 * This class is used as an interpreter for GUTL.
 */
public class GUTLInterpreter {

    private UIContext context;
    private final GUTLParser parser;
    private final GUTLVariableEvaluator varEvaluator;
    private final GUTLMacroEvaluator macroEvaluator;
    private GUTLLexer lexer;

    public GUTLInterpreter(UIContext ctx){
        this.context = ctx;
        this.lexer = new GUTLLexer();
        this.parser = new GUTLParser();
        this.varEvaluator = new GUTLVariableEvaluator();
        this.macroEvaluator = new GUTLMacroEvaluator();
    }

    /**
     * Evaluates a string of GUTL source code
     * returns a String result. (Can be empty)
     */
    public String evaluate(String src){
        Array<Token> tokens = lexer.lex(src);
        GUTLParser.GUTLParseTreeNode tree = parser.parseTokens(tokens);
        if(tree != null) {
            varEvaluator.evalVariables(tree, context);
            macroEvaluator.evalMacros(tree);
            return tree.token; // The root should be the result
        }
        return null; // There was an error
    }
}

/**
 * Represents a Token
 */
class Token{

    public TokenType type;
    public String text;
    public int index;

    public Token(TokenType type, String txt, int index){
        this.type = type;
        this.text = txt;
        this.index = index;
    }
    public Token(TokenType type, char c, int index){
        this(type,Character.toString(c), index);
    }

    public String toString(){
        return String.format("%s{%s}", type.toString(), text);
    }

    /**
     * List of token types
     */
    public enum TokenType{
        LPAREN,   // (
        RPAREN,   // )
        MACRO,    // @macro
        STRLIT,   // 'string literal'
        COMMA,    // ,
        ATOM,     // something like a $var or an int
        BLANK,    // a space character
        EOF       // Additional token at the end of source
    }
}

/**
 * Some specification elements for the Goat Engine Templating Language
 */
class GUTLSpecs {
    public static final char MACRO_TOKEN = '@';
    public static final char MACRO_START_PARAM_ENUM_TOKEN = '(';
    public static final char MACRO_END_PARAM_ENUM_TOKEN = ')';
    public static final char MACRO_PARAM_DELIMITER_TOKEN = ',';
    public static final char VARIABLE_TOKEN = '$';

    public static ObjectMap<Character,TokenType> tokenMap = new ObjectMap<>();
    static {
        tokenMap.put(MACRO_TOKEN, TokenType.MACRO);
        tokenMap.put(MACRO_START_PARAM_ENUM_TOKEN, TokenType.LPAREN);
        tokenMap.put(MACRO_END_PARAM_ENUM_TOKEN, TokenType.RPAREN);
        tokenMap.put(MACRO_PARAM_DELIMITER_TOKEN, TokenType.COMMA);
    }
}


/**
 * Class used to traverse a GUTLParseTree and
 * replace the tokens in the tree that correspond to a variable with
 * their corresponding values by fetching them in the context
 */
class GUTLVariableEvaluator{

    public void evalVariables(GUTLParser.GUTLParseTreeNode node, UIContext ctx) {
        int childCount = node.getChildren().size;
        for (int i = 0; i < childCount; i++) {
            evalVariables(node.getChildren().get(i), ctx);
        }

        if (node.token.charAt(0) == GUTLSpecs.VARIABLE_TOKEN) {
            // Eval Variable
            String varName = node.token.substring(1); // without $ char
            UIVariable var = ctx.getVariable(varName);

            if (GAssert.notNull(var, "Execution error: undefined identifier: " + varName))
                if (var.getVarType() == UIVariable.VariableType.DYNAMIC) {
                    var.setVarType(var.findType());
                }
            node.token = var.getString();
        }
    }
}

/**
 * Class used to traverse a GUTLParseTree and replace macro calls
 * with their results.
 */
class GUTLMacroEvaluator{

    private ObjectMap<String, Macro> macros;

    public GUTLMacroEvaluator(){
        macros = new ObjectMap<>();
        // Add macros ...
        addMacro(new Macro.Multiply());
        addMacro(new Macro.Divide());
        addMacro(new Macro.Add());
        addMacro(new Macro.Minus());
        addMacro(new Macro.SetValue());
        addMacro(new Macro.Increment());
        addMacro(new Macro.IsNull());
        addMacro(new Macro.Not());
        addMacro(new Macro.Or());
        addMacro(new Macro.And());
        addMacro(new Macro.GetGEParam());
        addMacro(new Macro.Abs());
        addMacro(new Macro.Min());
        addMacro(new Macro.Max());
        addMacro(new Macro.Enclose());
        addMacro(new Macro.Upper());
        addMacro(new Macro.Lower());
        addMacro(new Macro.Replace());
        addMacro(new Macro.Trim());
        addMacro(new Macro.Contains());
        addMacro(new Macro.Sum());
        addMacro(new Macro.Avg());
    }

    /**
     * Traverses the GUTLParseTreeNode
     */
    public void evalMacros(GUTLParser.GUTLParseTreeNode node){

        // Evaluate children first (if any) as they will used as parameters
       /* int childCount = node.getChildren().size;
      for (int i = 0; i < childCount ; i++ ) {
         evalMacros(node.getChildren().get(i));
      }*/
        if(node.token.charAt(0) == '@'){
            // Eval macro
            String macroName = node.token.substring(1);
            Macro macro = this.getMacro(macroName);
            if(GAssert.notNull(macro, "Execution Error: undefined macro " + macroName)){

                // Consider childs as parameters
                int childCount = node.getChildren().size;
                UIVariable[] params = new UIVariable[childCount];

                // Convert parameters to UIVariables and infer type
                for (int i = 0; i < childCount ; i++ ) {
                    GUTLParser.GUTLParseTreeNode child = node.getChildren().get(i);
                    evalMacros(child);
                    UIVariable param = new UIVariable(child.token);
                    param.setVarType(param.findType());
                    params[i] = param;
                }

                // Execute macro with params
                UIVariable result = macro.execute(params);
                node.token = result.getString(); // Keep result in parse tree
            }
        }
    }

    public void addMacro(Macro macro){
        this.macros.put(macro.getToken(), macro);
    }

    public Macro getMacro(String macroToken){
        return this.macros.get(macroToken);
    }
}


/**
 * Class used to create lexemes for a string of GUTL
 * Lexical Analyser
 */
class GUTLLexer{

    /**
     * Analyses a string of text and creates tokens out of it
     */
    public Array<Token> lex(String src) {
        src = src.trim(); // This will reduce unnecessary blank tokens
        Array<Token> tokens = new Array<>();
        lex(src,0,tokens);

        if(!src.isEmpty()){
            GAssert.that(tokens.size != 0, "Lexing Error: There was an error lexing input, no token found");
        }
        tokens.add(new Token(TokenType.EOF, "", src.length()));
        return tokens;
    }

    private void lex(String src, int i, Array<Token> tokens) {
        if (i < src.length()) {
            char c = src.charAt(i);
            switch (c) {
                case '(' :
                case ')':
                    processParentheses(src, i, tokens);
                    break;
                case '@':
                    processMacro(src, i, tokens);
                    break;
                case ',':
                    processComma(src, i, tokens);
                    break;
                case '\'':
                    processStringLiteral(src, i, tokens);
                    break;
                case ' ':  // Skip spaces
                case '\r': // Skip carrige return
                case '\n': // Skip new lines
                    processIgnoredChar(src, i, tokens);
                default: // Everything else is an atom
                    processAtom(src, i, tokens);
            }
        }
    }

    /**
     * Tries to tokenize an input starting at index as a parentheses
     */
    private void processParentheses(String src, int i, Array<Token> tokens){
        char c = src.charAt(i);
        if (!GAssert.that(c == '(' || c == ')', "Lexing Error: Invalid input '" + c + "' expected '(' or ')'")) return;

        TokenType type = null;
        switch (c) {
            case '(' :
                type = TokenType.LPAREN;
                break;
            case ')' :
                type = TokenType.RPAREN;
                break;
        }
        tokens.add(new Token(type, c, i));
        lex(src, ++i, tokens);
    }

    /**
     * Tries to tokenize an input starting at index as a COMMA
     */
    private void processComma(String src, int i, Array<Token> tokens){
        char c = src.charAt(i);
        if (!GAssert.that(c == ',', "Lexing Error: Invalid input '" + c + "' expected ','")) return;
        tokens.add(new Token(TokenType.COMMA, c, i));
        lex(src, ++i, tokens);
    }

    /**
     * Tries to tokenize an input starting at index as a Macro
     */
    private void processMacro(String src, int start, Array<Token> tokens){
        char mC = src.charAt(start);
        if (!GAssert.that(mC == '@', "Lexing Error: Invalid input '" + mC + "' expected '@'")) return;

        int end = start;
        int srcLength = src.length();
        for (; end < srcLength; end++) {
            char c = src.charAt(end);
            if(start == end) continue;
            if(c == '(' || c == ')' || c == ',' || c == '@'){
                break;
            }
        }
        String macro = src.substring(start,end);
        tokens.add(new Token(TokenType.MACRO, macro, start));
        lex(src, end, tokens);
    }

    /**
     * Processes an atom Token
     */
    private void processAtom(String src, int start, Array<Token> tokens){
        int end = start;
        int srcLength = src.length();
        for (; end < srcLength; end++) {
            char c = src.charAt(end);
            if(c == '(' || c == ')' || c == ',' || c == '@'){
                break;
            }
        }
        String atom = src.substring(start,end);
        tokens.add(new Token(TokenType.ATOM, atom, start));
        lex(src, end, tokens);
    }

    /**
     * Tries to tokenize an input starting at index as a String literal
     */
    private void processStringLiteral(String src, int start, Array<Token> tokens){
        char mC = src.charAt(start);
        if (!GAssert.that(mC == '\'', "Lexing Error: Invalid input '" + mC + "' expected '\''")) return;

        int end = start;
        int srcLength = src.length();
        for (; end < srcLength; end++) {
            char c = src.charAt(end);
            if(start == end) continue;
            if(c == '\''){
                end++; // include the char
                break;
            }
        }
        String str = src.substring(start,end);
        if(GAssert.that(str.charAt(str.length()-1) == '\'', "Lexing Error: Invalid input, string literal not closed, expected ' ")){
            tokens.add(new Token(TokenType.STRLIT, str, start));
            lex(src, end, tokens);
        }
    }

    /**
     * Tries to tokenize an input starting at index as an ignored token.
     * These ignored tokens are all considered as BLANK
     */
    private void processIgnoredChar(String src, int i, Array<Token> tokens){
        char c = src.charAt(i);
        tokens.add(new Token(TokenType.BLANK, c, i));
        lex(src, ++i, tokens);
    }
}

/**
 *  GUTL Parser
 *
 * RULES:
 * BLANKS ARE ALWAYS SKIPPED
 * After MACRO expect LPAREN
 * After LPAREN expect one in [ATOM, STRLIT, RPAREN, MACRO]
 * After ATOM expect one in [COMMA, RPAREN]
 * After STRLIT expect one in [COMMA, RPAREN] (same as ATOM)
 * After RPAREN expect one in [NOTHING,COMMA]
 */
class GUTLParser{

    /**
     * Represents a parse tree node
     */
    public class GUTLParseTreeNode{
        String token;
        Array<GUTLParseTreeNode> children;

        public GUTLParseTreeNode(){
            children = new Array<>();
        }

        public GUTLParseTreeNode(Token token) {
            this.token = token.text;
            this.children = new Array<>();
        }

        /**
         * Adds a child
         * @param child
         */
        public void addChild(GUTLParseTreeNode child) {
            children.add(child);
        }

        public Array<GUTLParseTreeNode> getChildren() {
            return children;
        }

        public String toString(){
            return token;
        }
    }

    public GUTLParser(){

    }

    private int curTokenIdx; // The currently parsed token index
    private Array<Token> tokens;




    public GUTLParseTreeNode parseTokens(Array<Token> tokens){
        if(tokens.size == 0) return null;
        curTokenIdx = -1;
        this.tokens = tokens;

        // String should start with a macro
        nextToken();
        // Create an Empty root node to use as parent of the real root node because Java does not have pass-by-reference
        GUTLParseTreeNode root = new GUTLParseTreeNode();
        if(expect(TokenType.MACRO)){
            parseMacro(root);
        }
        expect(TokenType.EOF);
        return root.getChildren().get(0);
    }


    private Token getCurrentToken(){
        return tokens.get(curTokenIdx);
    }
    /**
     * Advances in the token list
     */
    private void nextToken(){
        if(curTokenIdx < 0 || getCurrentToken().type != TokenType.EOF)
            curTokenIdx++;
    }

    /**
     * Expects that the current token should be of a certain type
     * (equivalent of doing if accept(type) then nextToken())
     */
    private boolean expect(TokenType type){
        return GAssert.that(accept(type), "Unexpected token type :" + type.toString());
    }

    /**
     * Indicates that it is not faulty if the current token is specified token
     * and if so changes the current token to that next one.
     */
    private boolean accept(TokenType type){
        if(getCurrentToken().type == type){
            nextToken();
            return true;
        }
        return false;
    }

    /**
     * Parses the current Token as a macro
     */
    private void parseMacro(GUTLParseTreeNode parent){
        GUTLParseTreeNode node = createNode();
        // Only macro nodes can be parents
        GAssert.notNull(parent, "parent == null");
        parent.addChild(node);
        //Consume Blanks
        while(accept(TokenType.BLANK));

        if(expect(TokenType.LPAREN)){
            do{
                if(accept(TokenType.ATOM)){
                    GUTLParseTreeNode atom = createNode();
                    node.addChild(atom);
                }
                else if(accept(TokenType.STRLIT)){
                    GUTLParseTreeNode str = createNode();
                    node.addChild(str);
                }
                else if(accept(TokenType.MACRO)){
                    parseMacro(node);
                }
            }while(accept(TokenType.COMMA));
            if(expect(TokenType.RPAREN)){
                //parseRParen();
            }
        }
    }

    /**
     * Parses a Right Parenthesis
     */
    private void parseRParen(){
        /*if(accept(TokenType.COMMA)){
            if(expect(TokenType.MACRO)) {
                parseMacro();
            }
        }*/
        if(accept(TokenType.RPAREN)){
            // )) can exist
        }else if(accept(TokenType.EOF)){
        }
    }

    /**
     * Creates a node in the parse tree and adds it to
     * the necessary parent node.
     */
    private GUTLParseTreeNode createNode(){
        // Get preceding token of current token
        int prevIndex = (curTokenIdx > 0) ? (curTokenIdx - 1) : 0;
        return new GUTLParseTreeNode(tokens.get(prevIndex));
    }
}

