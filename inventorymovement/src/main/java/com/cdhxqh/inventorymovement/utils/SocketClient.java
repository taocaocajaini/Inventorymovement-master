package com.cdhxqh.inventorymovement.utils;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/12/5.
 */
public class SocketClient {
    private static final int MAX_TIMEOUT = 10;

    private SocketClient() {
    }

    /**
     * 向服务端发送消息
     *  @param host
     *            主机Host或IP
     * @param port
     *            端口
     * @param timeout
 *            超时,单位秒
     * @param content
     */
    public static void send(String host, int port, int timeout, byte[] content) {
        Socket s = null;
        DataOutputStream out = null;
        try {
            s = new Socket(host, port);
            s.setSoTimeout(timeout);
            out = new DataOutputStream(s.getOutputStream());
            out.write(GetBinaryDataNoDirect(content));
            out.flush();
        } catch (Exception e) {
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                }
            if (out != null)
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            s = null;
            out = null;
        }
    }

    /**
     * 向SocketServer发送通信指令并获取回复数据
     *
     * @param host 主机名称或IP
     * @param port 端口
     * @param timeout 超时时间(秒)
     * @param content 指令内容
     * @return
     */
    public static String sendAndGetReply(String host, int port, int timeout,
                                         byte[] content) {
        String encode = "UTF-8";
        Socket s = null;
        BufferedReader in = null;
        DataOutputStream out = null;
        String line = null;
        try {
//            content = new String(GetBinaryDataNoDirect(content))
            s = new Socket(host, port);
            s.setSoTimeout(timeout);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s
//                    .getOutputStream())), true);
            out = new DataOutputStream(s.getOutputStream());
            out.write(GetBinaryDataNoDirect(content));

//            line = in.readLine();
        } catch (Exception e) {
            return "连接异常";
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                }
            if (out != null)
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
            s = null;
            out = null;
            in = null;
        }
//        try {
//            line = URLEncoder.encode(line, encode);
//        } catch (UnsupportedEncodingException e) {
//            return "连接异常";
//        }
        return "打印成功";
    }

    /**
     * 向SocketServer发送通信指令,无同步回复消息
     *
     * @param host 主机名称或IP
     * @param port 端口
     * @param timeout 超时时间(秒)
     * @param content 指令内容
     * @return
     */
    public static void sendAndNoReply(String host, int port, int timeout,
                                      String content) {
        String encode = "utf-8";
        Socket s = null;
        PrintWriter out = null;
        try {
            content = URLEncoder.encode(content, encode);
            s = new Socket(host, port);
            s
                    .setSoTimeout((timeout > MAX_TIMEOUT ? MAX_TIMEOUT
                            : timeout) * 1000);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s
                    .getOutputStream())), true);
            out.println(content);
        } catch (Exception e) {
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                }
            if (out != null)
                out.close();
            s = null;
            out = null;
        }
    }

    public static byte[] GetBinaryDataNoDirect(byte[] pData)
    {
        byte[] pData2 = new byte[4];
        pData2[0] = (byte) (1+(pData.length/16777216)*16);
        pData2[1] = (byte)(pData.length%256);
        pData2[2] = (byte)((pData.length%65536)/256);
        pData2[3] = (byte)((pData.length/65536)%256);
        byte[] data3 = new byte[pData.length+pData2.length];

//        System.arraycopy(pData,0,data3,0,pData.length);
//        System.arraycopy(pData2,0,data3,pData2.length,pData2.length);

        System.arraycopy(pData2,0,data3,0,pData2.length);
        System.arraycopy(pData,0,data3,pData2.length,pData.length);
//        pData.CopyTo(pData2,4);
        return data3;
    }

    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }
}
