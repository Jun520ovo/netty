package com.netty.Third;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * �Զ�����Ϣͷ ������
 * 
 * @author Ф����
 *         2017��6��14������11:10:03
 * @desc
 */
public class Encoder extends MessageToByteEncoder<String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String str, ByteBuf out) throws Exception {
		try {
			byte[] body = str.getBytes(); // ������ת��Ϊbyte��α���룬������ʲô�������л�����������ѡ�񡣴˴��õ���fastJson
		
			int dataLength = body.length; // ��ȡ��Ϣ�ĳ���
			out.writeInt(dataLength); // �Ƚ���Ϣ����д�룬Ҳ������Ϣͷ
			out.writeBytes(body); // ��Ϣ���а�������Ҫ���͵�����
		} catch (Exception e) {
			System.out.println(e + "");
		}
	}
}