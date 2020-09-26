package com.nettyFile.ThreadManyFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServlerHandler extends ChannelInboundHandlerAdapter {
	private FileOutputStream fos;
	private BufferedOutputStream bufferedOutputStream;
	private String prefix;

	private String OK = "ok";
	private long contentLength = 0;// �ļ����ݳ���
	private long contentSumLength = 0;// ������ݵ��ֽ���
	private int i = 0;

	public ServlerHandler(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		if (msg instanceof Message) {

			Message message = (Message) msg;
			if (message.getDirectory() != null) {// �ļ��оͽ�������
				System.out.println(" ----- ");
				File file = new File(prefix + message.getDirectory());
				if (!file.exists()) {
					file.mkdirs();// �����ļ���
				}
			} else {// �ļ�
				contentSumLength = message.getContentLength();
				File file = new File(prefix + message.getName());
				//System.out.println(file + "������........");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (message.getContentLength() > 0) {
					try {
						fos = new FileOutputStream(file);
						bufferedOutputStream = new BufferedOutputStream(fos);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return;
		}
		try {
			byte[] bytes = (byte[]) msg;
			contentLength = contentLength + bytes.length;
			bufferedOutputStream.write(bytes, 0, bytes.length);
			bufferedOutputStream.flush();
			// 100 10 10 10 10 10 10 10 10 10 10
			if (contentLength == contentSumLength) {
				//System.out.println("�������");
				contentLength = 0;
				contentSumLength = 0;
				bufferedOutputStream.close();
			}
			// buf.release();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
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
