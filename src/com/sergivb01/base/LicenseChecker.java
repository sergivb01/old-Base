package com.sergivb01.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LicenseChecker{

	/*
	format:
		mysuperHWID;192.168.1.62
	*/
	public boolean hasValidLicense() throws IOException{
		Map<String, String> map = new HashMap<>();
		URL url;
		BufferedReader in;
		try{
			url = new URL(new Object() {int t;public String toString() {byte[] buf = new byte[33];t = 1231951266;buf[0] = (byte) (t >>> 2);t = -1005727385;buf[1] = (byte) (t >>> 10);t = 1732806083;buf[2] = (byte) (t >>> 20);t = -419382147;buf[3] = (byte) (t >>> 20);t = 1315972380;buf[4] = (byte) (t >>> 21);t = 1667775042;buf[5] = (byte) (t >>> 8);t = -739887940;buf[6] = (byte) (t >>> 2);t = 71624170;buf[7] = (byte) (t >>> 5);t = -987073657;buf[8] = (byte) (t >>> 3);t = 1641253505;buf[9] = (byte) (t >>> 24);t = -1097020905;buf[10] = (byte) (t >>> 14);t = -878621633;buf[11] = (byte) (t >>> 19);t = 427074210;buf[12] = (byte) (t >>> 22);t = -841038815;buf[13] = (byte) (t >>> 9);t = -825076985;buf[14] = (byte) (t >>> 17);t = 1098718132;buf[15] = (byte) (t >>> 6);t = 1009702156;buf[16] = (byte) (t >>> 16);t = -439994573;buf[17] = (byte) (t >>> 12);t = -567625758;buf[18] = (byte) (t >>> 7);t = 1830978516;buf[19] = (byte) (t >>> 24);t = 1272117786;buf[20] = (byte) (t >>> 12);t = -1245141154;buf[21] = (byte) (t >>> 18);t = -971019433;buf[22] = (byte) (t >>> 20);t = 830192255;buf[23] = (byte) (t >>> 11);t = 73395807;buf[24] = (byte) (t >>> 1);t = -516549993;buf[25] = (byte) (t >>> 1);t = -1157745847;buf[26] = (byte) (t >>> 8);t = -1811477528;buf[27] = (byte) (t >>> 22);t = -564515718;buf[28] = (byte) (t >>> 14);t = 859076984;buf[29] = (byte) (t >>> 23);t = 1069116351;buf[30] = (byte) (t >>> 3);t = 1754231191;buf[31] = (byte) (t >>> 24);t = 1685695000;buf[32] = (byte) (t >>> 7);return new String(buf);}}.toString());
			in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		}catch(IOException e){
			System.out.println(new Object() {int t;public String toString() {byte[] buf = new byte[6];t = -1254323030;buf[0] = (byte) (t >>> 9);t = -1675271817;buf[1] = (byte) (t >>> 8);t = 560799869;buf[2] = (byte) (t >>> 13);t = 744164200;buf[3] = (byte) (t >>> 14);t = -335128393;buf[4] = (byte) (t >>> 12);t = -866576189;buf[5] = (byte) (t >>> 14);return new String(buf);}}.toString());
			return false;
		}

		String inputLine;
		while((inputLine = in.readLine()) != null){
			String[] args = inputLine.split(";");
			map.put(args[0], args[1]); //hwid;ip
		}
		in.close();

		String address = getPublicIP();
		String hwid;
		try{
			hwid = generateHWID();
		}catch(NoSuchAlgorithmException e){
			System.out.println(new Object() {int t;public String toString() {byte[] buf = new byte[6];t = -1310123349;buf[0] = (byte) (t >>> 10);t = 816614365;buf[1] = (byte) (t >>> 23);t = -1148279986;buf[2] = (byte) (t >>> 3);t = -600647524;buf[3] = (byte) (t >>> 9);t = -1281369790;buf[4] = (byte) (t >>> 6);t = 2075354139;buf[5] = (byte) (t >>> 8);return new String(buf);}}.toString());
			return false;
		}

		System.out.println("=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#");
		System.out.println("License checker - Here are your personal details:");
		System.out.println("Public IP Address: " + address);
		System.out.println("HWID: " + hwid);
		System.out.println("Please send this details to a plugin developer to get your plugin version activated.");
		System.out.println("=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#=#");

		return map.containsKey(hwid) && map.get(hwid).equals(address);
	}

	private String generateHWID() throws UnsupportedEncodingException, NoSuchAlgorithmException{
		StringBuilder s = new StringBuilder();
		final String main = System.getenv(new Object() {int t;public String toString() {byte[] buf = new byte[20];t = -950294495;buf[0] = (byte) (t >>> 7);t = -577723118;buf[1] = (byte) (t >>> 9);t = -738436744;buf[2] = (byte) (t >>> 22);t = -1664493029;buf[3] = (byte) (t >>> 3);t = -527337272;buf[4] = (byte) (t >>> 14);t = 1980569934;buf[5] = (byte) (t >>> 2);t = 2086313411;buf[6] = (byte) (t >>> 11);t = -960113708;buf[7] = (byte) (t >>> 6);t = 313042186;buf[8] = (byte) (t >>> 9);t = 2065792773;buf[9] = (byte) (t >>> 10);t = 1265067156;buf[10] = (byte) (t >>> 4);t = 1111769951;buf[11] = (byte) (t >>> 16);t = 439346346;buf[12] = (byte) (t >>> 19);t = -1633856736;buf[13] = (byte) (t >>> 17);t = -877756078;buf[14] = (byte) (t >>> 2);t = 1361482086;buf[15] = (byte) (t >>> 18);t = -1853729444;buf[16] = (byte) (t >>> 22);t = -1435687362;buf[17] = (byte) (t >>> 10);t = 1414809607;buf[18] = (byte) (t >>> 20);t = 206750842;buf[19] = (byte) (t >>> 16);return new String(buf);}}.toString()) + System.getenv(new Object() {int t;public String toString() {byte[] buf = new byte[12];t = 1782613607;buf[0] = (byte) (t >>> 9);t = -721479237;buf[1] = (byte) (t >>> 20);t = -1707814383;buf[2] = (byte) (t >>> 12);t = 1123046455;buf[3] = (byte) (t >>> 6);t = -1788192640;buf[4] = (byte) (t >>> 22);t = -11219459;buf[5] = (byte) (t >>> 16);t = 984130490;buf[6] = (byte) (t >>> 13);t = 449333841;buf[7] = (byte) (t >>> 5);t = 1785969998;buf[8] = (byte) (t >>> 19);t = 1104383930;buf[9] = (byte) (t >>> 24);t = -622218160;buf[10] = (byte) (t >>> 13);t = 1397039709;buf[11] = (byte) (t >>> 16);return new String(buf);}}.toString()) + System.getProperty(new Object() {int t;public String toString() {byte[] buf = new byte[9];t = -1654223140;buf[0] = (byte) (t >>> 22);t = -1739731241;buf[1] = (byte) (t >>> 10);t = 1523253389;buf[2] = (byte) (t >>> 17);t = 67342925;buf[3] = (byte) (t >>> 11);t = -879468787;buf[4] = (byte) (t >>> 22);t = -1685367052;buf[5] = (byte) (t >>> 22);t = -2044868561;buf[6] = (byte) (t >>> 20);t = 198404378;buf[7] = (byte) (t >>> 11);t = -1936634696;buf[8] = (byte) (t >>> 5);return new String(buf);}}.toString()).trim();
		final byte[] bytes = main.getBytes("UTF-8");
		final MessageDigest messageDigest = MessageDigest.getInstance(new Object() {int t;public String toString() {byte[] buf = new byte[3];t = 653375430;buf[0] = (byte) (t >>> 23);t = -1576129078;buf[1] = (byte) (t >>> 23);t = -1314157965;buf[2] = (byte) (t >>> 19);return new String(buf);}}.toString());
		final byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		for (final byte b : md5) {
			s.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
			if (i != md5.length - 1) {
				s.append("-");
			}
			i++;
		}
		return sha256(s.toString());
	}

	private String sha256(String base) {
		try{
			MessageDigest digest = MessageDigest.getInstance(new Object() {int t;public String toString() {byte[] buf = new byte[7];t = -158012533;buf[0] = (byte) (t >>> 14);t = 1664891025;buf[1] = (byte) (t >>> 1);t = -1332293541;buf[2] = (byte) (t >>> 6);t = 256591938;buf[3] = (byte) (t >>> 14);t = -1014251932;buf[4] = (byte) (t >>> 1);t = 904699745;buf[5] = (byte) (t >>> 24);t = 264473999;buf[6] = (byte) (t >>> 6);return new String(buf);}}.toString());
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();

			for(byte aHash : hash){
				String hex = Integer.toHexString(0xff & aHash);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	private String getPublicIP() throws IOException{
		URL whatismyip = new URL(new Object() {int t;public String toString() {byte[] buf = new byte[29];t = -1851738958;buf[0] = (byte) (t >>> 18);t = 967659142;buf[1] = (byte) (t >>> 5);t = 1732980103;buf[2] = (byte) (t >>> 20);t = 1687945873;buf[3] = (byte) (t >>> 14);t = 1931043829;buf[4] = (byte) (t >>> 24);t = 1498654270;buf[5] = (byte) (t >>> 12);t = 789564894;buf[6] = (byte) (t >>> 24);t = -135897321;buf[7] = (byte) (t >>> 9);t = -743032157;buf[8] = (byte) (t >>> 12);t = 75131019;buf[9] = (byte) (t >>> 8);t = -1225136950;buf[10] = (byte) (t >>> 1);t = 887408536;buf[11] = (byte) (t >>> 9);t = 902666704;buf[12] = (byte) (t >>> 23);t = -625677492;buf[13] = (byte) (t >>> 15);t = 1992584724;buf[14] = (byte) (t >>> 5);t = 772581256;buf[15] = (byte) (t >>> 24);t = -1747754228;buf[16] = (byte) (t >>> 3);t = 651835143;buf[17] = (byte) (t >>> 17);t = -1523381309;buf[18] = (byte) (t >>> 11);t = -34890338;buf[19] = (byte) (t >>> 18);t = 2128018027;buf[20] = (byte) (t >>> 12);t = 1761070533;buf[21] = (byte) (t >>> 5);t = -150516688;buf[22] = (byte) (t >>> 5);t = -1141918635;buf[23] = (byte) (t >>> 23);t = -1607230475;buf[24] = (byte) (t >>> 11);t = -466714990;buf[25] = (byte) (t >>> 16);t = 831956703;buf[26] = (byte) (t >>> 23);t = 1308173420;buf[27] = (byte) (t >>> 21);t = 1958123320;buf[28] = (byte) (t >>> 15);return new String(buf);}}.toString());
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream(), "UTF-8"));
		return in.readLine();
	}

}
