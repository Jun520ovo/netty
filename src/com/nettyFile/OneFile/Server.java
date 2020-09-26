package com.nettyFile.OneFile;

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
 * 传文件 小文件 一次性传
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(1111);
	}

	/**
	 * 总结 客户端传数据服务端 一条数据直接转是没有任何问题的 多条数据就有问题 问题为 客户端传了一百次 但是服务端就当做一次到两次全部接完了 这就是粘包问题
	 * 应该客户端发多少次 服务端接多少次 要是服务端想反回数据给客户端 服务端返回多少次 客户端也得接多少次 解决方法 自定义类型编码解码器 客户端传数据的时候
	 * 使用EnCoder 讲数据传送给服务端 服务端使用DeCoder接住 （其中客户端传的数据如果是字符串 就必须在Encoder里面用字符串去接
	 * 不然接不到） 服务端接也就接传过来那一个类型。必须保持一致 反向的 服务端接到数据 发送给客户端的时候
	 * 服务端使用Encoder自定义编码将数据传给客户端（服务端里面的数据是什么类型的自定义编码器里面也就是什么类型 不然也接不到）
	 * 客户端接也就接传过来那一个类型。必须保持一致 最后 服务端打印的时候 打印的也就是传过来的类型 不能乱打印 反之客户端也是一样 服务端将什么类型传给客户端
	 * 客户端也就接到什么类型 不能乱接 否则就会报错 进入异常哪个方法 从而关闭
	 * 
	 * @param port
	 * @throws Exception
	 */

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 异步线程池
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 异步线程池

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // 添加工作线程组
					.channel(NioServerSocketChannel.class) // 设置管道模式
					.option(ChannelOption.SO_BACKLOG, 1024) // 配置BLOCK大小
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
			// arg0.pipeline().addLast(new StringDecoder());//字符解码
			 arg0.pipeline().addLast(new ByteArrayEncoder());
			 arg0.pipeline().addLast(new StringEncoder());
			 arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
