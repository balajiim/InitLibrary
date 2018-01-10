package com.venue.venueidentity.utils;

import android.content.Context;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by santhana on 12/14/17.
 */

public class IdentityUtility {

    private final String TAG = IdentityUtility.class.getName();
    private static IdentityUtility single_instance = null;
    Context mContext;
    private KeyStore keyStore;

    public IdentityUtility(Context context) {
        mContext = context;
    }

    // static method to create instance of Singleton class
    public static IdentityUtility getInstance(Context context) {
        if (single_instance == null)
            single_instance = new IdentityUtility(context);
        return single_instance;
    }

    public void initKeystore() {
        try {
            loadKeyStore();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final String KEYSTORE_PROVIDER = "AndroidKeyStore";

    public void loadKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        keyStore.load(null);
    }

}
