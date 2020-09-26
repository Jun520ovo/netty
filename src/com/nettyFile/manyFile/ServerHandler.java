package com.nettyFile.manyFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler  extends ChannelInboundHandlerAdapter{
	private FileOutputStream fos;//输出流对象
	private List<Massage> list = new ArrayList<>();
	private static String prefix = "C:\\Users\\Administrator\\Desktop\\Socketserver\\";//存入的文件目录
	private Massage massage;
	private static int minCount = 0;//集合的遍历的下标总数
	private int maxCount;//集合的遍历的下标总数
	private long count;//获取总字节数
	private int copy = -1;
	private FileOutputStream fileOutputStream;

	public static String OK = "ok";
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Massage) {//是否为message类型
			massage = (Massage) msg;
			if(massage.getFileDirectory() == null) {
				File file = new File(prefix + massage.getName());//构建一个file对象
				if (!file.exists()) {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();//创建文件夹
					}
					file.createNewFile();//创建文件
				}
				massage.setFile(file);
				if(massage.getContentLength() != 0){
					list.add(massage);//添加进集合
				}
			}else{//表示是文件目录
				File file = new File(prefix + massage.getFileDirectory());
				if (!file.exists()) {//如果不存在就创建
					file.mkdirs();//创建文件夹
				}
			}
			return;
		}else if(msg instanceof Integer){
			ctx.writeAndFlush(OK.getBytes());//write并flush,返回ok到客户端，客户端接收到值持续发送文件内容的流
			maxCount = list.size()-1;//记录最大下标数
		}else if(msg instanceof byte[]){//文件内容
			Massage message = list.get(minCount);
			File file = message.getFile();
			//System.out.println(file);
			if(copy != minCount){//第一次不相等，然后如果不变就不进入
				fileOutputStream = new FileOutputStream(file);//这里是指向一个新的内存地址，而不是覆盖掉原来的地址值，所以每次都要关闭。
				System.out.println(fileOutputStream.hashCode() + "hashcode=============");
			}
			copy = minCount;
			try {
				byte[] bytes = (byte[]) msg;//如果是发送的是byte数组就转成byte数组
				fileOutputStream.write(bytes,0,bytes.length);//输出到文件
				long contentLength = message.getContentLength();
				count += bytes.length;//接收的总字节数
				fileOutputStream.flush();
				if(contentLength == count){//如果接收完，接收下一个
					if(minCount == maxCount){//最后一次执行的方法
						Decoder.count = 0;//重置为0
						Decoder.bool = true;//重新接收头
						Decoder.list.clear();
						list.clear();
						copy = -1;
						minCount = 0;
					}else{
						minCount++;
						count = 0;
					}
					fileOutputStream.close();//文件传输完毕后，关闭流
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
