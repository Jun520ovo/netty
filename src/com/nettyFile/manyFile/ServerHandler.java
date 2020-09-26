package com.nettyFile.manyFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	private FileOutputStream fos;//���������
	private List<Massage> list = new ArrayList<>();
	private static String prefix = "C:\\Users\\Administrator\\Desktop\\Socketserver\\";//������ļ�Ŀ¼
	private Massage massage;
	private static int minCount = 0;//���ϵı������±�����
	private int maxCount;//���ϵı������±�����
	private long count;//��ȡ���ֽ���
	private int copy = -1;
	private FileOutputStream fileOutputStream;

	public static String OK = "ok";
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Massage) {//�Ƿ�Ϊmessage����
			massage = (Massage) msg;
			if(massage.getFileDirectory() == null) {
				File file = new File(prefix + massage.getName());//����һ��file����
				if (!file.exists()) {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();//�����ļ���
					}
					file.createNewFile();//�����ļ�
				}
				massage.setFile(file);
				if(massage.getContentLength() != 0){
					list.add(massage);//��ӽ�����
				}
			}else{//��ʾ���ļ�Ŀ¼
				File file = new File(prefix + massage.getFileDirectory());
				if (!file.exists()) {//��������ھʹ���
					file.mkdirs();//�����ļ���
				}
			}
			return;
		}else if(msg instanceof Integer){
			ctx.writeAndFlush(OK.getBytes());//write��flush,����ok���ͻ��ˣ��ͻ��˽��յ�ֵ���������ļ����ݵ���
			maxCount = list.size()-1;//��¼����±���
		}else if(msg instanceof byte[]){//�ļ�����
			Massage message = list.get(minCount);
			File file = message.getFile();
			//System.out.println(file);
			if(copy != minCount){//��һ�β���ȣ�Ȼ���������Ͳ�����
				fileOutputStream = new FileOutputStream(file);//������ָ��һ���µ��ڴ��ַ�������Ǹ��ǵ�ԭ���ĵ�ֵַ������ÿ�ζ�Ҫ�رա�
				System.out.println(fileOutputStream.hashCode() + "hashcode=============");
			}
			copy = minCount;
			try {
				byte[] bytes = (byte[]) msg;//����Ƿ��͵���byte�����ת��byte����
				fileOutputStream.write(bytes,0,bytes.length);//������ļ�
				long contentLength = message.getContentLength();
				count += bytes.length;//���յ����ֽ���
				fileOutputStream.flush();
				if(contentLength == count){//��������꣬������һ��
					if(minCount == maxCount){//���һ��ִ�еķ���
						Decoder.count = 0;//����Ϊ0
						Decoder.bool = true;//���½���ͷ
						Decoder.list.clear();
						list.clear();
						copy = -1;
						minCount = 0;
					}else{
						minCount++;
						count = 0;
					}
					fileOutputStream.close();//�ļ�������Ϻ󣬹ر���
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	  @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) {
	    	ctx.flush();
		}

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();
	        ctx.close();
	    }
	
}
