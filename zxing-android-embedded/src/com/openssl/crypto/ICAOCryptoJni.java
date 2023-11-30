package com.openssl.crypto;


public class ICAOCryptoJni {
    public static native byte[] sm4_encrypt(byte[] plaintext, byte[] key);
    public static native byte[] sm4_decrypt(byte[] ciphertext, byte[] key);
    public static native void sm2_generate_key(byte[] public_key, byte[] private_key);
    public static native byte[] sm2_sign_data(byte[] private_key, byte[] data);
    public static native boolean sm2_verify_data(byte[] public_key, byte[] data, byte[] sign_data);


    static {
        System.loadLibrary("ICAOCryptoJni");
    }

}