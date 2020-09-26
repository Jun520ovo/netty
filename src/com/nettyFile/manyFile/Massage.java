package com.nettyFile.manyFile;

import java.io.File;

public class Massage {
	  private int nameLength;//名字长度
	  private String name;//名字
	  private long contentLength;//内容长度
	  
	  private int fileDirectoryLength;//文件夹名字长度
	  private String fileDirectory;//文件夹的名字
	  
	  private Integer state;//除文件内容以外信息发送成功为OK
	  private File file;//文件路径
	  
	public int getNameLength() {
		return nameLength;
	}
	public void setNameLength(int nameLength) {
		this.nameLength = nameLength;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public int getFileDirectoryLength() {
		return fileDirectoryLength;
	}
	public void setFileDirectoryLength(int fileDirectoryLength) {
		this.fileDirectoryLength = fileDirectoryLength;
	}
	public String getFileDirectory() {
		return fileDirectory;
	}
	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "Massage [nameLength=" + nameLength + ", name=" + name + ", contentLength=" + contentLength
				+ ", fileDirectoryLength=" + fileDirectoryLength + ", fileDirectory=" + fileDirectory + ", state="
				+ state + ", file=" + file + "]";
	}
	
}
