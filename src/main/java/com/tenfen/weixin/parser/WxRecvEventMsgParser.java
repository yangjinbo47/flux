package com.tenfen.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.tenfen.weixin.vo.recv.WxRecvEventMsg;
import com.tenfen.weixin.vo.recv.WxRecvMsg;

public class WxRecvEventMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvEventMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		String event = getElementText(root, "Event");
		String eventKey = getElementText(root, "EventKey");

		if ("LOCATION".equals(event)) {
			String latitude = getElementText(root, "Latitude");
			String longitude = getElementText(root, "Longitude");
			String precision = getElementText(root, "Precision");
			return new WxRecvEventMsg(msg, event, eventKey, latitude, longitude, precision);
		} else {			
			return new WxRecvEventMsg(msg, event, eventKey);
		}
	}

}
