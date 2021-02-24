package com.developer.assignment.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.developer.assignment.dao.CreateFolder;
import com.developer.assignment.dao.CreateFolderImpl;

public class RandomFileFolderImpl implements RandomFileFolder {

	private CreateFolder folder = new CreateFolderImpl();

	List<String> fileExtension = new ArrayList<String>();

	List<String> basePath = new ArrayList<String>();
	String homedir = "";

	@Override
	public void createfilefolder() {
		initValues();
		try {
			if (Files.exists(Paths.get(homedir))) {
				FileUtils.cleanDirectory(new File(homedir));
			} else {
				Files.createDirectories(Paths.get(homedir));
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(2);
		}

		folder.createFolder(basePath);
		folder.createFile(fileExtension, basePath);

		List<String> collect = folder.getListOfFiles(homedir, "txt");
		folder.copyOutputFiles(collect, homedir, fileExtension);

	}

	private void initValues() {
		fileExtension.add("txt");
		fileExtension.add("doc");
		fileExtension.add("csv");
		fileExtension.add("json");

		String currentUsersHomeDir = System.getProperty("user.dir");

		homedir = currentUsersHomeDir + File.separator + "Test" + File.separator;
		basePath.add(homedir);
	}
	
	
	
	
	
	

}
