package com.tenfen.weixin.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.tenfen.weixin.vo.recv.WxRecvMsg;
import com.tenfen.weixin.vo.send.WxSendMsg;

public final class WxMsgKit {

	private static final Map<String, WxRecvMsgParser> recvParserMap = new HashMap<String, WxRecvMsgParser>();

	static {
		// 文本消息解析程序
		recvParserMap.put("text", new WxRecvTextMsgParser());
		// 链接消息解析程序
		recvParserMap.put("link", new WxRecvLinkMsgParser());
		// 地址消息解析程序
		recvParserMap.put("location", new WxRecvGeoMsgParser());
		// 图片消息解析程序
		recvParserMap.put("image", new WxRecvPicMsgParser());
		// 事件消息解析程序
		recvParserMap.put("event", new WxRecvEventMsgParser());
	}

	public static WxRecvMsg parse(HttpServletRequest request) throws JDOMException, IOException {
		StringBuffer sb = new StringBuffer();
		String line;
		request.setCharacterEncoding("UTF-8");
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		Document dom = new SAXBuilder().build(new StringReader(sb.toString()));
		Element msgType = dom.getRootElement().getChild("MsgType");
		if (null != msgType) {
			String txt = msgType.getText().toLowerCase();
			WxRecvMsgParser parser = recvParserMap.get(txt);
			if (null != parser) {
				return parser.parser(dom);
			} else {
				System.out.println("WxRecvMsg Parser Error! MsgType=" + txt);
			}
		}
		return null;
	}

	public static WxRecvMsg parse(InputStream in) throws JDOMException, IOException {
		Document dom = new SAXBuilder().build(in);
		Element msgType = dom.getRootElement().getChild("MsgType");
		if (null != msgType) {
			String txt = msgType.getText().toLowerCase();
			WxRecvMsgParser parser = recvParserMap.get(txt);
			if (null != parser) {
				return parser.parser(dom);
			} else {
				System.out.println("Parser Error! MsgType=" + txt);
			}
		}
		return null;
	}

	public static Document parse(WxSendMsg msg) throws JDOMException {
		return msg.toDocument();
	}
}
