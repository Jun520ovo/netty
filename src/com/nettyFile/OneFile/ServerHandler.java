package com.nettyFile.OneFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	
	private boolean first = true;
	private FileOutputStream fos;
	private BufferedOutputStream bufferedOutputStream;
	private static String prefix = "C:\\Users\\Administrator\\Desktop\\Socketserver\\";
	private String OK = "ok";
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//客户端发消息来的时候进到这里来
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		if (first) {
			String fileName = new String(bytes);
			System.out.println(fileName);
			first = false;
			File file = new File(prefix + fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				fos = new FileOutputStream(file);
				bufferedOutputStream = new BufferedOutputStream(fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ctx.writeAndFlush(OK.getBytes());
			buf.release();
			return;
		}

		try {
			bufferedOutputStream.write(bytes, 0, bytes.length);
			buf.release();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
	}
	// channelRead执行后触发
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	// 出错是会触发，做一些错误处理
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
