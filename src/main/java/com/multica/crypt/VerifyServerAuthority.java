package com.multica.crypt;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class VerifyServerAuthority {
	
	final static private String CLTSALT = "BJCLTSALTBJCLTSALTBJCLTS";
	
	public static int	GetServerAuthorityVerifyStatus(
				String sClientDate,			//客户端时间（格式：201812131430）
				String sVerifyString			//上表中，“验证结果”字段的内容
			)
	{
		
		// 检查客户端时间与服务器时间是否超过5分钟
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
		
		Date serverDate = new Date();
		
		long lServerTime = serverDate.getTime();
		
		try {
			
			long lClientTime = df.parse(sClientDate).getTime();
			int minutes = (int) (Math.abs(lServerTime - lClientTime)/(1000 * 60));
			if(minutes > 5)
				return 1;
			
		} catch (ParseException e) {
			
			e.printStackTrace();
			return 3;
			
		}
		
		//根据当前时间，计算验证码
		df = new SimpleDateFormat("yyyyMMdd");
		
		String sTime = df.format(serverDate);
		
		sTime = sTime + CLTSALT;
		
		try {
			
			byte[] bHash = MuticaCrypt.ESSGetDigest(sTime.getBytes());
			String sHash = MuticaCrypt.ESSGetBase64Encode(bHash);
			
			if(sVerifyString == null){
				
				sVerifyString = "";
				
			}
			
			if(sHash.equals(sVerifyString)  == false)
				return 2;
			else
				return 0;
			
		} catch (MuticaCryptException e) {
			
			e.printStackTrace();
			
			return 3;
			
		}		
	}
	
	
	public static String VerifyServerAuth(
			String	sOwnerName,				//保存在数据库中的客户单位名称
			String	sServerIP,					//保存在数据库中的服务器IP地址
			int		iSealMaxCount,			//保存在数据库中的UK 公章、UK手签、非UK公章、非UK手签总授权数量的和
			String	sAuthorizeCode,			//保存在数据库中的授权产品编码
			String	sAuthorizeEndDate,		//保存在数据库中的授权到期时间
			String	sSignVal					//签名值
			)	{
		
		String sData = sOwnerName + sServerIP + Integer.toString(iSealMaxCount) + sAuthorizeCode + sAuthorizeEndDate;
		
		// 获取本地ip
		try {
			
			String address = InetAddress.getLocalHost().getHostAddress().toString();
			
			if(sServerIP == null || address == null){
				
				return "";
				
			}
			
			if(!sServerIP.contains(address)){
				
				return "";
				
			}
			
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
			
		}
		
		try {
			byte[] bSignVal = MuticaCrypt.ESSGetBase64Decode(sSignVal);
			boolean bl = MuticaCrypt.ESSVerifyData(sData.getBytes(), bSignVal,"SHA1withRSA");
			
			if(bl == false)
				
				return "";
			
			else{
				
				//根据当前时间，计算验证码
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				
				String sTime = df.format(new Date());
				
				sTime = sTime + CLTSALT;
				
				byte[] bHash = MuticaCrypt.ESSGetDigest(sTime.getBytes());
				
				String sHash = MuticaCrypt.ESSGetBase64Encode(bHash);
				
				return sHash;
				
			}
				
		} catch (MuticaCryptException e) {
			
			e.printStackTrace();
			
			return "";
			
		}
	}
	
	
	public static String GetOwnerName(String sOwnerName){
		try {
			
			byte[] bName = MuticaCrypt.ESSGetBase64Decode(sOwnerName);
			
			bName = MuticaCrypt.ESSDecryptData(bName, CLTSALT.getBytes());
			
			String sName = new String(bName);
			
			System.out.println(sName);
			
			byte[] essGetBase64Decode = MuticaCrypt.ESSGetBase64Decode(sName);
			
			return new String(essGetBase64Decode,"utf8");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return "";
			
		} 
		
	}
	
	
	
	
	/**
     * 获取服务器IP地址
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String  getServerIp(){
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
            	
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                
                SERVER_IP = ip.getHostAddress();
                
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    
        return SERVER_IP;
    }
	
	
	
	
	
}
