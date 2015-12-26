/**
 * Copyright 2015 StrongJoshua (strongjoshua@hotmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.strongjoshua.console;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;

/** A simple console that allows live logging, and live execution of methods, from within an application. Please see the <a
 * href="https://github.com/StrongJoshua/libgdx-inGameConsole">GitHub Repository</a> for more information.
 * 
 * @author StrongJoshua */
public class Console implements Disposable {


	/** Specifies the 'level' of a log entry. The level affects the color of the entry in the console and is also displayed next to
	 * the entry when the log entries are printed to a file with {@link Console#printLogToFile(String)}.
	 * 
	 * @author StrongJoshua */
	public enum LogLevel {
		/** The default log level. Prints in white to the console and has no special indicator in the log file.<br>
		 * Intentional Use: debugging. */
		DEFAULT(Color.WHITE, ""),
		/** Use to print errors. Prints in red to the console and has the '<i>ERROR</i>' marking in the log file.<br>
		 * Intentional Use: printing internal console errors; debugging. */
		ERROR(Color.RED, "Error: "),
		/** Prints in green. Use to print success notifications of events. Intentional Use: Print successful execution of console
		 * commands (if needed). */
		SUCCESS(Color.GREEN, "Success! "),
		/** Prints in white with {@literal "> "} prepended to the command. Has that prepended text as the indicator in the log file.
		 * Intentional Use: To be used by the console, alone. */
		COMMAND(Color.WHITE, "> "),

		/** Prints in cyan. Use to print info notifications of events. Intentional Use: Print informative execution of console
		 * commands */
		INFO(Color.CYAN, "Info: "),

		/** Prints in orange. Use to print warning notifications of events. Intentional Use: Print informative execution of console
		 * commands */
		WARNING(Color.ORANGE, "Warning: "),


		;


		private Color color;
		private String identifier;

		LogLevel (Color c, String identity) {
			this.color = c;
			identifier = identity;
		}

		Color getColor () {
			return color;
		}

		String getIdentifier () {
			return identifier;
		}
	}

	/** Use to set the amount of entries to be stored to unlimited. */
	public static final int UNLIMITED_ENTRIES = -1;

	protected int keyID = Input.Keys.GRAVE;
	protected boolean disabled;
	protected Log log;
	protected ConsoleDisplay display;
	protected boolean hidden = true;
	protected Stage stage;
	protected CommandHistory commandHistory;
	protected CommandCompleter commandCompleter;
	protected Window consoleWindow;
	protected Boolean logToSystem;


	/** Creates the console.<br>
	 * <b>***IMPORTANT***</b> Call {@link Console#dispose()} to make your {@link InputProcessor} the default processor again (this
	 * console uses a multiplexer to circumvent it).
	 * @see Console#dispose() */
	public Console() {
        VisUI.load();
        Skin skin = VisUI.getSkin();

		stage = new Stage();
		log = new Log();
		display = new ConsoleDisplay(skin);
		commandHistory = new CommandHistory();
		commandCompleter = new CommandCompleter();
		logToSystem = false;

        GoatEngine.inputManager.addInputProcessor(stage);

		display.pad(4);
		display.padTop(22);
		display.setFillParent(true);


		consoleWindow = new Window("Dev console", skin);
		consoleWindow.setMovable(true);
		consoleWindow.setResizable(true);
		consoleWindow.setKeepWithinStage(true);
		consoleWindow.addActor(display);
		consoleWindow.setTouchable(Touchable.disabled);
		consoleWindow.setColor(1,1,1,0.5f); //Lower Alpha

		stage.addActor(consoleWindow);
		stage.setKeyboardFocus(display);

		setSizePercent(50, 50);
		setPositionPercent(50, 50);


	}


	/** Clears all log entries. */
	public void clear () {
		log.getLogEntries().clear();
		display.refresh();
	}

