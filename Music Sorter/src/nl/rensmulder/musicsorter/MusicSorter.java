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
		String path = retrievePath(reader);
		
		putFilesInArray(path);
		renameFiles();
		
		System.out.println("==============================================");
		System.out.println("All the files have been renamed. Have fun!");
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
