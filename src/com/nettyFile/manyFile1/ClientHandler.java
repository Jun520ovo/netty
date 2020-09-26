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
	 * 递归文件
	 */
	public void recursionDirectory(File file, ChannelHandlerContext ctx) {
		try {
			File[] fileArray = file.listFiles();//获得file对象下的所有文件、文件夹数组
			for (File files:fileArray) {//循环文件下的所有文件
				if (files.isDirectory()) {//是否是文件夹
					System.out.println("文件夹:" + files);
					Message message = new Message();
					String directoryName = files.getPath().replace(path,"");//获得相对路径加文件名
					directoryName.getBytes(CharsetUtil.UTF_8);
					
					message.setDirectoryLength(directoryName.getBytes().length);
					message.setDirectory(directoryName);
					//ctx.writeAndFlush(((String)message).getBytes(CharsetUtil.UTF_8));//以防名称有中文 转为utf—8编码
					ctx.writeAndFlush(message);
					recursionDirectory(files, ctx);
				} else {//否则是文件
					//this.file = files;//将files对象赋给file对象
					System.out.println("文件:" + files);
					String directoryName = files.getPath().replace(path,"");//获得相对路径加文件名
					Message message = new Message();
					message.setNameLength(directoryName.getBytes().length);
					message.setName(directoryName);
					//FileInputStream fileInputStream = new FileInputStream(files);
				//	try {
					message.setContentLength(files.length());//这里将可读字节长度加入
					//} catch (Exception e) {
						//e.printStackTrace();
					//}
					ctx.writeAndFlush(message);

					if (files.length() > 0) {//可读字节大于0，表示没有字节就不进入
						RandomAccessFile raf = null;
						try {
							raf = new RandomAccessFile(files.getPath(), "r");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} /**
			             * model各个参数详解
			             * r 代表以只读方式打开指定文件
			             * rw 以读写方式打开指定文件
			             * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
			             * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
			             * 参考网址：https://www.cnblogs.com/nightsu/p/5938950.html
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
