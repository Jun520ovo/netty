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
 * ���ļ�   С�ļ� һ���Դ�
 */
public class Client {
	EventLoopGroup group = new NioEventLoopGroup();
	private static String filePath ="D:\\����\\spring";//���͵��ļ���
	//private static String filePath ="F:\\zip";//���͵��ļ���
	public static void main(String[] args) throws Exception {
		new Client().connect(1111, "127.0.0.1");
	}
 
	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//����buffer��������С
					.handler(new ClientInternal());
			ChannelFuture f = b.connect(host, port).sync();
			// �ȴ��ͻ�����·�ر�
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public class ClientInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//�������еĽ�����
			 arg0.pipeline().addLast(new Encoder());//תΪ�ַ�������
			// arg0.pipeline().addLast(new Decord());//תΪ�ַ�������
			// arg0.pipeline().addLast(new StringEncoder());//תΪ�ַ�������
			 arg0.pipeline().addLast(new ByteArrayEncoder());//���˸�byteȥ�����  Ȼ�������յ�֮���ǲ��ǵ�һ����    �Ǿ��ȴ����ļ�  Ȼ�� �ڷ��ظ���Ϣ���ͻ���  �ͻ����ڰ��ļ����ݴ��������  �������д��������
			 arg0.pipeline().addLast(new ChunkedWriteHandler());//�����ǿͻ����ļ�����תΪ�������  ����˾��Eֱ�ӽӵ�
			 arg0.pipeline().addLast(new ClientHandler(filePath));
		}
	}
}
