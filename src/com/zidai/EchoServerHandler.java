package com.zidai;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * Sharable��ʾ�˶�����channel�乲�� handler�������ǵľ���ҵ����
 * */
//@Sharable
// ע��@Sharable����������channels�乲��
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println(msg);
		System.out.println();
		System.out.println();
		//ctx.write(msg);// д�����ݣ�
	}
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//System.out.println("���Ƿ�������ӳɹ�");
	}
}