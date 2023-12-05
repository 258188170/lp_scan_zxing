package com.lpcode.decoding.v0048;


public class CodeDecJni {
    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */

    public native boolean  CodeDecode(byte[] imgData, int width, int height,byte[] appPath, byte[][] retStr);

    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */

    public native boolean  CodeRegister(byte[] appPath,byte[] regSN,byte[] retMN);
    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String  unimplementedCodeDecode();

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.HelloJni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("CodeDecJni");
    }

}