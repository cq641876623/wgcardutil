调用方式：项目启动时
```
		WGPortListener wgPortListener=WGPortListener.getWGPortListener();
        添加实现方法
		wgPortListener.addListener(new EventListener<WgEventData>() {

			@Override
			public void handle(Event<WgEventData> event) {
				System.out.println("hajskhdkgsajdgagdasfdhgfasasfdfasfdfashfdasjd"+event.getData());
			}
		});
```

后台管理添加设备时
```java

CardUtil.setReceivePort(ip, port, serialNumber,"本机ip地址",WGPortListener.WEIGENG_LINSTEN_PORT)
//WGPortListener.WEIGENG_LINSTEN_PORT（本机监听端口）
```