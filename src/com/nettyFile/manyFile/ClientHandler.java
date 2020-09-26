package com.nettyFile.manyFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

public class ClientHandler extends ChannelInboundHandlerAdapter{
	private String directory;
	private File file;
	private List<File> list = new ArrayList<>();//��Ŵ����Ŀ��ļ�����
	private int i = 0;
	
	public ClientHandler(String directory) {
		this.directory = directory;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		recursionDirectory(new File(directory), ctx);//�����ļ�����
		System.out.println("�����ļ���:"+i);
		
		Massage message = new Massage();
		message.setState(1);//��ʾ�������
		ctx.writeAndFlush(message);//ѭ����Ͻ������ļ������ݹ�ȥ
		
	}//���������ͻ�����������
	/**
	 * �ݹ��ļ�
	 */
	private String filename =   i+".zip";
	public void recursionDirectory(File file,ChannelHandlerContext ctx) throws Exception {
		try {
			File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
			if(null == fileArray) {
				this.file = file;//��files���󸳸�file����
				++i;
				System.out.println("�ļ�:" + file);
				//���ͳ��ļ�������������Ϣ
				Massage message = new Massage();
				//String directoryName = file.getPath().replace(directory,"");//������·�����ļ���
				message.setNameLength(filename.getBytes().length);
				message.setName(filename);
				message.setContentLength(file.length());//�������ݳ��ȵ��ֽ�
				ctx.writeAndFlush(message);//������ͨ��ͨ�����
				if(message.getContentLength() != 0){
					list.add(file);
				}
			}else {
			for (File file2 : fileArray) {
				++i;
				if(file2.isDirectory()) {//�ж��ǲ����ļ���
					System.out.println("�ļ���" + file2);
					Massage message = new Massage();
					String directoryName = file2.getPath().replace(directory,"");//������·�����ļ���
					message.setFileDirectoryLength(directoryName.getBytes().length);
					message.setFileDirectory(directoryName);//���ļ�Ŀ¼·������
					ctx.writeAndFlush(message);//������ͨ��ͨ�����
					recursionDirectory(file2, ctx);
				}else {//����Ϊ�ļ�
					this.file = file2;//��files���󸳸�file����
					System.out.println("�ļ�:" + file2);
					//���ͳ��ļ�������������Ϣ
					Massage message = new Massage();
					String directoryName = file2.getPath().replace(directory,"");//������·�����ļ���
					message.setNameLength(directoryName.getBytes().length);
					message.setName(directoryName);
					message.setContentLength(file2.length());//�������ݳ��ȵ��ֽ�
					ctx.writeAndFlush(message);//������ͨ��ͨ�����
					if(message.getContentLength() != 0){
						list.add(file2);
					}
				}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		for (File file:list) {
			//��������
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(file, "r");//����ָ��·����ȡֻ���Ķ���
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				//�����ֽ�
				ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						//future.channel().close();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}//���յ�����˵����ݾͻ�����������
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}//���쳣�ͽ����������
}
