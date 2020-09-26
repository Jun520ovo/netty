package com.nettyFile.manyFile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Massage>{

	@Override
	protected void encode(ChannelHandlerContext arg0, Massage massage, ByteBuf arg2) throws Exception {
		if(massage.getState() != null) {
			arg2.writeInt(2);//��ʾ�������
			arg2.writeInt(massage.getState());
		}else {//��ʾ��û�з������
			if(massage.getFileDirectory() != null) {//�ļ����������Ϊ�յĻ�  ��ʾ�ļ����Ѿ�������
				arg2.writeInt(0);//0��ʾ�ļ���
				arg2.writeInt(massage.getFileDirectoryLength());
				arg2.writeBytes(massage.getFileDirectory().getBytes());
			}else {//��Ȼ���������ļ�
				arg2.writeInt(1);//1��ʾ�ļ���
				arg2.writeInt(massage.getNameLength());//�����ļ����ֽڳ���
				arg2.writeLong(massage.getContentLength());//�ļ������ֽ�
				arg2.writeBytes(massage.getName().getBytes());//�����ļ���
			}
		}
	}
}
