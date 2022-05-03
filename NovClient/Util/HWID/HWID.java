package NovClient.Util.HWID;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.*;

import NovClient.Client;
import net.minecraft.client.main.Main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HWID {


	public static boolean hahaha = HWIDVerify();
	public static boolean BITCH = AntiCrack();
	public HWID () {

		

		if (hahaha && !BITCH) {
			//succeed
//			JOptionPane.showMessageDialog(null,"LAC Client \nBy LEFgangaDEV \nBase:ETB\nVersion:DevCode");
		}else {
			
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
		}
	}


	public static boolean HWIDVerify() {
		boolean passesd = false;
		boolean CheckVer = false;
		try {
			for (int iop = 0; iop < 7; iop++) {
				String[] essswitch;
				switch (iop) {
					case 0:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 1:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 2:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 3:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 4:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 5:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 6:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					default:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
				}
				for (int i = 0; i < essswitch.length; i++) {
					if(essswitch[i] != null && (! essswitch[i].isEmpty()) && essswitch[i].contains(HWIDUtil.oo())) {
						passesd = true;
						String[] var = essswitch[i].split(" ");
						if(var.length < 2)Runtime.getRuntime().exit(-1);
						Client.User = var[0].replaceAll("&", "\u00a7");
						break;
					}else if(essswitch[i] != null && (! essswitch[i].isEmpty()) && essswitch[i].contains("Ver:"+Client.instance.build)) {
						CheckVer = true;
					}
				}
			}
			if (!passesd) {

				if (JOptionPane.showInputDialog(null, "HWID:", HWIDUtil.oo()).toString().contains("LEFgangaDEV")) {
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					return false;
				} else {
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					return false;
				}
			}
			if (!CheckVer) {
				    JOptionPane.showMessageDialog(null, "你的版本过期啦，铁憨憨！");
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					Runtime.getRuntime().exit(-1);
					return false;
				
			}
		}catch (Exception e) {
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			Runtime.getRuntime().exit(-1);
			return false;
		}
		return true;
	}

	public static boolean AntiCrack() {
		boolean passesd = false;
		try {
			for (int iop = 0; iop < 7; iop++) {
				String[] essswitch;
				switch (iop) {
					case 0:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 1:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 2:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 3:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 4:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 5:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					case 6:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
					default:
						essswitch = HttpUtil.sendGet("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HWID.txt", null).split("\n");
						break;
				}
				for (int i = 0; i < essswitch.length; i++) {
					if(essswitch[i] != null && (! essswitch[i].isEmpty()) && essswitch[i].contains("FAKECRACKINGMEFAKECRACKINGMEFAKECRACKINGMEFAKECRACKINGMEFAKECRAC")) {
						passesd = true;
						break;
					}
				}
			}
			if (!passesd) {
				return false;
			}
		}catch (Exception e) {
			return false;

		}
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		Runtime.getRuntime().exit(-1);
		return true;
	}
	

}
