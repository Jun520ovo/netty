package com.nettyFile.ThreadOneFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{
	private String filepath = "C:\\Users\\Administrator\\Desktop\\Socketserver\\";
	private FileOutputStream fos;
	private BufferedOutputStream bufferedOutputStream;
	private long fileLength = 0;
	private long sum= 0;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			Message ms = (Message) msg;
			if(ms.getSign() == 0) {//��ʾ�ļ���
				
				File file = new File(filepath+ms.getName());
				if(!file.exists()) {
					file.mkdirs();
				}
				ctx.writeAndFlush("0");
				Decoder.mark.put(ctx, true);
				ctx.close();
				return ;
			}else {//��ʾΪ�ļ�
				File file = new File(filepath+ms.getName());
				if (!file.exists()) {
					file.createNewFile();
				}
				
				String test = "1";
				
				fileLength =ms.getContentLength();
				 if(fileLength == 0) {//�ļ�Ϊ��ʱ�ر�
					 	System.out.println("�ļ�Ϊ���ѹر�");
						Decoder.mark.put(ctx, true);
						test = "0";
						ctx.writeAndFlush(test);
						ctx.close();
						return ;
				 }
				 
				 ctx.writeAndFlush(test);
				 if(fileLength != 0)//�ļ���Ϊ�յ�ʱ�� return��ȥ���ļ�����
					{
						fos =  new FileOutputStream(file);
						bufferedOutputStream = new BufferedOutputStream(fos);
					}
					return ;
			}
		}else {
			byte[] bytes= (byte[]) msg;
			sum = sum + bytes.length;
			bufferedOutputStream.write(bytes, 0, bytes.length);
			bufferedOutputStream.flush();
    		if(sum == fileLength)//�ļ�����ʱ�ر�
    		{
    			System.out.println("�������....");
    			Decoder.mark.put(ctx, true);
    			bufferedOutputStream.close();
				ctx.writeAndFlush("0");
				ctx.close();
    		}
		}
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
