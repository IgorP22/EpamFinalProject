package com.podverbnyj.provider.logic.command;

import java.util.*;

public class CommandContainer {
	
	private static Map<String, Command> commands;
	
	static {
		commands = new HashMap<>();
		
		commands.put("login", new LoginCommand());
		commands.put("sort", new SortCommand());
		commands.put("download", new DownloadCommand());
		commands.put("email", new EmailCommand());
		commands.put("adminRequest", new AdminRequestCommand());
//		commands.put("insertUsers", new InsertUsers());
		}

	public static Command getCommand(String commandName) {
		return commands.get(commandName);
	}
	
	private CommandContainer() {}

}
