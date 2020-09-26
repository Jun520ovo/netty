package com.nettyFile.ThreadOneFile;

import java.io.File;

public class ClientThread {
	private static String filePath ="D:\\����\\spring";//���͵��ļ���
	public static void main(String[] args) throws Exception {
		recursionDirectory(new File(filePath));
		///Thread.sleep(10000);
	}
	/**
	 * �ݹ��ļ�
	 */
	public static void recursionDirectory(File file) {
		try {
			File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
			for (File files:fileArray) {//ѭ���ļ��µ������ļ�
				if (files.isDirectory()) {//�Ƿ����ļ���
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
