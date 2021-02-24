package com.developer.assignment.dao;

import java.util.List;

public interface CreateFolder {
	
	void createFolder(List<String> basePath);
	void createFile(List<String> extension,List<String> basePath );
    List<String> getListOfFiles(String basePath, String ext);
	void copyOutputFiles(List<String> collect, String basePath, List<String> ext);

}
