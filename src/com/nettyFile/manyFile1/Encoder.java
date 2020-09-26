package com.nettyFile.manyFile1;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message>{
	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		if(message.getDirectory() == null){
			out.writeInt(1);
			out.writeInt(message.getNameLength());
			out.writeBytes(message.getName().getBytes());
			out.writeLong(message.getContentLength());
		}else{//ÎÄ¼þ¼Ð
			out.writeInt(0);
			out.writeInt(message.getDirectoryLength());
			out.writeBytes(message.getDirectory().getBytes());
		}
	}
}
