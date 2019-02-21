package nl.rensmulder.musicsorter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MusicSorter {
	
	private Map<Integer, File> files = new LinkedHashMap<Integer, File>();
	private String[] allowedExtensions = {"mp3", "wav", "aac", "ogg", "wma"};
	
	
	public MusicSorter() {
		Scanner reader = new Scanner(System.in);
		
		start(reader);
	}
	
	/*
	 * First method that is executed by the program.
	 * Note: Method also gets recalled when the program is done with tasks.
	 */
	public void start(Scanner reader) {
		String command = getCommand(reader);
		executeCommand(command, reader);
	}
	
	/*
	 * Executes the given command.
	 * Note: It does not have a method to go when the command does not
	 * exist. This is because the existence of the command gets checked in
	 * getCommand();
	 */
	private void executeCommand(String command, Scanner reader) {
		if(command.equalsIgnoreCase("undo")) {
			String path = retrievePath(reader);
			
			undoFiles(path);
			
			System.out.println("==============================================");
			System.out.println("All the files have been unnumbered.");
			
		} else if(command.equalsIgnoreCase("rename")) {
			String path = retrievePath(reader);
			
			putFilesInArray(path);
			renameFiles();
			
			System.out.println("==============================================");
			System.out.println("All the files have been renamed. Have fun!");
		}
		start(reader);
	}

	/*
	 * "Preparation" method for retrieving the command.
	 */
	private String getCommand(Scanner reader) {
		
		String command;
		
		System.out.println("Please specify the command you want to use(rename/undo): ");
		
		command = retrieveCommand(reader);
		
		return command;
	}
	
	/*
	 * Method that retrieves the command. Gets called by getCommand();
	 */
	private String retrieveCommand(Scanner reader) {
		
		String command = reader.nextLine();
		
		if(!command.equalsIgnoreCase("undo") && !command.equalsIgnoreCase("rename")) {
			System.out.println("Command not found.");
			System.out.println("Please specify the command you want to use(rename/undo): ");
			command = retrieveCommand(reader);
		}
		
		return command;
		
	}

	private String retrievePath(Scanner reader) {
		
		System.out.println("Path of your music library: ");
		String path = reader.nextLine();
		
		if(path.substring(path.length() - 1) != "\\") {
			path += "\\";
		}
		
		if(!doesFolderExist(path)) {
			System.out.println("[x] The given path does not exist.");
			retrievePath(reader);
		} else {
			return path;
		}
		
		return null;
	}

	private void renameFiles() {
		ArrayList<Integer> ids = new ArrayList<Integer>(files.keySet());
		ArrayList<File> localFiles = new ArrayList<File>(files.values());
		
		Collections.shuffle(ids);
		
		System.out.println("Starting to rename all the files.");
		
		for(int i = 0; i < files.size(); i++) {
			renameFile(localFiles.get(i), ids.get(i));
		}
		
	}
	
	private void undoFiles(String path) {
		File[] pathFiles = new File(path).listFiles();
		
		for(File file : pathFiles) {
			undoFile(file);
		}
		
	}
	
	private void undoFile(File file) {
		if(!isMusicFile(file)) {
			System.out.println("[warning] " + file.getName() + " is not an audio file, ignoring!");
			return;
		}
		
		if(!fileHasBeenNumbered(file)) {
			System.out.println("[info] " + file.getName() + " is not numbered, ignoring!");
			return;
		}
		
		String path = file.getParent();
		
		String name = file.getName().substring(4, file.getName().length());
		
		File newFile = new File(path + "\\" + name);
		
		if(!file.renameTo(newFile)) {
			System.out.println("An error occured while renaming: " + name);
		}
		
	}

	private void renameFile(File file, int id) {
		
		String path = file.getParent();
		
		String name = file.getName();
		
		if(!isMusicFile(file)) {
			System.out.println("[warning] " + file.getName() + " is not an audio file, ignoring!");
			return;
		}
		
		
		if(fileHasBeenNumbered(file)) {
			name = file.getName().substring(4, file.getName().length());
		}
		
		File newFile;
		
		if(id < 10) {
			newFile = new File(path + "\\0" + id + ". " + name);
		} else {
			newFile = new File(path + "\\" + id + ". " + name);
		}
		
		if(!file.renameTo(newFile)) {
			System.out.println("An error has occured while renaming the files.");
		}
	}

	private boolean fileHasBeenNumbered(File file) {
		if(Character.isDigit(file.getName().charAt(0))) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		new MusicSorter();
	}

	private void putFilesInArray(String path) {
		File[] pathFiles = new File(path).listFiles();

		int index = 1;
		
		for(File file : pathFiles) {
			
			
			
			files.put(index, file);
			index++;
		}
		System.out.println("All the files are loaded in memory.");
	}
	
	private boolean doesFolderExist(String path) {
		return new File(path).exists();
	}
	
	/*
	 * Checks if the file has a music file extension.
	 */
	private boolean isMusicFile(File file) {
		String extension = "";

		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getPath().substring(i+1);
		}
		
		List<String> temp = new ArrayList<String>(Arrays.asList(allowedExtensions));
		
		if(temp.contains(extension)) {
			return true;
		}
		
		return false;
	}
	
}
