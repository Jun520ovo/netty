package com.nettyFile.manyFile1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	private String context;
	private static String path ;
	public ClientHandler(String path) {
		this.path=path;
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		File file =new File(path);
		recursionDirectory(file, ctx);
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * �ݹ��ļ�
	 */
	public void recursionDirectory(File file, ChannelHandlerContext ctx) {
		try {
			File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
			for (File files:fileArray) {//ѭ���ļ��µ������ļ�
				if (files.isDirectory()) {//�Ƿ����ļ���
					System.out.println("�ļ���:" + files);
					Message message = new Message();
					String directoryName = files.getPath().replace(path,"");//������·�����ļ���
					directoryName.getBytes(CharsetUtil.UTF_8);
					
					message.setDirectoryLength(directoryName.getBytes().length);
					message.setDirectory(directoryName);
					//ctx.writeAndFlush(((String)message).getBytes(CharsetUtil.UTF_8));//�Է����������� תΪutf��8����
					ctx.writeAndFlush(message);
					recursionDirectory(files, ctx);
				} else {//�������ļ�
					//this.file = files;//��files���󸳸�file����
					System.out.println("�ļ�:" + files);
					String directoryName = files.getPath().replace(path,"");//������·�����ļ���
					Message message = new Message();
					message.setNameLength(directoryName.getBytes().length);
					message.setName(directoryName);
					//FileInputStream fileInputStream = new FileInputStream(files);
				//	try {
					message.setContentLength(files.length());//���ｫ�ɶ��ֽڳ��ȼ���
					//} catch (Exception e) {
						//e.printStackTrace();
					//}
					ctx.writeAndFlush(message);

					if (files.length() > 0) {//�ɶ��ֽڴ���0����ʾû���ֽھͲ�����
						RandomAccessFile raf = null;
						try {
							raf = new RandomAccessFile(files.getPath(), "r");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} /**
			             * model�����������
			             * r ������ֻ����ʽ��ָ���ļ�
			             * rw �Զ�д��ʽ��ָ���ļ�
			             * rws ��д��ʽ�򿪣��������ݻ�Ԫ���ݶ�ͬ��д��ײ�洢�豸
			             * rwd ��д��ʽ�򿪣����ļ����ݵĸ���ͬ���������ײ�洢�豸
			             * �ο���ַ��https://www.cnblogs.com/nightsu/p/5938950.html
			             **/
						try {
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
					//fileInputStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
