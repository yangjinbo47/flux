package com.tenfen.www.common;

/**
 * 
 * Title: Description:常量类
 * 
 * @author
 * @version 1.0
 */

public class Constants {
	/** session会话中存放的用户名 */
	public static final String OPERATOR_NAME = "operator_name";

	/** session会话中存放的用户ID */
	public static final String OPERATOR_ID = "operator_id";

	/** session会话中存放的用户密码 */
	public static final String OPERATOR_PWD = "operator_pwd";
	
	/** session会话中存放的用户密码 */
	public static final String OPERATOR_TYPE = "operator_type";

	/** 角色类型　 */
	public static final String OPERATOR_ROLETYPE = "operator_roletype";

	/** 角色名称　 */
	public static final String OPERATOR_ROLENAME = "operator_rolename";
	
	public static final String COOKIE_OPERATOR_NAME = "cookie_operator_name";
	
	public static final String COOKIE_OPERATOR_ID = "cookie_operator_id";

	/** 超级管理员 **/
	public static final String SUPER_ADMIN = "administrator";

	/** 超级管理员ID **/
	public static final String SUPER_ADMIN_ID = "00000";
	
	/** memcache Key 相关 **/
	public static final String PRE_FIRST_AUTHORITY = "first_authority_";
	public static final String PRE_SECOND_AUTHORITY = "second_authority_";
	
	/**
	 * 微信app相关
	 */
	public static final String WX_MCH_ID = "1249560601";
	public static final String WX_APP_ID = "wxe1abe2da599ef0f4";
	public static final String WX_APP_SECRET = "0737cf67482b4604ab731f88a97d5bc7";
	public static final String WX_PAY_SECRET = "01f7838fd25549c18150f91e66d9a81c";
	public static final String WX_PAY_CALLBACK = "http://www.gomzone.com/flux/internal/wxpay_callBack.action";
	
	/**
	 * 管理员状态
	 * @author BOBO
	 */
	public static enum OPERATOR_STATUS {

		NORMAL(1),FREEZE(2);

		private Integer value;

		OPERATOR_STATUS(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 管理员类型
	 * @author BOBO
	 */
	public static enum USER_TYPE {

		ALL(1),BJ(0);

		private Integer value;

		USER_TYPE(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 权限状态
	 * @author BOBO
	 */
	public static enum PRIVILEGE_STATUS {

		NORMAL(1),FORBID(0);

		private Integer value;

		PRIVILEGE_STATUS(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 包类型
	 * 1-全国包 2-省包
	 * @author BOBO
	 */
	public static enum PACKAGE_REGION {

		QUANGUO(1),PROV(2);

		private Integer value;

		PACKAGE_REGION(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 运营商
	 * 1-移动 2-联通 3-电信
	 * @author BOBO
	 */
	public static enum PACKAGE_OPERATOR {

		CMCC(1),CUCC(2),CTCC(3);

		private Integer value;

		PACKAGE_OPERATOR(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 包状态
	 * 1-正常 0-停用
	 * @author BOBO
	 */
	public static enum PACKAGE_STATUS {

		NORMAL(1),FREEZE(0);

		private Integer value;

		PACKAGE_STATUS(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	/**
	 * 0-成功 1-充值中 2-失败 2001-失败退款中 2002-失败已退款
	 * @author BOBO
	 */
	public static enum BSORDER_STATUS {

		SUCC(0),CHARGING(1),FAIL(2),FAILREFUNDING(2001),FAILREFUNDED(2002);

		private Integer value;

		BSORDER_STATUS(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
	
}
