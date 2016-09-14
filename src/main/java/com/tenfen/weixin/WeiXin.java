package com.tenfen.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

import com.tenfen.weixin.parser.WxMsgKit;
import com.tenfen.weixin.vo.recv.WxRecvMsg;
import com.tenfen.weixin.vo.send.WxSendMsg;

public final class WeiXin {

	/**
	 * 安全性验证
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean access(String token, String signature, String timestamp, String nonce) {
		List<String> ss = new ArrayList<String>();
		ss.add(timestamp);
		ss.add(nonce);
		ss.add(token);
		Collections.sort(ss);
		StringBuilder builder = new StringBuilder();
		for (String s : ss) {
			builder.append(s);
		}
		return signature.equalsIgnoreCase(HashKit.sha1(builder.toString()));
	}

	/**
	 * 解析收到的信息
	 * 
	 * @param in
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static WxRecvMsg recv(InputStream in) throws JDOMException, IOException {
		return WxMsgKit.parse(in);
	}

	/**
	 * 解析收到的信息
	 * 
	 * @param in
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static WxRecvMsg recv(HttpServletRequest request) throws JDOMException, IOException {
		return WxMsgKit.parse(request);
	}

	/**
	 * 回复信息
	 * 
	 * @param msg
	 * @param out
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static void send(WxSendMsg msg, OutputStream out) throws JDOMException, IOException {
		Document doc = WxMsgKit.parse(msg);
		if (null != doc) {
			new XMLOutputter().output(doc, out);
		} else {
			Logger.getAnonymousLogger().warning("发送消息时,解析出dom为空 Msg :" + msg);
		}
	}

	/**
	 * 在接收信息的基础上交换收发双方
	 * 
	 * @param msg
	 * @return
	 */
	public static WxSendMsg builderSendByRecv(WxRecvMsg msg) {
		WxRecvMsg m = new WxRecvMsg(msg);
		String from = m.getFromUser();
		m.setFromUser(m.getToUser());
		m.setToUser(from);
		m.setCreateDt((System.currentTimeMillis() / 1000) + "");
		return new WxSendMsg(m);
	}
}
