package com.netty.Third;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler  extends ChannelInboundHandlerAdapter{
	private  ByteBuf firstMessage;
	
	private int cont;
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 100; i++) {
			//byte[] req = "zheshikehude11111111".getBytes();
			//firstMessage = Unpooled.buffer(req.length);
			//firstMessage.writeBytes(req);
			ctx.writeAndFlush("kehuduanfadeshuju11....");
		}
	}
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String  buf = (String) msg;
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(buf, "UTF-8");
		System.out.println("客戶端接到服務端消息 : " + buf +"........"+ ++cont);
	}
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
		
}
