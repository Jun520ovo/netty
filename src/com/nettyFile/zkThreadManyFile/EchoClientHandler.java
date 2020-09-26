/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.nettyFile.zkThreadManyFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

/**
 * Handler implementation for the echo client. It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	private String context;
	private static String path = "D:\\绝密\\springmvc";

	public EchoClientHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		File file =new File(path);
		recursionDirectory(file, ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * �ݹ��ļ�
	 */
	public void recursionDirectory(File file, ChannelHandlerContext ctx) {
		try {
			File[] fileArray = file.listFiles();//���file�����µ������ļ����ļ�������
			for (File files:fileArray) {//ѭ���ļ��µ������ļ�
				if (files.isDirectory()) {//�Ƿ����ļ���
					System.out.println("�ļ���:" + files);
					Message message = new Message();
					String directoryName = files.getPath().replace(path,"");//������·�����ļ���
					message.setDirectoryLength(directoryName.getBytes().length);
					message.setDirectory(directoryName);
					ctx.writeAndFlush(message);
					recursionDirectory(files, ctx);
				} else {//�������ļ�
					//this.file = files;//��files���󸳸�file����
					System.out.println("�ļ�:" + files);
					String directoryName = files.getPath().replace(path,"");//������·�����ļ���
					Message message = new Message();
					message.setNameLength(directoryName.getBytes().length);
					message.setName(directoryName);
					message.setContentLength(files.length());
					ctx.writeAndFlush(message);

					
					if (files.length() > 0) {//�ɶ��ֽڴ���0����ʾû���ֽھͲ�����
						RandomAccessFile raf = null;
						try {
							raf = new RandomAccessFile(files.getPath(), "r");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						try {
							ctx.writeAndFlush(new ChunkedFile(raf)).addListener(new ChannelFutureListener(){
								@Override
								public void operationComplete(ChannelFuture future) throws Exception {
									//future.channel().close();
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
