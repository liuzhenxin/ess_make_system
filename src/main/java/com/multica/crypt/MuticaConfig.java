package com.multica.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MuticaConfig {
	public String GetKeyVal(String sKey) throws FileNotFoundException, IOException
	{
		String stemp = this.getClass().getClassLoader().getResource("multicacrypt.properties").toString();
		String sOS = System.getProperties().getProperty("os.name");
		boolean blWindows = false;
		if(sOS.startsWith("win") || sOS.startsWith("Win"))
			blWindows = true;
		String sFile = "";
		if(blWindows)
		{
			sFile = stemp.substring(6);
			File f = new File(sFile);
			if(!f.exists())
			{
				System.out.println("can not find file:" + sFile);
				return null;
			}			
		}else
		{
			sFile = stemp.substring(5);
			File f = new File(sFile);
			if(!f.exists())
			{
				System.out.println("can not find file:" + sFile);
				return null;
			}
		}
		Properties pro = new Properties();
		pro.load(new FileInputStream(sFile));
		return pro.getProperty(sKey);
	}
}
