package com.cq.cardutil;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author chenqi
 * @date 2021/1/20 16:01
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String portString=scanner.nextLine();
            final int port=Integer.parseInt(portString);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        ServerSocketChannel server= ServerSocketChannel.open();
                        server.socket().bind(new InetSocketAddress(port));
                        server.configureBlocking(false);
                        Selector selector= Selector.open();
                        server.register(selector, SelectionKey.OP_ACCEPT);
                        while (selector.select()>0){
                            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                            while(it.hasNext()) {
                                SelectionKey key = it.next();
                                it.remove();
                                if(!key.isValid()){
                                    key.cancel();
                                    key.channel().close();

                                }
                                if(key.isAcceptable()) {
                                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                                    SocketChannel clientChannel = ssc.accept();
                                    clientChannel.socket().setSoTimeout(3000);
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                    System.out.println("a new client connected "+clientChannel.getRemoteAddress());
                                }




                                if(key.isReadable()){
                                    try {
                                        SocketChannel socketChannel = (SocketChannel)key.channel();
                                        ByteBuffer buf=ByteBuffer.allocate(1024);
                                        buf.clear();
                                        int len2=0;
                                        StringBuilder stringBuilder=new StringBuilder();
                                        while((len2=socketChannel.read(buf))>0){
                                            buf.flip();
                                            byte[] bytes=new byte[buf.limit()];
                                            buf.get(bytes);
                                            stringBuilder.append(new String(bytes));
                                            buf.clear();
                                        }
                                        if(stringBuilder.length()>0){
                                            System.out.println(stringBuilder.toString());
                                        }
                                        if(len2==-1){
                                            key.cancel();
                                            key.channel().close();
                                        }


                                    }catch (Exception e){
                                        e.printStackTrace();
                                        key.cancel();
                                        key.channel().close();
                                    }
                                }

                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();



        }
    }
}
