package com.nettyFile.ThreadManyFile;

public class Message {
	private int nameLength;//�ļ�������
	private String name;//�ʼ���
	private long contentLength;//�ļ����ݳ���
	
	private int directoryLength;//�ļ��г���
	private String directory;//�ļ�·��
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
