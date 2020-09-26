package com.netty.Third;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	private int cont;
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//客户端发消息来的时候进到这里来
		String body = (String) msg;
		System.out.println("服务端接到客户端消息为 : " + body+"....."+ ++cont);
		
		
		String currentTime = "这是随便写的  傻逼".equalsIgnoreCase(body)
				? new java.util.Date(System.currentTimeMillis()).toString() : "fuwuduanfageikeuhduan2222222";
		//ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(currentTime);
	}
	// channelRead执行后触发
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	// 出错是会触发，做一些错误处理
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
