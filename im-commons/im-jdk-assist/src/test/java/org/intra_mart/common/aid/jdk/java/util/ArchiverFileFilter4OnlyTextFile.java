package org.intra_mart.common.aid.jdk.java.util;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

public class ArchiverFileFilter4OnlyTextFile implements FileFilter, Serializable {
	public boolean accept(File pathname) {
		
		if(pathname.isDirectory()){
			if(pathname.list().length > 0){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(pathname.getName().indexOf(".txt") != -1){
				return true;
			}
			else{
				return false;
			}
		}
	}
}
