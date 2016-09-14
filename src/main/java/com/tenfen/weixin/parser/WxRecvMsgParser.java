package com.tenfen.weixin.parser;

import org.jdom.Document;
import org.jdom.JDOMException;

import com.tenfen.weixin.vo.recv.WxRecvMsg;

public interface WxRecvMsgParser {
	WxRecvMsg parser(Document doc) throws JDOMException;
}
