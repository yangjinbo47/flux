package com.tenfen.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.tenfen.weixin.vo.recv.WxRecvMsg;
import com.tenfen.weixin.vo.recv.WxRecvPicMsg;

public class WxRecvPicMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvPicMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		return new WxRecvPicMsg(msg, getElementText(root, "PicUrl"));
	}

}
