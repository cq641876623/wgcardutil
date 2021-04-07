package com.cq.cardutil.receive;


import com.cq.cardutil.base.event.Event;
import com.cq.cardutil.base.event.EventListener;
import com.cq.cardutil.base.event.EventSource;
import com.cq.cardutil.entity.WgEventData;
import com.cq.cardutil.event.WgEvent;
import com.cq.cardutil.utils.StringUtil;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author chenqi
 * @date 2020/6/24 16:11
 */
public class WGPortListener implements EventSource {

	private DatagramChannel datagramChannel;
	private ByteBuffer buf;
	private Selector selector;
	private List<EventListener> listeners = new ArrayList<EventListener>();



	public static int WEIGENG_LINSTEN_PORT=60000;

	private volatile static WGPortListener wgPortListener;

	private Thread wgThread;


	public static WGPortListener getWGPortListener() {
		if (wgPortListener == null) {
			synchronized (WGPortListener.class) {
				if(wgPortListener == null){
					wgPortListener=new WGPortListener();
				}
			}
		}
		return wgPortListener;
	}


	//  Channel 的实现
	//      FileChannel  从文件中读写数据
	//  DatagramChannel  能通过UDP 读写网络中的数据
	//    SocketChannel  能通过TCP 读写网络中的数据
//	ServerSocketChannel  可以监听新进来的TCP连接，像web服务器那样，
	//                   对每一个新进来的连接都会创建一个SocketChannel.



	private Thread startListener() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
                    datagramChannel  = DatagramChannel.open();
                    //端口复用
                    datagramChannel.socket().setReuseAddress(true);
                    //绑定监听的端口
                    datagramChannel.bind(new InetSocketAddress(WEIGENG_LINSTEN_PORT));
                    //创建buf空间 运输数据的载体
                    buf = ByteBuffer.allocate(64);
                    //设置为channel非阻塞
                    datagramChannel.configureBlocking(false);
                    selector = Selector.open();
                    //将datagramChannel注册到选择器中
                    datagramChannel.register(selector, SelectionKey.OP_READ);
                    //当选择器中有channel
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

												if (isSucc == 0x01) {
													byte[] cardNumByte = new byte[4];
													System.arraycopy(res, 16, cardNumByte, 0, 4);
													String cardNum = String.valueOf(StringUtil.bytes2Int(cardNumByte));
													int doorNum = res[14];
													int inOrOut = res[15];
//									回调函数
													//产生事件 并交由函数启动时 注入的监听器 去处理
                                                    WgEventData wgEventData=new WgEventData(cardNum,inOrOut,address.getAddress().getHostAddress(),serialNumber,doorNum);
                                                    WgEvent wgEvent=new WgEvent(wgEventData,getWGPortListener(),"微耕udp监听服务收到来自设备发送的消息！");
                                                    getWGPortListener().notifyListener(wgEvent);

												} else {
													// 刷卡未通过
													byte[] cardNumByte = new byte[4];
													System.arraycopy(res, 16, cardNumByte, 0, 4);
													String cardNum = String.valueOf(StringUtil.bytes2Int(cardNumByte));

													System.out.println("刷卡未通过" + cardNum);
												}

												break;
										}

									}
								}
							} catch (Exception e) {
								System.out.println("wg门禁"+ e.getMessage());
							}
						}
					}
				} catch (IOException e) {
					System.out.println("udp服务监听错误{}"+  e.getMessage());
				}
			}
		});
		thread.setName("WG实时监听线程");
		thread.start();
		return thread;
	}

//	public static WGPortListener getWGPortListener() {
//		return wgPortListener;
//	}

	private WGPortListener()  {
	    super();
        this.wgThread=startListener();
	}

//	public WGPortListener()  {
//		super();
//		this.wgThread=startListener();
//		wgPortListener=this;
//	}
	/**
	 * 关闭监听
	 */
	public void close(){
	    this.wgThread.interrupt();
    }

	/**
	 * 添加监听
	 * @param listener 监听器
	 */
	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public EventListener delListener(int i) {
		return listeners.remove(i);
	}


	/**
	 * 通知监听
	 * @param event
	 */
	public void notifyListener(Event event) {
		for (EventListener listener : listeners) {
			listener.handle(event);
		}
	}

	@Override
	public void removeListener(int i) {

	}
}
