package com.nettyFile.ThreadOneFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext cxf, Message message, ByteBuf out) throws Exception {
		if(message.getSign() == 1){//表示是文件
			out.writeInt(1);
			out.writeInt(message.getNameLength());
			out.writeBytes(message.getName().getBytes());
			out.writeLong(message.getContentLength());
		}else{//文件夹
			out.writeInt(0);
			out.writeInt(message.getNameLength());
			out.writeBytes(message.getName().getBytes());
		}
	}
}
