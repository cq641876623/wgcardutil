package com.cq.cardutil.deivcesdk;

import com.cq.cardutil.base.event.Event;
import com.cq.cardutil.base.event.EventListener;

import com.cq.cardutil.entity.WgEventData;
import com.cq.cardutil.receive.WGPortListener;
import com.cq.cardutil.utils.StringUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/***
 * 门禁卡操作类
 * 
 * @author wsj
 *
 */
public class CardUtil {

	/***
	 * 添加门禁卡号
	 * 
	 * @param ip
	 *            ip地址
	 * @param port
	 *            端口
	 * @param serialNumber
	 *            序列号
	 * @param card
	 *            卡号
	 * @return boolean
	 */
	public static boolean addCard(String ip, int port, String serialNumber, int card) {
		// 定义boolean值
		boolean bool = false;
		DatagramSocket socket = null;
		try {
			// 创建DatagramSocket对象
			socket = new DatagramSocket();
			// 设置超时时间
			socket.setSoTimeout(2000);
			// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			// 通过ip地址和端口连接设备
			packet.setSocketAddress(new InetSocketAddress(ip, port));
			// 向相应的设备发送数据流
			byte[] sendMessage = new byte[64];
			sendMessage[0] = 0x17;
			sendMessage[1] = (byte) 0x50;
			// 序列号流
			byte[] serialNumbers = StringUtil.toLH(Integer.parseInt(serialNumber));
			for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumbers.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = serialNumbers[i];
			}
			// 卡号流
			byte[] cards = StringUtil.toLH(card);
			for (int i = 0, sendMessageSerialNumberStart = 8; i < cards.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = cards[i];
			}

			for (int i = 0, sendMessageSerialNumberStart = 20; i < 4; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = 1;
			}
			sendMessage[12] = 0x20;
			sendMessage[13] = 0x11;
			sendMessage[14] = 0x12;
			sendMessage[15] = 0x31;

			sendMessage[16] = 0x20;
			sendMessage[17] = 0x69;
			sendMessage[18] = 0x12;
			sendMessage[19] = 0x31;

			// 放置指令数据
			packet.setData(sendMessage);
			// 发送指令数据
			socket.send(packet);
			// 返回报文数据
			socket.receive(packet);

			// 判断添加是否成功
			bool = packet.getData()[8] == 0x01 ? true : false;

			// 判断连接是否断开 未断开时 先关闭后断开连接
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bool;

	}

	/***
	 * 删除门禁卡号
	 * 
	 * @param ip
	 *            ip地址
	 * @param port
	 *            端口
	 * @param serialNumber
	 *            序列号
	 * @param card
	 *            卡号
	 * @return boolean
	 */
	public static boolean delCard(String ip, int port, String serialNumber, int card) {
		// 定义boolean值
		boolean bool = false;
		DatagramSocket socket = null;
		try {
			// 创建DatagramSocket对象
			socket = new DatagramSocket();
			// 设置超时时间
			socket.setSoTimeout(2000);
			// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			// 通过ip地址和端口连接设备
			packet.setSocketAddress(new InetSocketAddress(ip, port));
			// 向相应的设备发送数据流
			byte[] sendMessage = new byte[64];
			sendMessage[0] = 0x17;
			sendMessage[1] = (byte) 0x52;
			// 序列号流
			byte[] serialNumbers = StringUtil.toLH(Integer.parseInt(serialNumber));
			for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumbers.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = serialNumbers[i];
			}

			// 卡号流
			byte[] cards = StringUtil.toLH(card);
			for (int i = 0, sendMessageSerialNumberStart = 8; i < cards.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = cards[i];
			}

			// 放置指令数据
			packet.setData(sendMessage);
			// 发送指令数据
			socket.send(packet);
			// 返回报文数据
			socket.receive(packet);
			// 是否成功删除门禁卡
			bool = packet.getData()[8] == 0x01 ? true : false;
			// 判断连接是否断开 未断开时 先关闭后断开连接
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket.disconnect();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bool;

	}

