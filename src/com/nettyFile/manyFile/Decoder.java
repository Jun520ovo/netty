package com.nettyFile.manyFile;

import java.util.ArrayList;
import java.util.List;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder  extends ByteToMessageDecoder{
		public  static boolean bool = true;
		public int headLength = 4 + 8;//�������ļ����ֽڳ��ȣ��ļ����·�����ֽڳ���
		public int directoryLength = 4;//�������ļ��е��ֽڳ��ȣ���Ϊ��Int,������4
		public static int i = 0;
		public static List<Long> list = new ArrayList<>();
		public static int count = 0;//listѭ�����±�
		public static long sum;//���ֽ���
		
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
		if
		(bool) {
			System.out.println(++i);
			arg1.markReaderIndex();
			
			int a = arg1.readableBytes();//������еĿɶ��ֽ���
			if(a < 4) {//����ɶ��ֽ�С��ͷ�ֽڴ�С����������
				arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
				return;//����
			}
			int mark = arg1.readInt();
			
				
			if(mark == 1) {//��ʾ�ļ�
				try {
					int data = arg1.readableBytes();//������еĿɶ��ֽ���
					if(data <headLength) {//����ɶ��ֽ�С��ͷ�ֽڴ�С����������
						System.err.println("�ļ��ɶ��ֽ�����С��ͷ�ļ��ֽ�����С");
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;//����
					}
					int dataLength = arg1.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf	readInt()������������ridx����4
					//int pathLength = in.readInt();
					
					if (arg1.readableBytes() < 8) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�,�൱�ڻع�
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
					long contentLength = arg1.readLong();//ȫ�����ݳ���
					
					if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}

					if (arg1.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�,�൱�ڻع�
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
					//������������������
					byte[] body = new byte[dataLength];//����һ���ȳ���byte����
					arg1.readBytes(body);//��ȡ���ݵ�byte������arg1
					
					Massage massage = new Massage();
					massage.setName(new String(body));
					massage.setContentLength(contentLength);
					if(massage.getContentLength() != 0){
						list.add(contentLength);//��ӵ�������
					}
					System.out.println("�ļ��� =================== " + massage.getName());
					System.out.println("�ļ��ֽڳ��� =================== " + massage.getContentLength());

					//mark = false;//����Ϊfalseȡ��ȡ�ļ�����
					arg2.add(massage);//��������ӵ������У���Ӻ���Զ������handler��
				} catch (Exception e) {
				e.printStackTrace();
				}
			}else if(mark == 0) {//��ʾ�ļ���
				try {
					int data = arg1.readableBytes();//������еĿɶ��ֽ���
					if (data < directoryLength) { //����ɶ��ֽ�С��ͷ�ֽڴ�С����������
						System.err.println("�ļ��пɶ��ֽ�����С��ͷ�ļ��ֽ�����С");
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;//����
					}
				
					int dataLength = arg1.readInt(); // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf	readInt()������������ridx����4
					if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
					if (arg1.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�,�൱�ڻع�
						arg1.resetReaderIndex();//������ǰ���,��ʱ�����¶�ȡ.�������´εĽ���ֵ�ۼ�
						return;
					}
					//������������������
					byte[] body = new byte[dataLength];//����һ���ȳ���byte����
					arg1.readBytes(body);//��ȡ���ݵ�byte������

					Massage message = new Massage();
					message.setFileDirectory(new String(body));//����ļ���
					System.out.println("�ļ�Ŀ¼ -------------- " + message.getFileDirectory());
					arg2.add(message);//��������ӵ������У���Ӻ���Զ������handler��
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("�׳����쳣");
				}
			}else if(mark == 2){
				arg2.add(arg1.readInt());
				System.out.println("�ļ����������");
				bool = false;
			}
		}else {//�����Ϊture ��Ϊ��
			long contentLength = list.get(count);
			long surplusLength = (long)contentLength - (long)sum;//�ܳ���  -  �Ѷ��ۼӳ��� = ʣ�೤��
			byte[] bytes;
			//readableBytes()�õ��ɶ��ֽ���
			if(arg1.readableBytes() > surplusLength){//��������ˣ�������͵�����������һ��ճһ���ˣ���ֻ���Լ���������һ���������ţ�Ȼ�󣬵ȴ���һ���ļ��������͹�������ʣ��������ͳ�ȥ
				bytes = new byte[Integer.parseInt(String.valueOf(surplusLength))];//����һ���ȳ���byte����
				arg1.readBytes(bytes);//������ļ�
				sum += surplusLength;//���յ����ֽ���
				if(contentLength == sum){//��������꣬������һ��
					count++;
					sum = 0;//���
				}
			}else{
				bytes = new byte[arg1.readableBytes()];
				arg1.readBytes(bytes);//������ļ�
				sum += bytes.length;//���յ����ֽ���
				if(contentLength == sum){//��������꣬������һ��
					count++;
					sum = 0;//���
				}
			}
			arg2.add(bytes);//��bytes���뼯����
		}
	}
}
