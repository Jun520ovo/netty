package com.nettyFile.ThreadOneFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter{
	private String filepath ;
	private static String basepath ="D:\\����\\spring";//���͵��ļ���
	private int sign;
	public ClientHandler(String filepath,int sign) {
		this.filepath =filepath ;
		this.sign = sign;
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Message message = new Message();
		message.setSign(sign);
		if(sign == 1) {//��ʾ�ļ�
			message.setContentLength(new File(filepath).length());//�ļ����ݳ���
		}
		String directoryName = new File(filepath).getPath().replace(basepath,"");//������·�����ļ���
		message.setNameLength(directoryName.getBytes().length);//�ļ������ֳ���
		message.setName(filepath.replace(basepath,""));//�ļ�����
		ctx.writeAndFlush(message);
		
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String test = (String)msg;
		if(test.equals("0")) {//�ʹ���û�ж���������  ���ļ���  ����Ҫ�ر�
			System.out.println("���ļ��е��ѹر�");
			ctx.close();
		}else if(test.equals("1")) {
				
			RandomAccessFile raf = null;
			try {  
				raf = new RandomAccessFile(filepath, "r");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						System.out.println("���ļ��Ĺر���");
						future.channel().close();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			ctx.close();
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
	
}

