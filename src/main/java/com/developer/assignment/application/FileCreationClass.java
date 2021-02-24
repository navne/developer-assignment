package com.developer.assignment.application;

import com.developer.assignment.service.RandomFileFolder;
import com.developer.assignment.service.RandomFileFolderImpl;

public class FileCreationClass {
	
	public static void main(String[] args) {
      RandomFileFolder randomFile = new RandomFileFolderImpl(); 
		randomFile.createfilefolder();
	}

}
