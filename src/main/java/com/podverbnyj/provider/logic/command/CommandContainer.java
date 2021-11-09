package com.podverbnyj.provider.logic.command;

import java.util.*;

public class CommandContainer {
	
	private static Map<String, Command> commands;
	
	static {
		commands = new HashMap<>();
		
		commands.put("login", new LoginCommand());
//		commands.put("insertUsers", new InsertUsers());
		}

	public static Command getCommand(String commandName) {
		return commands.get(commandName);
	}
	
	private CommandContainer() {}

}