	/***
	 * 检查是否存在卡号
	 * 
	 * @param ip
	 * @param port
	 * @param serialNumber
	 * @param card
	 * @return
	 */
	public static boolean checkCard(String ip, int port, String serialNumber, int card) {
		// 定义boolean值
		boolean bool = false;
		DatagramSocket socket = null;
		try {
			// 创建DatagramSocket对象
			socket = new DatagramSocket();
			// 设置超时时间
			socket.setSoTimeout(2000);
			// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			// 通过ip地址和端口连接设备
			packet.setSocketAddress(new InetSocketAddress(ip, port));
			// 向相应的设备发送数据流
			byte[] sendMessage = new byte[64];
			sendMessage[0] = 0x17;
			sendMessage[1] = (byte) 0x5A;
			// 序列号流
			byte[] serialNumbers = StringUtil.toLH(Integer.parseInt(serialNumber));
			for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumbers.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = serialNumbers[i];
			}
			// 卡号流
			byte[] cards = StringUtil.toLH(card);
			for (int i = 0, sendMessageSerialNumberStart = 8; i < cards.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = cards[i];
			}

			// 放置指令数据
			packet.setData(sendMessage);
			// 发送指令数据
			socket.send(packet);
			// 返回报文数据
			socket.receive(packet);

			// 把返回的报文复制给reponseCard
			byte[] reponseCard = new byte[4];
			System.arraycopy(packet.getData(), 8, reponseCard, 0, 4);
			// 叛断是否含有门禁卡
			bool = !StringUtil.isBytesEmpty(reponseCard);
			// 判断连接是否断开 未断开时 先关闭后断开连接
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
		return bool;

	}

	/***
	 * 是否在使用
	 * 
	 * @param ip
	 *            ip址址
	 * @param port
	 *            端口
	 * @param serialNumber
	 *            产品序列号
	 * @return booealn
	 */
	public static boolean isOnline(String ip, int port, String serialNumber) {
		// 定义boolean值
		boolean bool = false;
		DatagramSocket socket = null;
		try {
			// 创建DatagramSocket对象
			socket = new DatagramSocket();
			// 设置超时时间
			socket.setSoTimeout(2000);
			// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			// 通过ip地址和端口连接设备
			packet.setSocketAddress(new InetSocketAddress(ip, port));
			// 向相应的设备发送数据流
			byte[] sendMessage = new byte[64];
			sendMessage[0] = 0x17;
			sendMessage[1] = (byte) 0x20;
			// 序列号流
			byte[] serialNumbers = StringUtil.toLH(Integer.parseInt(serialNumber));
			for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumbers.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = serialNumbers[i];
			}

			// 放置指令数据
			packet.setData(sendMessage);
			// 发送指令数据
			socket.send(packet);
			// 返回报文数据
			socket.receive(packet);

			// 获取返回报文
			byte[] res = packet.getData();
			byte type = res[12];
			// 判断是否该设备在使用
			bool = type == 1 ? true : false;
			// 判断连接是否断开 未断开时 先关闭后断开连接
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
		return bool;

	}

