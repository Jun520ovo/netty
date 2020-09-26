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
	private List<File> list = new ArrayList<>();//存放创建的空文件集合
	private int i = 0;
	
	public ClientHandler(String directory) {
		this.directory = directory;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		recursionDirectory(new File(directory), ctx);//发送文件对象
		System.out.println("发送文件数:"+i);
		
		Massage message = new Massage();
		message.setState(1);//表示发送完成
		ctx.writeAndFlush(message);//循环完毕将发送文件数传递过去
		
	}//进来这个类就会加载这个方法
	/**
	 * 递归文件
	 */
	private String filename =   i+".zip";
	public void recursionDirectory(File file,ChannelHandlerContext ctx) throws Exception {
		try {
			File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
			if(null == fileArray) {
				this.file = file;//将files对象赋给file对象
				++i;
				System.out.println("文件:" + file);
				//发送除文件内容外所有信息
				Massage message = new Massage();
				//String directoryName = file.getPath().replace(directory,"");//获得相对路径加文件名
				message.setNameLength(filename.getBytes().length);
				message.setName(filename);
				message.setContentLength(file.length());//设置内容长度的字节
				ctx.writeAndFlush(message);//将对象通过通道输出
				if(message.getContentLength() != 0){
					list.add(file);
				}
			}else {
			for (File file2 : fileArray) {
				++i;
				if(file2.isDirectory()) {//判断是不是文件夹
					System.out.println("文件夹" + file2);
					Massage message = new Massage();
					String directoryName = file2.getPath().replace(directory,"");//获得相对路径加文件名
					message.setFileDirectoryLength(directoryName.getBytes().length);
					message.setFileDirectory(directoryName);//将文件目录路径加入
					ctx.writeAndFlush(message);//将对象通过通道输出
					recursionDirectory(file2, ctx);
				}else {//否则为文件
					this.file = file2;//将files对象赋给file对象
					System.out.println("文件:" + file2);
					//发送除文件内容外所有信息
					Massage message = new Massage();
					String directoryName = file2.getPath().replace(directory,"");//获得相对路径加文件名
					message.setNameLength(directoryName.getBytes().length);
					message.setName(directoryName);
					message.setContentLength(file2.length());//设置内容长度的字节
					ctx.writeAndFlush(message);//将对象通过通道输出
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
			//发送内容
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(file, "r");//根据指定路径获取只读的对象
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				//发送字节
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
	}//接收到服务端的数据就会进来这个方法
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}//抛异常就进入这个方法
}
