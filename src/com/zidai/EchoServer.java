package com.zidai;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
public class EchoServer {  
    private static final int port = 8080;  
    
    public void start() throws InterruptedException {  
    	
        ServerBootstrap b = new ServerBootstrap();// ������������  
        EventLoopGroup group = new NioEventLoopGroup();// ͨ��nio��ʽ���������Ӻʹ�������  
        
        try {  
            b.group(group);  
            b.channel(NioServerSocketChannel.class);// ����nio���͵�channel  
            b.localAddress(new InetSocketAddress(port));// ���ü����˿�  
            b.childHandler(new ChannelInitializer<SocketChannel>() {//�����ӵ���ʱ�ᴴ��һ��channel  
                        protected void initChannel(SocketChannel ch) throws Exception {  
                        	ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,2, 0,2));    
                        	//ch.pipeline().addLast(new LengthFieldPrepender(2));    
                        	ch.pipeline().addLast(new StringEncoder()); 
                        	ch.pipeline().addLast(new StringDecoder()); 
                            ch.pipeline().addLast(new EchoServerHandler());  
                        }  
                    });  
            ChannelFuture f = b.bind().sync();// ������ɣ���ʼ��server��ͨ������syncͬ����������ֱ���󶨳ɹ�  
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());  
            f.channel().closeFuture().sync();// Ӧ�ó����һֱ�ȴ���ֱ��channel�ر�  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            group.shutdownGracefully().sync();//�ر�EventLoopGroup���ͷŵ�������Դ�����������߳�  
        }  
    }  
    public static void main(String[] args) {  
        try {  
            new EchoServer().start();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}  