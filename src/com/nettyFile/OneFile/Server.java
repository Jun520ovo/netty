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
 * ���ļ� С�ļ� һ���Դ�
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(1111);
	}

	/**
	 * �ܽ� �ͻ��˴����ݷ���� һ������ֱ��ת��û���κ������ �������ݾ������� ����Ϊ �ͻ��˴���һ�ٴ� ���Ƿ���˾͵���һ�ε�����ȫ�������� �����ճ������
	 * Ӧ�ÿͻ��˷����ٴ� ����˽Ӷ��ٴ� Ҫ�Ƿ�����뷴�����ݸ��ͻ��� ����˷��ض��ٴ� �ͻ���Ҳ�ýӶ��ٴ� ������� �Զ������ͱ�������� �ͻ��˴����ݵ�ʱ��
	 * ʹ��EnCoder �����ݴ��͸������ �����ʹ��DeCoder��ס �����пͻ��˴�������������ַ��� �ͱ�����Encoder�������ַ���ȥ��
	 * ��Ȼ�Ӳ����� ����˽�Ҳ�ͽӴ�������һ�����͡����뱣��һ�� ����� ����˽ӵ����� ���͸��ͻ��˵�ʱ��
	 * �����ʹ��Encoder�Զ�����뽫���ݴ����ͻ��ˣ�����������������ʲô���͵��Զ������������Ҳ����ʲô���� ��ȻҲ�Ӳ�����
	 * �ͻ��˽�Ҳ�ͽӴ�������һ�����͡����뱣��һ�� ��� ����˴�ӡ��ʱ�� ��ӡ��Ҳ���Ǵ����������� �����Ҵ�ӡ ��֮�ͻ���Ҳ��һ�� ����˽�ʲô���ʹ����ͻ���
	 * �ͻ���Ҳ�ͽӵ�ʲô���� �����ҽ� ����ͻᱨ�� �����쳣�ĸ����� �Ӷ��ر�
	 * 
	 * @param port
	 * @throws Exception
	 */

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// �첽�̳߳�
		EventLoopGroup workerGroup = new NioEventLoopGroup();// �첽�̳߳�

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // ��ӹ����߳���
					.channel(NioServerSocketChannel.class) // ���ùܵ�ģʽ
					.option(ChannelOption.SO_BACKLOG, 1024) // ����BLOCK��С
					.childHandler(new ServerInternal());// �����Լ��������

			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public class ServerInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {// �@�e�ǌ����a�� ��a����
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//�������еĽ�����
			// arg0.pipeline().addLast(new Decord());//�ַ�����
			// arg0.pipeline().addLast(new Encoder());//�ַ�����
			// arg0.pipeline().addLast(new StringDecoder());//�ַ�����
			 arg0.pipeline().addLast(new ByteArrayEncoder());
			 arg0.pipeline().addLast(new StringEncoder());
			 arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
