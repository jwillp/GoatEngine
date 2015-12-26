package com.strongjoshua.console;

import java.util.ArrayList;

/**
 * Classed used to complete commands in the console
 */
class CommandCompleter {
	private ArrayList<String> availableCommands;
	/**
	 * All the commands the completer proposed to the user since the last reset
	 * Serves as a history not to propose the same choice over and over
	 */
	private ArrayList<String> proposedCommands;


	// Used when there is not possible suggestion
	public static final String NO_SUGGESTION_AVAILABLE = "";



	public CommandCompleter() {
		proposedCommands = new ArrayList<String>();
		availableCommands = new ArrayList<String>();
	}

	/**
	 * Adds a command to be available as a completion possibility
	 * @param commandName the name of the command to make available as a suggestion
	 */
	public void addCommand(String commandName){
		this.availableCommands.add(commandName);
	}

	/**
	 * Returns the most likely command be what the user
	 * wants based on the input passed
	 * @return
	 */
	public String getNext(String currentInput){

		for(String c : availableCommands){
			if(c.startsWith(currentInput) && !proposedCommands.contains(c)){
				return c;
			}
		}
		return currentInput;
	}

	/**
	 * Resets the proposition history
	 */
	public void reset(){
		this.proposedCommands.clear();
	}





}