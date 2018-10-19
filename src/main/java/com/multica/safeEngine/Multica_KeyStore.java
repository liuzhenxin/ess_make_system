package com.multica.safeEngine;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;

public class Multica_KeyStore {

	public KeyStore getKeyStore(String sStoreType,String sProviderName,String sStoreName,String sStorePin)
	{
		KeyStore inputKeyStore = null;
		try {
			inputKeyStore = KeyStore.getInstance(sStoreType, sProviderName);
			java.io.FileInputStream fis = new java.io.FileInputStream(sStoreName);
			inputKeyStore.load(fis, sStorePin.toCharArray());
			return inputKeyStore;
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {



	}

}
