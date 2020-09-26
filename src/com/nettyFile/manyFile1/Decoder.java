package com.nettyFile.manyFile1;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder extends ByteToMessageDecoder {
	int HEAD_LENGTH = 4;
	private long contentSumLength = 0;
	private long contentLength = 0;
	public static boolean mark = true;
	public int headLength = 4;//�������ļ����ֽڳ���
	/**
	 * ���ļ����ȷ� �ļ������� �ļ���  �ļ����ݳ��� �ļ�����      �ļ���Ҳ��һ����  �ļ��������� �ļ�����
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// ������һ���ļ�������,�ڶ��ļ���ͷ��Ϣ,�ڶ����ļ�������,�����ļ���ͷ��Ϣ,�������ļ�������
		// �Ƚ���ͷ,���ͷ��Ϣ���������.����Ϊfalse
		if (mark) {
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

					Message message = new Message();
					message.setDirectory(new String(body));
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

//					data = in.readableBytes();
//					if (dataLength < HEAD_LENGTH) { // ���HEAD_LENGTH���������ڱ�ʾͷ���ȵ��ֽ�����
//						in.resetReaderIndex();// ������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
//						return;
//					}

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
					long contentLength = in.readLong();//��ȡ�ļ����ܳ���
					Message message = new Message();
					message.setName(new String(body));
					
					message.setContentLength(contentLength);

					contentSumLength = contentLength;
					if (contentLength != 0) {
						mark = false;
					} else {
					
					}
					out.add(message);

				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("�׳����쳣---Decord");
				}
			}
		} else {
			// ��������ܳ��ȼ�ȥ�ۼӵĳ���,���С�ڿɶ�����(�ɶ�����������һ�������������ļ�.�������ʱ��,ֻ����ǰ����ļ��Լ��ĳ���)
			if (in.readableBytes() > contentSumLength - contentLength) {
				// buf.skipBytes(buf.readableBytes());
				byte[] bytes = new byte[Integer.parseInt(String.valueOf(contentSumLength - contentLength))];
				in.readBytes(bytes);
				contentSumLength = 0;
				contentLength = 0;
				out.add(bytes);
			} else {// ����ļ����ܳ���,�������ǿɶ����ۼӵĳ���

				byte[] bytes = new byte[in.readableBytes()];
				contentLength = contentLength + bytes.length;
				in.readBytes(bytes);
				if (contentLength == contentSumLength) {
					contentLength = 0;
					contentSumLength = 0;
				}
				out.add(bytes);
			}
		}

	}
}
