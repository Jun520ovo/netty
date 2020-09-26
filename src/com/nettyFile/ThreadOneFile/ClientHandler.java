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
	private static String basepath ="D:\\绝密\\spring";//发送的文件夹
	private int sign;
	public ClientHandler(String filepath,int sign) {
		this.filepath =filepath ;
		this.sign = sign;
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Message message = new Message();
		message.setSign(sign);
		if(sign == 1) {//表示文件
			message.setContentLength(new File(filepath).length());//文件内容长度
		}
		String directoryName = new File(filepath).getPath().replace(basepath,"");//获得相对路径加文件名
		message.setNameLength(directoryName.getBytes().length);//文件夹名字长度
		message.setName(filepath.replace(basepath,""));//文件夹名
		ctx.writeAndFlush(message);
		
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String test = (String)msg;
		if(test.equals("0")) {//就代表没有东西传输了  是文件夹  就需要关闭
			System.out.println("传文件夹的已关闭");
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
						System.out.println("传文件的关闭了");
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

