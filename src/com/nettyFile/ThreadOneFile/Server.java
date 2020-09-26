package com.nettyFile.ThreadOneFile;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 传文件 多个文件
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(8889);
	}

	

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 异步线程池
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 异步线程池

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // 添加工作线程组
					.channel(NioServerSocketChannel.class) // 设置管道模式
					.option(ChannelOption.SO_BACKLOG, 1024) // 配置BLOCK大小
					.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//设置buffer缓冲区大小
					.childHandler(new ServerInternal());// 我们自己处理的类

			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public class ServerInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {// 這裡是寫編碼器 解碼器的
			
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//用来换行的解码器
			// arg0.pipeline().addLast(new Decord());//字符解码
			// arg0.pipeline().addLast(new Encoder());//字符解码
			 arg0.pipeline().addLast(new ByteArrayEncoder());//这是不需要解码的  发过去就是个流
			 arg0.pipeline().addLast(new Decoder());
			 arg0.pipeline().addLast(new StringEncoder());//字符解码
			 arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
