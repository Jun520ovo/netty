package com.nettyFile.ThreadManyFile;

public class Message {
	private int nameLength;//文件名长度
	private String name;//问价名
	private long contentLength;//文件内容长度
	
	private int directoryLength;//文件夹长度
	private String directory;//文件路径
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
	public int getDirectoryLength() {
		return directoryLength;
	}
	public void setDirectoryLength(int directoryLength) {
		this.directoryLength = directoryLength;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	
	
}
