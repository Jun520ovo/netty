package com.nettyFile.manyFile;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 传文件   小文件 一次性传
 */
public class Client {
	EventLoopGroup group = new NioEventLoopGroup();
	private static String filePath ="D:\\绝密\\spring";//发送的文件夹
	//private static String filePath ="F:\\zip";//发送的文件夹
	public static void main(String[] args) throws Exception {
		new Client().connect(1111, "127.0.0.1");
	}
 
	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//设置buffer缓冲区大小
					.handler(new ClientInternal());
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public class ClientInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//用来换行的解码器
			 arg0.pipeline().addLast(new Encoder());//转为字符串编码
			// arg0.pipeline().addLast(new Decord());//转为字符串编码
			// arg0.pipeline().addLast(new StringEncoder());//转为字符串编码
			 arg0.pipeline().addLast(new ByteArrayEncoder());//发了个byte去服务端  然后服务端收到之后看是不是第一次来    是就先创建文件  然后 在返回个消息给客户端  客户端在把文件内容传给服务端  服务端在写服务内容
			 arg0.pipeline().addLast(new ChunkedWriteHandler());//这里是客户端文件内容转为这个类型  服务端就鞥直接接到
			 arg0.pipeline().addLast(new ClientHandler(filePath));
		}
	}
}
