package com.sleuth.api.interceptor.sign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.api.exception.SignInvalidException;
import com.sleuth.api.exception.TimeOutException;
import com.sleuth.api.interceptor.ApiVerify;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.core.utils.DateUtils;
import com.sleuth.core.web.exception.ResponseCodeException;
import com.sleuth.oauth.keypair.KeyPairUtils;

/** 签名验证
 * 
 * @author Jonse
 * @date 2021年1月14日
 */
@Component
public class ApiVerifySignature implements ApiVerify {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String ALGORITHM = "EC";//ECDSA
	static final String SIGN_ALGORITHM = "SHA256withECDSA";
	
	static final String HEADER_ACCESS_KEY = "THING-ACCESS-KEY";
	static final String HEADER_ACCESS_SIGN = "THING-ACCESS-SIGN";
	static final String HEADER_ACCESS_TIMESTAMP = "THING-ACCESS-TIMESTAMP";

	@Override
	public boolean verify(HttpServletRequest request) {
		String requestPath = request.getPathInfo();
		String queryString = request.getQueryString();
		String method = request.getMethod();
		
		String pubKey = this.notEmpty(request.getHeader(HEADER_ACCESS_KEY), HEADER_ACCESS_KEY + " can't null.");//公钥
		String sign = this.notEmpty(request.getHeader(HEADER_ACCESS_SIGN), HEADER_ACCESS_SIGN + " can't null.");//签名
		String timestamp = this.notEmpty(request.getHeader(HEADER_ACCESS_TIMESTAMP), HEADER_ACCESS_TIMESTAMP + " can't null.");//时间戳（世界时间）
		
//		logger.debug("pubKey = {}", pubKey);
//		logger.debug("sign = {}", sign);
//		logger.debug("timestamp = {}", timestamp);
		
		//相差秒数
		long subsec = (DateUtils.localeToUtc(new Date()).getTime() - Long.valueOf(timestamp)) / 1000;
		if (subsec > 30) {
			throw new TimeOutException();
		}
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(timestamp)
			.append(method)
				.append(requestPath)
					.append(queryString);
		
		if ("POST".equals(method)) {
			List<Entry<String, String>> params = this.getParams(request);
			if (params != null && params.size() > 0) {
				for (Entry<String, String> entry: params) {
					builder.append(entry.getValue());
				}
			}
		}
		
		logger.debug("sign str = {}", builder.toString());
		//转换为二进制进行验证
		byte[] data = builder.toString().getBytes();
		
		try {
			byte[] decodedPublicKey = Base64Util.toBinary(pubKey);
			BCECPublicKey publicKey = KeyPairUtils.loadPublicKey(decodedPublicKey);
			return KeyPairUtils.verify(publicKey, data, Base64Util.toBinary(sign));
        } catch (Exception e) {
            throw new SignInvalidException(e.getMessage());
        }
		
	}
	
	/** 获取所有的请求参数
	 * 
	 * @param request
	 * @return
	 */
	private List<Entry<String, String>> getParams(HttpServletRequest request) {
		Map<String, String> params = new TreeMap<String, String>();
		Enumeration<String> enumes = request.getParameterNames();
		if (enumes != null) {
			while(enumes.hasMoreElements()) {
				String name = enumes.nextElement();
				String value = request.getParameter(name);
				params.put(name, value);
			}
			//排序
			List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(params.entrySet());
			Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
	            //升序排序
	            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
	                return o1.getValue().compareTo(o2.getValue());
	            }
	        });
			return list;
		}
		return null;
	}
	
	private String notEmpty(String value, String msg) {
		if (value == null || "".equals(value.trim())) {
			throw new ResponseCodeException(msg);
		}
		return value;
	}
	
}
