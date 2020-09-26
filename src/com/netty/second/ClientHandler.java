package com.netty.second;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler  extends ChannelInboundHandlerAdapter{
	//private final ByteBuf firstMessage;
	
	private int counter;
	
	private byte[] req;
	
	public ClientHandler() {
		req = ("kehuduan" + System.getProperty("line.separator")).getBytes();
	}
	public void channelActive(ChannelHandlerContext ctx) {//发送的方法
		
		
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("客戶端接到服務端消息 : " + body + ++counter);
	}
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
		
}
