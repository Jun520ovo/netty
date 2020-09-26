package com.nettyFile.ThreadOneFile;


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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;


public class Client extends Thread{
	//EventLoopGroup group = new NioEventLoopGroup();
	private String filePath;//���͵��ļ���
	private int sign;
	//private static String filePath ="F:\\zip";//���͵��ļ���
	public Client(String filePath,int sign) {
		this.filePath = filePath;
		this.sign =sign;
	}
	@Override
	public void run() {
		try {
			EventLoopGroup group = new NioEventLoopGroup();
			try {
				Bootstrap b = new Bootstrap();
				b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
	            .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//����buffer��������С
						.handler(new ClientInternal());
				ChannelFuture f = b.connect("127.0.0.1",8889).sync();
				// �ȴ��ͻ�����·�ر�
				f.channel().closeFuture().sync();
			} finally {
				group.shutdownGracefully();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public class ClientInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//�������еĽ�����
			 arg0.pipeline().addLast(new Encoder());//תΪ�ַ�������
			 arg0.pipeline().addLast(new StringDecoder());//תΪ�ַ�������
			 arg0.pipeline().addLast(new ByteArrayEncoder());//���˸�byteȥ�����  Ȼ�������յ�֮���ǲ��ǵ�һ����    �Ǿ��ȴ����ļ�  Ȼ�� �ڷ��ظ���Ϣ���ͻ���  �ͻ����ڰ��ļ����ݴ��������  �������д��������
			 arg0.pipeline().addLast(new ChunkedWriteHandler());//�����ǿͻ����ļ�����תΪ�������  ����˾��Eֱ�ӽӵ�
			 arg0.pipeline().addLast(new ClientHandler(filePath,sign));
		}
	}
}
