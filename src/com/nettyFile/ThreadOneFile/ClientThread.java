package com.nettyFile.ThreadOneFile;

import java.io.File;

public class ClientThread {
	private static String filePath ="D:\\绝密\\spring";//发送的文件夹
	public static void main(String[] args) throws Exception {
		recursionDirectory(new File(filePath));
		///Thread.sleep(10000);
	}
	/**
	 * 递归文件
	 */
	public static void recursionDirectory(File file) {
		try {
			File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
			for (File files:fileArray) {//循环文件下的所有文件
				if (files.isDirectory()) {//是否是文件夹
				Client client =	new Client(files.getPath(),0);
				client.start();
				client.join();
				recursionDirectory(files);
				} else {
				Client client =	new Client(files.getPath(),1);
				client.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
