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
	public void channelActive(ChannelHandlerContext ctx) {//���������ͻ���������
		ctx.writeAndFlush(filename.getBytes(CharsetUtil.UTF_8));//�Է����������� תΪutf��8����
	}
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//�յ�����˷��ص����ݾͽ����������
		String str = (String) msg;
		System.out.println(str);
		
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile("F:\\zip\\��������.zip", "r");
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//���쳣�����������
		ctx.close();
	}
	
		
}
