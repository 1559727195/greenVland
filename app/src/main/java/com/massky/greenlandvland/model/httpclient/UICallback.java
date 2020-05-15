package com.massky.greenlandvland.model.httpclient;

/**
 * 加调接口
 * @author Administrator
 *
 */
public interface UICallback {
	/**
	 * 处理程序
	 * @param data
	 */
	public void process(String data);
	public void onError(String data);
}
