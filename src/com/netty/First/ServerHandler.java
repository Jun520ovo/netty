package com.netty.First;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//客户端发消息来的时候进到这里来
		ByteBuf buf = (ByteBuf) msg;//接到的消息是一个obj  转成bytebuf数组
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("服务端接到客户端消息为 : " + body);
		
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
				? new java.util.Date(System.currentTimeMillis()).toString() : "fuwuduanfageikeuhduan2222222";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
