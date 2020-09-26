package com.nettyFile.manyFile;

import java.util.ArrayList;
import java.util.List;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder  extends ByteToMessageDecoder{
		public  static boolean bool = true;
		public int headLength = 4 + 8;//首先是文件名字节长度，文件相对路径的字节长度
		public int directoryLength = 4;//这是是文件夹的字节长度，因为是Int,所以是4
		public static int i = 0;
		public static List<Long> list = new ArrayList<>();
		public static int count = 0;//list循环的下标
		public static long sum;//总字节数
		
	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {
		if
		(bool) {
			System.out.println(++i);
			arg1.markReaderIndex();
			
			int a = arg1.readableBytes();//获得所有的可读字节数
			if(a < 4) {//如果可读字节小于头字节大小则满足条件
				arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
				return;//返回
			}
			int mark = arg1.readInt();
			
				
			if(mark == 1) {//表示文件
				try {
					int data = arg1.readableBytes();//获得所有的可读字节数
					if(data <headLength) {//如果可读字节小于头字节大小则满足条件
						System.err.println("文件可读字节数件小于头文件字节数大小");
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;//返回
					}
					int dataLength = arg1.readInt(); // 读取传送过来的消息的长度。ByteBuf	readInt()方法会让他的ridx增加4
					//int pathLength = in.readInt();
					
					if (arg1.readableBytes() < 8) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方,相当于回滚
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
					long contentLength = arg1.readLong();//全部内容长度
					
					if (dataLength < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}

					if (arg1.readableBytes() < dataLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方,相当于回滚
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
					//如果接收完则进入这里
					byte[] body = new byte[dataLength];//构建一个等长的byte数组
					arg1.readBytes(body);//读取数据到byte数组中arg1
					
					Massage massage = new Massage();
					massage.setName(new String(body));
					massage.setContentLength(contentLength);
					if(massage.getContentLength() != 0){
						list.add(contentLength);//添加到集合中
					}
					System.out.println("文件名 =================== " + massage.getName());
					System.out.println("文件字节长度 =================== " + massage.getContentLength());

					//mark = false;//设置为false取读取文件内容
					arg2.add(massage);//将对象添加到集合中，添加后会自动输出到handler中
				} catch (Exception e) {
				e.printStackTrace();
				}
			}else if(mark == 0) {//表示文件夹
				try {
					int data = arg1.readableBytes();//获得所有的可读字节数
					if (data < directoryLength) { //如果可读字节小于头字节大小则满足条件
						System.err.println("文件夹可读字节数件小于头文件字节数大小");
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;//返回
					}
				
					int dataLength = arg1.readInt(); // 读取传送过来的消息的长度。ByteBuf	readInt()方法会让他的ridx增加4
					if (dataLength < 0) { // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
					if (arg1.readableBytes() < dataLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方,相当于回滚
						arg1.resetReaderIndex();//返回以前标记,到时再重新读取.还会与下次的接收值累加
						return;
					}
					//如果接收完则进入这里
					byte[] body = new byte[dataLength];//构建一个等长的byte数组
					arg1.readBytes(body);//读取数据到byte数组中

					Massage message = new Massage();
					message.setFileDirectory(new String(body));//获得文件名
					System.out.println("文件目录 -------------- " + message.getFileDirectory());
					arg2.add(message);//将对象添加到集合中，添加后会自动输出到handler中
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("抛出了异常");
				}
			}else if(mark == 2){
				arg2.add(arg1.readInt());
				System.out.println("文件名发送完成");
				bool = false;
			}
		}else {//如果不为ture 则为流
			long contentLength = list.get(count);
			long surplusLength = (long)contentLength - (long)sum;//总长度  -  已读累加长度 = 剩余长度
			byte[] bytes;
			//readableBytes()得到可读字节数
			if(arg1.readableBytes() > surplusLength){//这里控制了，如果发送的内容流和另一个粘一起了，就只读自己的流，那一部分流留着，然后，等待下一个文件的流发送过来，将剩余的流发送出去
				bytes = new byte[Integer.parseInt(String.valueOf(surplusLength))];//构建一个等长的byte数组
				arg1.readBytes(bytes);//输出到文件
				sum += surplusLength;//接收的总字节数
				if(contentLength == sum){//如果接收完，接收下一个
					count++;
					sum = 0;//清空
				}
			}else{
				bytes = new byte[arg1.readableBytes()];
				arg1.readBytes(bytes);//输出到文件
				sum += bytes.length;//接收的总字节数
				if(contentLength == sum){//如果接收完，接收下一个
					count++;
					sum = 0;//清空
				}
			}
			arg2.add(bytes);//将bytes存入集合中
		}
	}
}