	/** Set size of the console in pixels
	 * @param width width of the console in pixels
	 * @param height height of the console in pixels */
	public void setSize (int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Pixel size must be greater than 0.");
		}
		consoleWindow.setSize(width, height);
	}

	/** Makes the console also log to the System when {@link Console#log(String)} is called.
	 * @param log to the system */
	public void setLoggingToSystem (Boolean log) {
		this.logToSystem = log;
	}

	/** Set size of the console as a percent of screen size
	 * @param wPct width of the console as a percent of screen width
	 * @param hPct height of the console as a percent of screen height */
	public void setSizePercent (float wPct, float hPct) {
		if (wPct <= 0 || hPct <= 0) {
			throw new IllegalArgumentException("Size percentage must be greater than 0.");
		}
		if (wPct > 100 || hPct > 100) {
			throw new IllegalArgumentException("Size percentage cannot be greater than 100.");
		}
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		consoleWindow.setSize(w * wPct / 100.0f, h * hPct / 100.0f);
	}

	/** Set position of the lower left corner of the console
	 * @param x
	 * @param y */
	public void setPosition (int x, int y) {
		consoleWindow.setPosition(x, y);
	}

	/** Set position of the lower left corner of the console as a percent of screen size
	 * @param xPosPct Percentage for the x position relative to the screen size.
	 * @param yPosPct Percentage for the y position relative to the screen size. */
	public void setPositionPercent (float xPosPct, float yPosPct) {
		if (xPosPct > 100 || yPosPct > 100) {
			throw new IllegalArgumentException("Error: The console would be drawn outside of the screen.");
		}
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		consoleWindow.setPosition(w * xPosPct / 100.0f, h * yPosPct / 100.0f);
	}


	/** Draws the console. */
	public void draw () {

		if (disabled) return;
		stage.act();

		if (hidden) return;
		stage.draw();
	}

	/** Calls {@link Console#refresh(boolean)} with true. */
	public void refresh () {
		this.refresh(true);
	}

	/** Refreshes the console's stage. Use if the app's window size was changed.
	 * @param retain True if you want position and size percentages to be kept. */
	public void refresh (boolean retain) {
		float oldWPct = 0, oldHPct = 0, oldXPosPct = 0, oldYPosPct = 0;
		if (retain) {
			oldWPct = consoleWindow.getWidth() / stage.getWidth() * 100;
			oldHPct = consoleWindow.getHeight() / stage.getHeight() * 100;
			oldXPosPct = consoleWindow.getX() / stage.getWidth() * 100;
			oldYPosPct = consoleWindow.getY() / stage.getHeight() * 100;
		}
		int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();
		stage.getViewport().setWorldSize(width, height);
		stage.getViewport().update(width, height, true);
		if (retain) {
			this.setSizePercent(oldWPct, oldHPct);
			this.setPositionPercent(oldXPosPct, oldYPosPct);
		}
	}

	/** Logs a new entry to the console.
	 * @param msg The message to be logged.
	 * @param level The {@link LogLevel} of the log entry.
	 * @see LogLevel */
	public void log (String msg, LogLevel level) {
		log.addEntry(msg, level);
		display.refresh();

		if (logToSystem) {
			System.out.println(msg);
		}
	}

	/** Logs a new entry to the console using {@link LogLevel#DEFAULT}.
	 * @param msg The message to be logged.
	 * @see LogLevel
	 * @see Console#log(String, LogLevel) */
	public void log (String msg) {
		this.log(msg, LogLevel.DEFAULT);
	}

	/** Prints all log entries to the given file. Log entries include logs in the code and commands made from within in the console
	 * while the program is running.<br>
	 * 
	 * <b>WARNING</b><br>
	 * The file that is sent to this function will be overwritten!
	 * 
	 * @param file The relative path to the file to print to. This method uses {@link Files#local(String)}. */
	public void printLogToFile (String file) {
		this.printLogToFile(Gdx.files.local(file));
	}

	/** Prints all log entries to the given file. Log entries include logs in the code and commands made from within in the console
	 * while the program is running.<br>
	 * 
	 * <b>WARNING</b><br>
	 * The file that is sent to this function will be overwritten!
	 * 
	 * @param fh The {@link FileHandle} that links to the file to be written to. Note that <code>classpath</code> and
	 *           <code>internal</code> FileHandles cannot be written to. */
	public void printLogToFile (FileHandle fh) {
		if (log.printToFile(fh))
			log("Successfully wrote logs to file.", LogLevel.SUCCESS);
		else
			log("Unable to write logs to file.", LogLevel.ERROR);
	}

	/** @return If the console is disabled.
	 * @see Console#setDisabled(boolean) */
	public boolean isDisabled () {
		return disabled;
	}

	/** @param disabled True if the console should be disabled (unable to be shown or used). False otherwise. */
	public void setDisabled (boolean disabled) {
		if (disabled && !hidden) ((KeyListener)display.getListeners().get(0)).keyDown(null, keyID);
		this.disabled = disabled;
	}

	/** Gets the console's display key. If the console is enabled, the console will be shown upon this key being pressed.<br>
	 * Default key is <b>`</b> a.k.a. '<b>backtick</b>'.
	 * @return the keyID */
	public int getKeyID () {
		return keyID;
	}

	/** @param code The new key's ID. Cannot be {@link Keys#ENTER}.
	 * @see Console#getKeyID() */
	public void setKeyID (int code) {
		if (code == Keys.ENTER) return;
		keyID = code;
	}



	private ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();


	/**
	 * Adds a command to the available commands list
	 * @return
	 */
	public Console addCommand(ConsoleCommand c){
		commands.add(c);
		c.setConsole(this);
		commandCompleter.addCommand(c.getName());
		return this;
	}

	/**
	 * Returns the list of available commands
	 * @return
	 */
	public ArrayList<ConsoleCommand> getCommands() {
		return commands;
	}


	/**
	 * Executes the command passed
	 * @param command
	 */
	private void execCommand (String command) {
		log(command, LogLevel.COMMAND);

		String[] parts = command.split(" ");
		String commandName = parts[0];
		String[] sArgs = new String[0];
		if (parts.length > 1) {
			sArgs = new String[parts.length - 1];
			System.arraycopy(parts, 1, sArgs, 0, parts.length - 1);
		}

		boolean commandFound = false;
		for(ConsoleCommand c: commands){
			if(c.getName().equals(commandName)){
				commandFound = true;
				c.exec(sArgs);
			}
		}
		if(!commandFound){
			log("Command not found: " + command, LogLevel.ERROR);
		}
	}

	private Vector3 stageCoords = new Vector3();

	/** Returns if the given screen coordinates hit the console.
	 * @param screenX
	 * @param screenY
	 * @return True, if the console was hit. */
	public boolean hitsConsole (float screenX, float screenY) {
		if (disabled || hidden) return false;
		stage.getCamera().unproject(stageCoords.set(screenX, screenY, 0));
		return stage.hit(stageCoords.x, stageCoords.y, true) != null;
	}

	private class ConsoleDisplay extends Table {
		private Table logEntries;
		private TextField input;
		private Skin skin;
		private Array<Label> labels;

		protected ConsoleDisplay (Skin skin) {
			super(skin);
			this.setFillParent(false);
			this.skin = skin;

			labels = new Array<Label>();

			logEntries = new Table(skin);

			input = new TextField("", skin);
			input.setTextFieldListener(new FieldListener());

			scroll = new ScrollPane(logEntries, skin);
			scroll.setFadeScrollBars(false);
			scroll.setScrollbarsOnTop(false);
			scroll.setOverscroll(false, false);

			this.add(scroll).expand().fill().pad(4).row();
			this.add(input).expandX().fillX().pad(4);
			this.addListener(new KeyListener(input));
		}

		protected void refresh () {
			Array<LogEntry> entries = log.getLogEntries();
			logEntries.clear();

			// expand first so labels start at the bottom
			logEntries.add().expand().fill().row();
			int size = entries.size;
			for (int i = 0; i < size; i++) {
				LogEntry le = entries.get(i);
				Label l;
				// recycle the labels so we don't create new ones every refresh
				if (labels.size > i) {
					l = labels.get(i);
				} else {
					l = new Label("", skin, "default-font", LogLevel.DEFAULT.getColor());
					l.setWrap(true);
					labels.add(l);
				}
				l.setText(le.toConsoleString());
				l.setColor(le.getColor());
				logEntries.add(l).expandX().fillX().top().left().padLeft(4).row();
			}
			scroll.validate();
			scroll.setScrollPercentY(1);
		}

        public TextField getInput() {
            return input;
        }
    }

	private ScrollPane scroll;

	private class FieldListener implements TextFieldListener {
		@Override
		public void keyTyped (TextField textField, char c) {
			if (("" + c).equalsIgnoreCase(Keys.toString(keyID))) {
				String s = textField.getText();
				textField.setText(s.substring(0, s.length() - 1));
			}
		}
	}

	/**
	 * Listens key presses and handle them
	 */
	private class KeyListener extends InputListener {
		private TextField input;

		protected KeyListener (TextField tf) {
			input = tf;
		}

		@Override
		public boolean keyDown (InputEvent event, int keycode) {
            if (disabled) return false;

			// reset command completer because input string may have changed
			if (keycode != Keys.TAB) {
				commandCompleter.reset();
			}

			if (keycode == Keys.ENTER && !hidden) {
				return onEnterKeyPress();
			} else if (keycode == Keys.UP && !hidden) {
				onArrowUpKeyPress();
				return true;
			} else if (keycode == Keys.DOWN && !hidden) {
				onArrowDownKeyPress();
				return true;
			} else if (keycode == Keys.TAB && !hidden) {
				return onTabKeyPress();
			} else if (keycode == Keys.ESCAPE && !hidden){
				return onEscapeKeyPress();
			} else if (keycode == keyID) {
                toggle();
				return true;
			}
			return false;
		}


		/**
		 * Called when escape key is pressed
		 * @return
		 */
		private boolean onEscapeKeyPress() {
			this.input.setText(""); // Empty text
			return true;
		}


		/**
		 * Called when the ENTER key is pressed
		 */
		private boolean onEnterKeyPress(){
			String s = input.getText();
			if (s.length() == 0 || s.equals("") || s.split(" ").length == 0) return false;
			commandHistory.store(s);
			execCommand(s);
			input.setText("");
			return true;
		}

		/**
		 * Called when th TAB key is pressed
		 */
		private boolean onTabKeyPress(){
			String userWrittenText = input.getText();
			if (userWrittenText.length() == 0) return false;

			input.setText(commandCompleter.getNext(userWrittenText));
			input.setCursorPosition(input.getText().length());
			return true;
		}

		/**
		 * Called when the arrow up key is pressed
		 */
		private void onArrowUpKeyPress(){
			input.setText(commandHistory.getPreviousCommand());
			input.setCursorPosition(input.getText().length());
		}

		/**
		 * Called when the arrow down key is pressed
		 */
		private void onArrowDownKeyPress(){
			input.setText(commandHistory.getNextCommand());
			input.setCursorPosition(input.getText().length());
		}

	}

	/** Resets the {@link InputProcessor} to the one that was the default before this console object was created. */
	@Override
	public void dispose () {
        GoatEngine.inputManager.removeInputProcessor(stage);
		stage.dispose();
	}

	/** @return If console is hidden */
	public boolean isHidden () {
		return hidden;
	}

	public void hide(){
		this.hidden = true;
	}

	public void toggle(){
        hidden = ! hidden;
        if (hidden) {
            this.display.getInput().setText("");
            stage.setKeyboardFocus(display);
            consoleWindow.setTouchable(Touchable.disabled);
            //GoatEngine.inputManager.reserve(stage);
        } else {
            stage.setKeyboardFocus(this.display.getInput());
            consoleWindow.setTouchable(Touchable.enabled);
            //GoatEngine.inputManager.release();
        }
    }

}
