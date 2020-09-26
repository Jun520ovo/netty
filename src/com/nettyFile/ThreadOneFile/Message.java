package com.nettyFile.ThreadOneFile;

import java.io.Serializable;

public class Message implements Serializable{
	private int nameLength;//文件名长度
	private String name;//问价名
	private long contentLength;//文件内容长度
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
