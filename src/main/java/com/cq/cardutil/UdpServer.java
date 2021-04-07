package com.cq.cardutil;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 最大接收数据量是2KB，多余数据库会被丢弃
 * @author chenqi
 * @date 2021/1/20 16:01
 */
public class UdpServer {
    public static void main(String[] args) throws IOException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String portString=scanner.nextLine();
            final int port=Integer.parseInt(portString);
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

            System.out.println(portString+" 于 "+ft.format(new Date())+" start");
            cachedThreadPool.submit(  new Runnable() {
                @Override
                public void run() {
                    try{
                        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

                        System.out.println(port+" 于 "+ft.format(new Date())+" end");
                        DatagramChannel server= DatagramChannel.open();
                        server.socket().bind(new InetSocketAddress(port));
                        server.configureBlocking(false);
                        Selector selector= Selector.open();
                        server.register(selector, SelectionKey.OP_READ);
                        while (selector.select()>0){
                            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                            while(it.hasNext()) {
                                SelectionKey key = it.next();
                                it.remove();
                                if(!key.isValid()){
                                    key.cancel();
                                    key.channel().close();

                                }


                                if(key.isReadable()){
                                    try {
                                        DatagramChannel channel = (DatagramChannel)key.channel();
                                        ByteBuffer buf=ByteBuffer.allocate(1024*2);
                                        buf.clear();
                                        InetSocketAddress address = (InetSocketAddress) channel.receive(buf);

                                        buf.flip();
                                        byte[] res = new byte[buf.limit()];
                                        buf.get(res);
                                        System.out.println("接收来自\t"+address.toString()+"\t:"+new String(res));


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
            });



        }
    }
}
