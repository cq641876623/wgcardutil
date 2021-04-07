package com.cq.cardutil.receive;

import com.cq.cardutil.base.event.Event;
import com.cq.cardutil.base.event.EventListener;
import com.cq.cardutil.base.wgListener.BaseWgListener;
import com.cq.cardutil.entity.WgEventData;
import com.cq.cardutil.event.WgEvent;
import com.cq.cardutil.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Chenqi
 * @Description:
 * @Date: create in 2021/4/7 15:14
 */
@Slf4j
public class WGPortListener_V2 implements BaseWgListener {

    private DatagramChannel datagramChannel;
    private ByteBuffer buf;
    private Selector selector;
    private List<EventListener> listeners;
    public static int WEIGENG_LINSTEN_PORT=60000;
    public static int WEIGENG_LINSTEN_BUFFER_SIZE=64;




    public WGPortListener_V2(Integer port,Integer bufSize, List<EventListener> listeners) throws IOException {
        Optional<Integer> portOptional=Optional.ofNullable(port);
        port=portOptional.orElse(WEIGENG_LINSTEN_PORT);
        this.datagramChannel  = DatagramChannel.open();
        this.datagramChannel.bind(new InetSocketAddress(port));
        Optional<Integer> bufSizeOptional=Optional.ofNullable(bufSize);
        bufSize=bufSizeOptional.orElse(WEIGENG_LINSTEN_BUFFER_SIZE);
        this.buf=ByteBuffer.allocate(bufSize);
        Optional<List<EventListener>> listenersOptional=Optional.of(listeners);
        this.listeners=listenersOptional.orElse(new ArrayList<EventListener>());
    }


    public WGPortListener_V2() throws IOException {
        this(WEIGENG_LINSTEN_PORT,WEIGENG_LINSTEN_BUFFER_SIZE,new ArrayList<EventListener>());
    }

    public WGPortListener_V2(Integer port) throws IOException {
        this(port,WEIGENG_LINSTEN_BUFFER_SIZE,new ArrayList<EventListener>());
    }
    public WGPortListener_V2(Integer port,Integer bufSize) throws IOException {
        this(port,bufSize,new ArrayList<EventListener>());
    }
    public WGPortListener_V2(Integer port, List<EventListener> listeners) throws IOException {
        this(port,WEIGENG_LINSTEN_BUFFER_SIZE,listeners);
    }



    @Override
    public void close() {
        try {
            this.selector.close();
            this.datagramChannel.close();
        } catch (IOException e) {
           log.error("微耕监听服务关闭失败 端口号：{} 错误信息：{}",this.datagramChannel.socket().getLocalPort(), e.getMessage());
        }
    }

    @Override
    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void notifyListener(Event event) {
        for (EventListener listener : listeners) {
            listener.handle(event);
        }
    }

    @Override
    public void removeListener(int i) {
        this.listeners.remove(i);
    }

    @Override
    public void run() {
        try {
            this.datagramChannel.configureBlocking(false);
            this.selector = Selector.open();
            log.info("微耕事件监听器启动 端口：{}",this.datagramChannel.socket().getLocalPort());
            this.datagramChannel.register(selector, SelectionKey.OP_READ);
            while (selector.select() > 0) {
//						拿到迭代器
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    try {
//								如果是读事件
                        if (selectionKey.isReadable()) {
//                              拿到channel
                            DatagramChannel dc = (DatagramChannel) selectionKey.channel();
                            //将标记指向1
                            buf.clear();
                            //将数据存入buf
                            InetSocketAddress address = (InetSocketAddress) dc.receive(buf);

                            //标记指向1并limit指向buf最后存储数据的最后一位
                            buf.flip();
                            byte[] res = new byte[64];
                            if (buf.remaining() != 64)
                                continue;
                            buf.get(res, 0, 64);
                            byte[] serialNumberByte = new byte[4];
                            System.arraycopy(res, 4, serialNumberByte, 0, 4);
                            String serialNumber = String.valueOf(StringUtil.bytes2Int(serialNumberByte));
                            byte func = res[1];
                            if (func == 0x20) {
                                byte type = res[12];


                                switch (type) {
                                    case 0x00:
//								deviceService.setOnline(deviceSdkDto);
                                        break;
                                    case 0x01:

                                        byte isSucc = res[13];
                                        byte[] cardNumByte = new byte[4];
                                        System.arraycopy(res, 16, cardNumByte, 0, 4);
                                        String cardNum = String.valueOf(StringUtil.bytes2Int(cardNumByte));
                                        int doorNum = res[14];
                                        int inOrOut = (isSucc==0x01? res[15]: -1);

                                        WgEventData wgEventData=new WgEventData(cardNum,inOrOut,address.getAddress().getHostAddress(),serialNumber,doorNum);
                                        WgEvent wgEvent=new WgEvent(wgEventData,this,"微耕udp监听服务收到来自设备发送的消息！");

                                        this.notifyListener(wgEvent);

                                        break;
                                }

                            }
                        }
                    } catch (Exception e) {
                        log.error("微耕门禁监听服务出错 端口号：{} 错误信息：{}",this.datagramChannel.socket().getLocalPort(), e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error("微耕门禁监听服务出错 端口号：{} 错误信息：{}",this.datagramChannel.socket().getLocalPort(), e.getMessage());
        }

    }


    public static void main(String[] args) throws IOException {
        WGPortListener_V2 wgPortListener_v2=new WGPortListener_V2();
        wgPortListener_v2.addListener(new EventListener<WgEventData>() {
            @Override
            public void handle(Event<WgEventData> event) {
                log.info("微耕监听服务事件监听：{} ,进出信息：{}",event.getMessage(),event.getData().getInOrOut());
            }
        });
        new Thread(wgPortListener_v2).start();


    }
}
