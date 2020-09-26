package com.nettyFile.manyFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Massage>{

	@Override
	protected void encode(ChannelHandlerContext arg0, Massage massage, ByteBuf arg2) throws Exception {
		if(massage.getState() != null) {
			arg2.writeInt(2);//表示发送完成
			arg2.writeInt(massage.getState());
		}else {//表示还没有发送完成
			if(massage.getFileDirectory() != null) {//文件夹名如果不为空的话  表示文件夹已经发送了
				arg2.writeInt(0);//0表示文件夹
				arg2.writeInt(massage.getFileDirectoryLength());
				arg2.writeBytes(massage.getFileDirectory().getBytes());
			}else {//不然就是属于文件
				arg2.writeInt(1);//1表示文件夹
				arg2.writeInt(massage.getNameLength());//发送文件名字节长度
				arg2.writeLong(massage.getContentLength());//文件内容字节
				arg2.writeBytes(massage.getName().getBytes());//发送文件名
			}
		}
	}
}
