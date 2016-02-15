package com.goatgames.goatengine.leveleditor.consolecommands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.strongjoshua.console.Console;

/**
 * Z index related commands
 */
public class ZCommand {
    /**
     * Sets the minimum Z index of the level editor.
     * The Minimum Z index means the minimum Z index an entity must have to be
     * considered by the editor
     */
    public static class SetZ extends ConsoleCommand {

        LevelEditor editor;

        public SetZ(LevelEditor editor){
            this.editor = editor;
        }

        @Override
        protected void execute(String... args) {
            if(args.length == 0){
                console.log("You must specify a number", Console.LogLevel.ERROR);
                console.log(getUsage());
            }
            editor.setMinZ(Integer.parseInt(args[0]));
            console.log(String.format("Level Editor: minimum Z set to %s", args[0]));
        }

        @Override
        public String getName() {
            return "setZ";
        }

        @Override
        public String getDesc() {
            return "Sets the minimum Z of the editor";
        }

        @Override
        public String getUsage() {
            return "Usage: setZ <Z value>";
        }
    }

    /**
     * Returns the min Z of the editor
     */
    public static class GetZ extends ConsoleCommand {

        LevelEditor editor;
        public GetZ(LevelEditor editor) {
            this.editor = editor;
        }
        @Override
        protected void execute(String... args) {
            console.log(String.valueOf(editor.getMinZ()));
        }

        @Override
        public String getName() {
            return "getZ";
        }

        @Override
        public String getDesc() {
            return "Returns the minimum Z of the level editor";
        }

        @Override
        public String getUsage() {
            return String.format("Usage: %s", getName());
        }
    }
}
