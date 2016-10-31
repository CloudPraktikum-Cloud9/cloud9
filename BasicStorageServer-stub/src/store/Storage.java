package store;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Storage {
	protected boolean inRange(String key, String from, String to){
		if(from.compareTo(to) > 0){
			if(getHashedKey(key).compareTo(from) > 0 || 
					getHashedKey(key).compareTo(to) < 0){
				return true;
			}
		}else{
			if(getHashedKey(key).compareTo(from) > 0 && 
					getHashedKey(key).compareTo(to) < 0){
				return true;
			}
		}
		return false;
	}
	
	

	public static String getHashedKey(String key){

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md.reset();
		md.update((key).getBytes());
		byte[] result = md.digest();
		String hashedkey = "";
		for(int i = 0; i < 16; i++){
			int n = result[i] >> 4 & 0x0F;
		 	hashedkey += String.format("%1s", Integer.toHexString(n));
		 	n = result[i] & 0xF;
		 	hashedkey += String.format("%1s", Integer.toHexString(n));
		 }
		return hashedkey;
	}

}
