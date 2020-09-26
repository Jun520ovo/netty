package com.nettyFile.OneFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class ClientHandler  extends ChannelInboundHandlerAdapter{
	private String filename;		
	
	public ClientHandler() {
		filename = "sb.zip";
		}
	public void channelActive(ChannelHandlerContext ctx) {//进来这个类就会进这个方法
		ctx.writeAndFlush(filename.getBytes(CharsetUtil.UTF_8));//以防名称有中文 转为utf—8编码
	}
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//收到服务端反回的数据就进来这个方法
		String str = (String) msg;
		System.out.println(str);
		
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile("F:\\zip\\开发工具.zip", "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().close();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//抛异常进来这个方法
		ctx.close();
	}
	
		
}
