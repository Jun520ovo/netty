package com.nettyFile.ThreadOneFile;

import java.io.Serializable;

public class Message implements Serializable{
	private int nameLength;//�ļ�������
	private String name;//�ʼ���
	private long contentLength;//�ļ����ݳ���
	private int sign;//
	
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
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
	
	
	
}
