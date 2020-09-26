package com.netty.Third;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * �Զ�����Ϣͷ ������
 * 
 * @author Ф����
 *
 *         2017��6��14������11:05:06
 * @desc
 */
public class Decord extends ByteToMessageDecoder {
	int HEAD_LENGTH = 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		try {

			int data = in.readableBytes();
			if (data < HEAD_LENGTH) { // ���HEAD_LENGTH���������ڱ�ʾͷ���ȵ��ֽ�����
										// �����������Ǵ�����һ��int���͵�ֵ����������HEAD_LENGTH��ֵΪ4.
				return;
			}
			in.markReaderIndex(); // ���Ǳ��һ�µ�ǰ��readIndex��λ��
			
			//��ʶ
			//����1,�ɹ�
			//����2.�ɹ�
			//����3,���ɹ�(���˵���ǰ��)
			
			
			
			
			// 4   abcdeabcde
			
			
			int dataLength = in.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf
											// ��readInt()������������readIndex����4
			if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
				ctx.close();
			}

			if (in.readableBytes() < dataLength) { // ��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex.
													// ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
				in.resetReaderIndex();
				return;
			}

			byte[] body = new byte[dataLength]; // �ţ���ʱ�����Ƕ����ĳ��ȣ��������ǵ�Ҫ���ˣ��Ѵ��͹��������ݣ�ȡ������~~
			in.readBytes(body); //
			String o = new String(body);
			//Object o = JSONObject.parse(body); // ��byte����ת��Ϊ������Ҫ�Ķ���α���룬��ʲô���л�������ѡ��
			out.add(o);
		} catch (Exception e) {
			
		}
	}
}