package com.goatgames.goatengine.ui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
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

    public GUTLInterpreter(UIContext ctx){
        this.context = ctx;
        this.varEvaluator = new GUTLVariableEvaluator();
        this.macroEvaluator = new GUTLMacroEvaluator();
        this.parser = new GUTLParser();
    }

    /**
     * Evaluates a string of GUTL source code
     * returns a String result. (Can be empty)
     */
    public String evaluate(String src){
        GUTLParser.GUTLParseTreeNode tree = parser.parseExpression(src);
        varEvaluator.evalVariables(tree, context);
        macroEvaluator.evalMacros(tree);
        return tree.token; // The root should be the result
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
}

/**
 * Class used to parse GUTL Macros expression and syntax
 */
class GUTLParser {

    /**
     * Represents a parse tree node
     */
    public class GUTLParseTreeNode{
        String token;
        Array<GUTLParseTreeNode> children;

        public GUTLParseTreeNode(){
            children = new Array<>();
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
    }

    /**
     * Takes an expression and parses it and return a parse tree node
     * for the expression
     */
    public GUTLParseTreeNode parseExpression(String expr){
        GUTLParseTreeNode node = new GUTLParseTreeNode();
        if (expr.charAt(0) == GUTLSpecs.MACRO_TOKEN) {
            // Find token (macro)
            int openParamEnumTokenIndex = expr.indexOf(GUTLSpecs.MACRO_START_PARAM_ENUM_TOKEN);
            if(!GAssert.that(openParamEnumTokenIndex != -1,
                    String.format("Parse Error: Unexpected token, expected '%s'",
                            GUTLSpecs.MACRO_START_PARAM_ENUM_TOKEN)))
                return null;

            int closeParamEnumTokenIndex = expr.lastIndexOf(')');
            if(!GAssert.that(closeParamEnumTokenIndex != -1,
                    String.format("Parse Error: Unexpected token, expected '%s' closing matching braces",
                            GUTLSpecs.MACRO_END_PARAM_ENUM_TOKEN)))
                return null;

            node.token = expr.substring(0, openParamEnumTokenIndex);
            // Find parameters of the macro
            String paramStr = expr.substring(openParamEnumTokenIndex + 1, closeParamEnumTokenIndex); // without "()"

            String[] params = paramStr.split(Character.toString(GUTLSpecs.MACRO_PARAM_DELIMITER_TOKEN));
            // parse each param and add it as a node
            for(int i=0; i<params.length; i++){
                node.addChild(parseExpression(params[i]));
            }
            return node;
        }else{
            node.token = expr;
        }
        return node;
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
