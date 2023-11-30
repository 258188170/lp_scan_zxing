package com.lpcode.decoding.v0047.l1;


public class CodeDecJni {
    private static int count;
    private static byte[][] arr;
    private static byte[] readnum;

    public native int CodeDecode(byte[] imgData, int width, int height, byte[] appPath, byte[][] retStr);

    public static native byte[] ImageDecode(byte[] imgCompress);

    public static void reset() {
        count = 0;
        arr = null;
        readnum = null;
    }

    public static byte[] getRead() {
        return readnum;
    }

    public static byte[] getData() {
        if (count <= 0)
            return null;
        byte[] byout = null;
        boolean flag = true;
        int total_length = 0;
        for (int i = 0; i < count; i++) {
            if (arr[i] == null) {
                flag = false;
                readnum[i] = 0;
            } else {
                total_length += arr[i].length;
                readnum[i] = 1;
            }
        }
        if (flag) {
            byout = new byte[total_length + count - 1];
            int pos = 0;
            for (int i = 0; i < count; i++) {
                if (i > 0 && arr[i - 1].length > 0) byout[pos++] = '|';
                System.arraycopy(arr[i], 0, byout, pos, arr[i].length);
                pos += arr[i].length;
            }
            byte[] bytemp = byout;
            byout = new byte[pos];
            System.arraycopy(bytemp, 0, byout, 0, pos);
            byout = correctLevelString(byout);
        }
        return byout;
    }

    public static boolean addData(byte[] bysrc) {
        String strtmp = new String(bysrc);
        String[] tmplist = strtmp.split("\\|");
        if (tmplist.length > 1) {
            count = Integer.parseInt(tmplist[0]);//code number
            int index = Integer.parseInt(tmplist[1]) - 1;//index from 0
            if (arr == null || count != arr.length) {
                arr = new byte[count][];
                readnum = new byte[count];
            }
            int pos = tmplist[0].length() + tmplist[1].length() + 2;
            arr[index] = new byte[bysrc.length - pos];
            System.arraycopy(bysrc, pos, arr[index], 0, bysrc.length - pos);
            return true;
        } else {
            count = 0;
            arr = null;
            readnum = null;
            return false;
        }
    }

    public static byte[] correctLevelString(byte[] bysrc) {
        int srcpos = 0, despos = 0;
        String[] codeArray = new String(bysrc, 0, bysrc.length).split("\\|");
        byte[] lastid = null, lastcontent = null;
        byte[] result = new byte[bysrc.length];
        while (codeArray.length >= 2) {
            byte[] id = codeArray[0].getBytes();
            srcpos += codeArray[0].length() + 1;
            int len = Integer.valueOf(codeArray[1]);
            boolean isSplit = codeArray[1].length() > 1 && codeArray[1].charAt(0) == '0';
            srcpos += codeArray[1].length() + 1;
            byte[] content = new byte[len];
            System.arraycopy(bysrc, srcpos, content, 0, len);
            srcpos += len + 1;
            if (lastid == null) {
                lastid = id;
                lastcontent = content;
            } else if (!isSplit) {
                if (despos > 0) result[despos++] = '|';
                System.arraycopy(lastid, 0, result, despos, lastid.length);
                despos += lastid.length;
                String lastlen = String.format("|%d|", lastcontent.length);
                System.arraycopy(lastlen.getBytes(), 0, result, despos, lastlen.length());
                despos += lastlen.length();
                System.arraycopy(lastcontent, 0, result, despos, lastcontent.length);
                despos += lastcontent.length;
                lastid = id;
                lastcontent = content;
                if (srcpos >= bysrc.length) {
                    if (despos > 0) result[despos++] = '|';
                    System.arraycopy(lastid, 0, result, despos, id.length);
                    despos += id.length;
                    String contlen = String.format("|%d|", content.length);
                    System.arraycopy(contlen.getBytes(), 0, result, despos, contlen.length());
                    despos += contlen.length();
                    System.arraycopy(content, 0, result, despos, content.length);
                    despos += content.length;
                }
            } else if (isSplit) {
                byte[] tempcontent = lastcontent;
                lastcontent = new byte[lastcontent.length + content.length];
                System.arraycopy(tempcontent, 0, lastcontent, 0, tempcontent.length);
                System.arraycopy(content, 0, lastcontent, tempcontent.length, content.length);
                if (srcpos >= bysrc.length) {
                    if (despos > 0) result[despos++] = '|';
                    System.arraycopy(lastid, 0, result, despos, lastid.length);
                    despos += lastid.length;
                    String lastlen = String.format("|%d|", lastcontent.length);
                    System.arraycopy(lastlen.getBytes(), 0, result, despos, lastlen.length());
                    despos += lastlen.length();
                    System.arraycopy(lastcontent, 0, result, despos, lastcontent.length);
                    despos += lastcontent.length;
                }
            }
            if (srcpos >= bysrc.length) {
                byte[] tempresult = result;
                result = new byte[despos];
                System.arraycopy(tempresult, 0, result, 0, despos);
                break;
            }
            codeArray = new String(bysrc, srcpos, bysrc.length - srcpos).split("\\|");
        }
        return result;
    }

    public static byte[][][] splitLevelString(byte[] bysrc) {
        int count = 0;
        int pos = 0;
        String[] dataArray = new String(bysrc, pos, bysrc.length - pos).split("\\|");
        while (dataArray.length > 2) {
            pos += dataArray[0].length() + 1;
            int len = Integer.valueOf(dataArray[1]);
            pos += dataArray[1].length() + 1;
            pos += len + 1;
            count++;
            if (pos >= bysrc.length) break;
            dataArray = new String(bysrc, pos, bysrc.length - pos).split("\\|");
        }
        if (count == 0)
            return null;
        byte[][][] retArray = new byte[count][2][];
        count = 0;
        pos = 0;
        dataArray = new String(bysrc, pos, bysrc.length - pos).split("\\|");
        while (dataArray.length > 2) {
            pos += dataArray[0].length() + 1;
            int len = Integer.valueOf(dataArray[1]);
            pos += dataArray[1].length() + 1;
            retArray[count][0] = dataArray[0].getBytes();
            retArray[count][1] = new byte[len];
            System.arraycopy(bysrc, pos, retArray[count][1], 0, len);
            pos += len + 1;
            count++;
            if (pos >= bysrc.length) break;
            dataArray = new String(bysrc, pos, bysrc.length - pos).split("\\|");
        }
        return retArray;
    }

    /* This is another native method declaration that is *not*
     * implemented by 'CodeDecJni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String unimplementedCodeDecode();

    /* this is used to load the 'CodeDecJni' library on application
     * startup. */
    static {
        System.loadLibrary("CodeDecJniL1");
    }

}