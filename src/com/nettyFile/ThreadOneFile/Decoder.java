package com.nettyFile.ThreadOneFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder extends ByteToMessageDecoder {
	public static Map<ChannelHandlerContext, Boolean> mark= new HashMap<ChannelHandlerContext, Boolean>();
	public int headLength = 4;//�������ļ����ֽڳ���
	/**
	 * ���ļ����ȷ� �ļ������� �ļ���  �ļ����ݳ��� �ļ�����      �ļ���Ҳ��һ����  �ļ��������� �ļ�����
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// ������һ���ļ�������,�ڶ��ļ���ͷ��Ϣ,�ڶ����ļ�������,�����ļ���ͷ��Ϣ,�������ļ�������
		// �Ƚ���ͷ,���ͷ��Ϣ���������.����Ϊfalse
		System.out.println(mark.get(ctx) + " === " + ctx.hashCode());
		if (mark.get(ctx) == null ? true : mark.get(ctx)) {
		
			in.markReaderIndex(); // ���Ǳ��һ�µ�ǰ��readIndex��λ�� // ��readInt()������������readIndex����4
			
			int data = in.readableBytes();//������еĿɶ��ֽ���
			if (data < 4) {
				in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
				return;
			}
			
			int state = in.readInt();
			
			if (state == 0) {// �ļ���
				try {

					data = in.readableBytes();
					if (data < 4) {
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					int dataLength = in.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf
					if (dataLength < 0) { // ���Ƕ�����intΪ�������ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					if (in.readableBytes() < dataLength) { // ��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex.
															// ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					byte[] body = new byte[dataLength];
					in.readBytes(body);
					mark.put(ctx, false);
					Message message = new Message();
					message.setSign(state);
					message.setName(new String(body));//���ļ����ַ������������  
					out.add(message);
				} catch (Exception e) {
					e.printStackTrace();
				System.err.println("�׳����쳣---Decord");
				}
			} else {
				try {
					data = in.readableBytes();
					if (data < headLength) {
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
					int dataLength = in.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf Ҳ�����ļ���
					if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					if (in.readableBytes() < dataLength) { // ��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex.
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					byte[] body = new byte[dataLength];
					in.readBytes(body);
					if (in.readableBytes() < 8) { // ��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex.
						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
				//	mark.put(ctx, false);
					long contentLength = in.readLong();//��ȡ�ļ����ܳ���
					Message message = new Message();
					message.setSign(state);
					message.setName(new String(body));
					message.setContentLength(contentLength);
					out.add(message);

				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("�׳����쳣---Decord");
				}
			}
		} else {
			byte[] bytes = new byte[in.readableBytes()];
			in.readBytes(bytes);
			out.add(bytes);
		}

	}
}