	/***
	 * 是否远程开门
	 * 
	 * @param ip
	 *            ip址址
	 * @param port
	 *            端口
	 * @param serialNumber
	 *            产品序列号
	 * @param postion
	 *            几号门 （1-4）最大是4号门     
	 * @return booealn
	 */
	public static boolean openDoor(String ip, int port, String serialNumber, byte postion) {
		// 定义boolean值
		boolean bool = false;
		DatagramSocket socket = null;
		try {
			// 创建DatagramSocket对象
			socket = new DatagramSocket();
			// 设置超时时间
			socket.setSoTimeout(2000);
			// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			// 通过ip地址和端口连接设备
			packet.setSocketAddress(new InetSocketAddress(ip, port));
			// 向相应的设备发送数据流
			byte[] sendMessage = new byte[64];
			sendMessage[0] = 0x17;
			sendMessage[1] = (byte) 0x40;
			// 序列号流
			byte[] serialNumbers = StringUtil.toLH(Integer.parseInt(serialNumber));
			for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumbers.length; i++) {
				sendMessage[sendMessageSerialNumberStart + i] = serialNumbers[i];
			}

			// 第8位字节代表位置
			sendMessage[8] = postion;

			// 放置指令数据
			packet.setData(sendMessage);
			// 发送指令数据
			socket.send(packet);
			// 返回报文数据
			socket.receive(packet);

			// 获取返回报文
			byte[] res = packet.getData();
			byte type = res[8];
			// 判断是否该设备在使用
			bool = type == 1 ? true : false;
			// 判断连接是否断开 未断开时 先关闭后断开连接
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
		return bool;

	}


	/**
	 * 设置接收端口
	 * @param deviceIp
	 * @param devicePort
	 * @param deviceSerialNumber
	 * @param ip
	 * @param port
	 * @return
	 * @throws IOException
	 */
	public static boolean setReceivePort(String deviceIp,int devicePort, String deviceSerialNumber,String ip, int port)throws IOException {
		DatagramSocket udp = new DatagramSocket();
		udp.setSoTimeout(2000);
		DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
		packet.setSocketAddress(
				new InetSocketAddress(deviceIp, devicePort));
		byte[] sendMessage = new byte[64];
		sendMessage[0] = 0x17;
		sendMessage[1] = (byte) 0x90;
		byte[] serialNumber = StringUtil.toLH(Integer.parseInt(deviceSerialNumber));
		for (int i = 0, sendMessageSerialNumberStart = 4; i < serialNumber.length; i++) {
			sendMessage[sendMessageSerialNumberStart + i] = serialNumber[i];
		}
		String[] ipStrings = ip.split("\\.");
		byte[] ips = new byte[4];
		for (int i = 0; i < ipStrings.length; i++) {
			ips[i] = (byte) Integer.parseInt(ipStrings[i]);
		}
		for (int i = 0, sendMessageIpStart = 8; i < ips.length; i++) {
			sendMessage[sendMessageIpStart + i] = ips[i];
		}
		byte[] portBytes = StringUtil.toLH(port, 2);
		for (int i = 0, sendMessageIpStart = 12; i < portBytes.length; i++) {
			sendMessage[sendMessageIpStart + i] = portBytes[i];
		}

		packet.setData(sendMessage);
		udp.send(packet);
		udp.receive(packet);

		if (udp != null && !udp.isClosed()) {
			udp.close();
			udp.disconnect();
		}

		return packet.getData()[8] == 0x01 ? true : false;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		String ip = "192.168.80.125";
		int port = 60000;
		String serialNumber = "233118416";
		int card = 1000100056;
		byte b= 1;
//		System.out.println(isOnline(ip, port, serialNumber));
//		System.out.println(addCard(ip, port, serialNumber, card));
//		System.out.println(checkCard(ip, port, serialNumber, card));
//		System.out.println(delCard(ip, port, serialNumber, card));
//		System.out.println(checkCard(ip, port, serialNumber, card));
//		System.out.println(openDoor(ip, port, serialNumber,b));
//		System.out.println(setReceivePort(ip, port, serialNumber,"192.168.80.120",60000));


//		WGPortListener wgPortListener=new WGPortListener();
		WGPortListener wgPortListener=WGPortListener.getWGPortListener();
//		wgPortListener.close();
//
		wgPortListener.addListener(new EventListener<WgEventData>() {

			@Override
			public void handle(Event<WgEventData> event) {
				System.out.println("hajskhdkgsajdgagdasfdhgfasasfdfasfdfashfdasjd"+event.getData());
			}
		});
//
//
		Thread.sleep(8*10*1000);
	}
}
