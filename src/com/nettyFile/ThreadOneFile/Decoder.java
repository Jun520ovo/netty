package com.nettyFile.ThreadOneFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder extends ByteToMessageDecoder {
	public static Map<ChannelHandlerContext, Boolean> mark= new HashMap<ChannelHandlerContext, Boolean>();
	public int headLength = 4;//首先是文件名字节长度
	/**
	 * 发文件得先发 文件名长度 文件名  文件内容长度 文件内容      文件夹也是一样的  文件夹名长度 文件夹名
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 包括第一个文件的内容,第二文件的头信息,第二个文件的内容,第三文件的头信息,第三个文件的内容
		// 先接收头,如果头信息接收完成了.设置为false
		System.out.println(mark.get(ctx) + " === " + ctx.hashCode());
		if (mark.get(ctx) == null ? true : mark.get(ctx)) {
		
			in.markReaderIndex(); // 我们标记一下当前的readIndex的位置 // 的readInt()方法会让他的readIndex增加4
			
			int data = in.readableBytes();//获得所有的可读字节数
			if (data < 4) {
				in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
				return;
			}
			
			int state = in.readInt();
			
			if (state == 0) {// 文件夹
				try {

					data = in.readableBytes();
					if (data < 4) {
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					int dataLength = in.readInt(); // 读取传送过来的消息的长度。ByteBuf
					if (dataLength < 0) { // 我们读到的int为负数，是不应该出现的情况，这里出现这情况，关闭连接。
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					if (in.readableBytes() < dataLength) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
															// 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					byte[] body = new byte[dataLength];
					in.readBytes(body);
					mark.put(ctx, false);
					Message message = new Message();
					message.setSign(state);
					message.setName(new String(body));//把文件名字放在这个对象里  
					out.add(message);
				} catch (Exception e) {
					e.printStackTrace();
				System.err.println("抛出了异常---Decord");
				}
			} else {
				try {
					data = in.readableBytes();
					if (data < headLength) {
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
					int dataLength = in.readInt(); // 读取传送过来的消息的长度。ByteBuf 也就是文件名
					if (dataLength < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					if (in.readableBytes() < dataLength) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					byte[] body = new byte[dataLength];
					in.readBytes(body);
					if (in.readableBytes() < 8) { // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
						in.resetReaderIndex();// 返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
				//	mark.put(ctx, false);
					long contentLength = in.readLong();//获取文件的总长度
					Message message = new Message();
					message.setSign(state);
					message.setName(new String(body));
					message.setContentLength(contentLength);
					out.add(message);

				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("抛出了异常---Decord");
				}
			}
		} else {
			byte[] bytes = new byte[in.readableBytes()];
			in.readBytes(bytes);
			out.add(bytes);
		}

	}
}
