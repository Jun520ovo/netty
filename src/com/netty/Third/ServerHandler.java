package com.netty.Third;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	private int cont;
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//�ͻ��˷���Ϣ����ʱ�����������
		String body = (String) msg;
		System.out.println("����˽ӵ��ͻ�����ϢΪ : " + body+"....."+ ++cont);
		
		
		String currentTime = "�������д��  ɵ��".equalsIgnoreCase(body)
				? new java.util.Date(System.currentTimeMillis()).toString() : "fuwuduanfageikeuhduan2222222";
		//ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(currentTime);
	}
	// channelReadִ�к󴥷�
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	// �����ǻᴥ������һЩ������
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
