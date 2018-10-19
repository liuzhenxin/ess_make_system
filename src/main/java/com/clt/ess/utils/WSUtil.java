package com.clt.ess.utils;

import com.clt.ws.HelloWebservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;



public class WSUtil {

//	private static String URLSTR = "http://192.144.176.134:8082/SealCenter/ws/mycxf?wsdl";
	private static String URLSTR = "http://192.168.1.113:8888/ESSSafeLoginServer/wsForServer?wsdl";
	
	
	/*
	 * 调用ws,根据人员id获取印章数据
	 */
	public static String getSealDataAbtSigner(String essPid,String essPara){
		
		String result = null;
		
		URL wsdlUrl;
		try {
			wsdlUrl = new URL(URLSTR);
		
			//2 xml描述详细内容
			// 参数1：xml 命名空间，从根标签targetNamespace获得
			// 参数2：具体的服务名称，从xml最后获得							
			QName qName = new QName("http://ws.clt.com/","HelloWebserviceService");
	
			//3 根据上面数据，创建远程调用服务
			Service service = Service.create(wsdlUrl, qName);
	
			//4 获得具体服务对应“客户端”java代码
			HelloWebservice s = service.getPort(HelloWebservice.class);
	
			result = s.doSomething("AAAAAAAAAAAAAAAAAAA");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 调用ws,根据token获取personId
	 */
	public static String getLoginUserInfo(String token) throws MalformedURLException {
		
		String result = null;
		URL wsdlUrl;
		wsdlUrl = new URL(URLSTR);
		//2 xml描述详细内容
		// 参数1：xml 命名空间，从根标签targetNamespace获得
		// 参数2：具体的服务名称，从xml最后获得
		QName qName = new QName("http://ws.clt.com/","HelloWebserviceService");

		//3 根据上面数据，创建远程调用服务
		Service service = Service.create(wsdlUrl, qName);

		//4 获得具体服务对应“客户端”java代码
		HelloWebservice s = service.getPort(HelloWebservice.class);

		result = s.getLoginUserInfo(token, null);

		return result;
		
	}

}
