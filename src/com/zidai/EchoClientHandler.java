package com.zidai;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;  
  
@Sharable  
public class EchoClientHandler extends ChannelInboundHandlerAdapter {  
    /** 
     *�˷����������ӵ��������󱻵���  
     * */  
    public void channelActive(ChannelHandlerContext ctx) {  
    	for(int i =0;i<1000;i++)
    	{
    		ctx.writeAndFlush("test123456");
    	}
        
    }

	  
  
  
}  