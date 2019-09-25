package com.ichi2yiji.anki.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 微信返回
 * @author shiqing  2015-7-23
 *
 */
public class WeixinBean {

	/**
	 *   "data": {
			 "appid": "wx01754ee3c11b2f17",
			 "partnerid": "1483129732",
			 "prepayid": "wx20170629161108cb0f60be720354127705",
			 "noncestr": "s6LsoFSyTsSOr7YH",
			 "timestamp": 1498723868,
			 "package": "Sign=WXPay",
			 "sign": "75BE44C5A7D8488C3FE9694EA442E379"
		 }
	 */

	private String appid;
	private String partnerid;
	private String prepayid;
	private String noncestr;
	private String timestamp;
	@SerializedName("package")
	private String packageStr;
	private String sign;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPackageStr() {
		return packageStr;
	}

	public void setPackageStr(String packageStr) {
		this.packageStr = packageStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "WeixinBean{" +
				"appid='" + appid + '\'' +
				", partnerid='" + partnerid + '\'' +
				", prepayid='" + prepayid + '\'' +
				", noncestr='" + noncestr + '\'' +
				", timestamp='" + timestamp + '\'' +
				", packageStr='" + packageStr + '\'' +
				", sign='" + sign + '\'' +
				'}';
	}
}
