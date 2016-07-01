package com.goatgames.goatengine.ui.macros;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ui.UIVariable;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.NotImplementedException;

/**
 * Represents a Macro
 */
public abstract class Macro {

    public abstract UIVariable execute(UIVariable[] params);

    /**
     * Returns the token by which the macro can be called.
     * Do not return the @ character.
     * @return
     */
    public String getToken(){
        String simpleName = this.getClass().getSimpleName();
        char[] c = simpleName.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        simpleName = new String(c);
        return simpleName;
    }

    /**
     * Multiplies two variables
     * e.g. size = 4, @multiply($size,2) : returns 8
     */
    public static class Multiply extends Macro{
        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 2; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;
            // All params will be interpreted as integers
            UIVariable number = params[0];
            UIVariable factor = params[1];

            int result = number.getInt() * factor.getInt();
            return new UIVariable(result);
        }
    }

    /**
     * Divides two variables
     * e.g. $size = 4, divide($size,2) : returns 2
     */
    public static class Divide extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 2; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            // All params will be interpreted as integers
            UIVariable number = params[0];
            UIVariable factor = params[1];

            int result = number.getInt() / factor.getInt(); // Result might not be accurate (TODO add float type)
            return new UIVariable(result);
        }
    }

    /**
     *  Adds
     */
    public static class Add extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  Minuss
     */
    public static class Minus extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  SetValues
     */
    public static class SetValue extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  Increments
     */
    public static class Increment extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  IsNulls
     */
    public static class IsNull extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  Nots
     */
    public static class Not extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  Ors
     */
    public static class Or extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     *  Makes an and between two boolean variables
     */
    public static class And extends Macro{

        @Override
        public UIVariable execute(UIVariable[] params) {
            final int NB_ARGUMENTS = 0; // Number of arguments for the command
            if(!GAssert.that(!(params.length > NB_ARGUMENTS), "MACRO(" + getToken() + "), too many arguments")) return null;
            if(!GAssert.that(!(params.length < NB_ARGUMENTS), "MACRO(" + getToken() + "), not enough arguments")) return null;

            throw new NotImplementedException();
        }
    }

    /**
     * Macro used to return a parameter from the Goat Engine configuration file
     */
    public static class GetGEParam extends Macro{

        public UIVariable execute(UIVariable[] params){

            final int NB_ARGUMENTS = 2;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;

            String[] types = {"boolean", "int", "float", "string"};

            String paramName = params[0].getString();
            String typeName = params[1].getString().toLowerCase();

            //if(!GAssert.that(typeName in types, "Invalid type name")) return null;

            UIVariable var = null;

            if(typeName.equals("boolean"))
                var = new UIVariable(GoatEngine.config.getBoolean(paramName));
            else if(typeName.equals("string"))
                var = new UIVariable(GoatEngine.config.getString(paramName));
            else if(typeName.equals("int") || typeName.equals("float"))
                var = new UIVariable(GoatEngine.config.getInt(paramName));

            return var;
        }
    }

    /**
     * Macro used to get absolute value of another value
     */
    public static class Abs extends Macro{

        public UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 1;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;
            UIVariable var = params[0];
            return new UIVariable(Math.abs(var.getInt()));
        }
    }

    /**
     * Macro used to get the lowest value
     */
    public static class Min extends Macro{

        public UIVariable execute(UIVariable[] params){
            if(!GAssert.that(params.length > 0, "Not enough arguments")) return null;

            int min = Integer.MAX_VALUE;
            for(int i=0; i<params.length; i++){
                int intValue = params[i].getInt();
                if(min == Integer.MAX_VALUE) min = intValue;
                min = (intValue < min) ? intValue : min;
            }
            return new UIVariable(min);
        }
    }

    /**
     * Macro used to get the highest value
     */
    public static class Max extends Macro{

        public UIVariable execute(UIVariable[] params){
            if(!GAssert.that(params.length > 0, "Not enough arguments")) return null;

            int max = Integer.MIN_VALUE;
            for(int i=0; i<params.length; i++){
                int intValue = params[i].getInt();
                if(max == Integer.MIN_VALUE) max = intValue;
                max = (intValue > max) ? intValue : max;
            }
            return new UIVariable(max);
        }
    }

    /**
     * Macro used to enclose a string with two other characters
     */
    public static class Enclose extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 3;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;
            UIVariable var = params[0];
            UIVariable start = params[1];
            UIVariable end = params[2];

            return new UIVariable(start.toString() + var.getString() + end.toString());
        }
    }

    /**
     * Upper cases a string
     */
    public static class Upper extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 1;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;
            UIVariable var = params[0];
            return new UIVariable(var.getString().toUpperCase());
        }
    }

    /**
     * Upper cases a string
     */
    public static class Lower extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 1;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;
            UIVariable var = params[0];

            return new UIVariable(var.getString().toLowerCase());
        }
    }

    /**
     * Used to replace a string
     */
    public static class Replace extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 2;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;

            String var = params[0].getString();
            String replaceWith = params[1].getString();
            return new UIVariable(var.replace(var, replaceWith));
        }
    }

    /**
     * Used to replace a string
     */
    public static class Trim extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 1;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;

            String var = params[0].getString();
            return new UIVariable(var.trim());
        }
    }

    /**
     * Indicates if a string or a char is contained in another
     */
    public static class Contains extends Macro {

        public  UIVariable execute(UIVariable[] params){
            final int NB_ARGUMENTS = 2;
            if(GAssert.that( !(params.length > NB_ARGUMENTS), "Too many arguments")) return null;
            if(GAssert.that( !(params.length < NB_ARGUMENTS), "Not enough arguments")) return null;

            UIVariable hay = params[0];
            UIVariable needle = params[1];

            return new UIVariable(hay.getString().contains(needle.getString()));
        }
    }

    /**
     * Indicates if a string or a char is contained in another
     */
    public static class Sum extends Macro {

        public  UIVariable execute(UIVariable[] params){
            if(!GAssert.that(params.length > 0, "Not enough arguments")) return null;

            int sum = 0;
            for(int i=0; i<params.length; i++){
                sum = params[i].getInt();
            }
            return new UIVariable(sum);
        }
    }

    /**
     * Indicates if a string or a char is contained in another
     */
    public static class Avg extends Macro {

        public  UIVariable execute(UIVariable[] params){
            if(!GAssert.that(params.length > 0, "Not enough arguments")) return null;

            int sum = 0;
            for(int i=0; i<params.length; i++){
                sum = params[i].getInt();
            }
            return new UIVariable(sum);
        }
    }
}
