package com.will.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class IPCServer {
	
	private Selector selector = null;
	
	private Charset charset = Charset.forName("UTF-8");
	
	public void init() throws IOException
	{
		selector = Selector.open();
		ServerSocketChannel server = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1",30000);
		server.socket().bind(isa);
		server.configureBlocking(false);
		server.register(selector, SelectionKey.OP_ACCEPT);
		while(selector.select() > 0){
			for(SelectionKey sk : selector.selectedKeys()){
				//remove from key set
				selector.selectedKeys().remove(sk);
				if(sk.isAcceptable()){
					SocketChannel sc = server.accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
					sk.interestOps(SelectionKey.OP_ACCEPT);
				}
				
				//channel has data to read
				if(sk.isReadable()){
					SocketChannel sc = (SocketChannel) sk.channel();
					
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					String content = "";
					try{
						while(sc.read(buffer) > 0){
							buffer.flip();
							content += charset.decode(buffer);
						}
						System.out.println("server=====" + content);
						String[] strs = content.split("\r\n");
						for(int i = 0; i < strs.length;i++){
							System.out.println("server parse is "+strs[i]);
						}
						sk.interestOps(SelectionKey.OP_READ);
					}catch(IOException ex){
						sk.cancel();
						if(sk.channel() != null){
							sk.channel().close();
						}
					}
					if(content.length() > 0){
						for(SelectionKey key : selector.keys()){
							Channel targetChannel = key.channel();
							if(targetChannel instanceof SocketChannel){
								SocketChannel dest = (SocketChannel) targetChannel;
								dest.write(charset.encode(content));
							}
						}
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		new IPCServer().init();
	}

}
