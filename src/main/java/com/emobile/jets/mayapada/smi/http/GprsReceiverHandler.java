package com.emobile.jets.mayapada.smi.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.emobile.jets.mayapada.smi.data.GeneralRespVO;
import com.emobile.jets.mayapada.smi.data.JetsConstant;
import com.emobile.jets.mayapada.smi.data.ResultCode;
import com.emobile.jets.mayapada.smi.message.MessagePool;
import com.emobile.jets.mayapada.util.CipherUtil;
import com.emobile.jets.mayapada.util.CommonUtil;

public class GprsReceiverHandler extends BaseHttpReceiverHandler{
	private final static Logger LOG = LoggerFactory.getLogger(GprsReceiverHandler.class);

	public static final String HTTP_PARAM_GPRS_MESSAGE		= "m";
		
	private HttpTransmitterAgent transmitterAgent;
	
	@Autowired
	private MessageSource messageSource;

	public void setTransmitterAgent(HttpTransmitterAgent transmitterAgent) {
		this.transmitterAgent = transmitterAgent;
	}

	private ObjectMapper mapper;
//	private int waitTimeout = 5000; // 5s
	
	@Override
	protected Logger getLogger() {
		return LOG;
	}
	
	public void initHandler() {		
		mapper = new ObjectMapper();
		// faster this way, not default
		mapper.configure(SerializationConfig.Feature.USE_STATIC_TYPING, true); 
	}
	
	@Override
	protected void handleInternal(HttpRequest request, HttpResponse response, HttpContext context) 
	{
		String uri = "";
		try{
			try {
				uri = populateRequest(request);
			} catch (Exception e) {
				getLogger().warn("Unexpected Exception when parsing URI", e);
				response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				try {
					response.setEntity(new StringEntity("Internal Server Error!"));
				} catch (Exception ex) {
					getLogger().warn("Unexpected Exception when setting responseError");
				}
				return;
			}
			
			String encryptMessage = CipherUtil.encryptDESede(uri, key);
			LOG.debug("Encrypted Message: " + encryptMessage);
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair(JetsConstant.PARAM_MESSAGE_ENCRYPTED, encryptMessage));
			
			Map<String, String> mapRequest = new HashMap<String, String>();
			Properties p = new Properties();
			CommonUtil.parseQueryString(uri, p);
			Enumeration keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				mapRequest.put(key, URLDecoder.decode(p.getProperty(key), "UTF-8"));
			}  // end while looping keys
			
			GeneralRespVO resp = transmitterAgent.sendGetMessage(serverSmsUrl, urlParameters);

			if(ResultCode.SUCCESS_CODE.equals(resp.getResultCode())){
				response.setStatusCode(HttpStatus.SC_OK);
				response.setEntity(new StringEntity(resp.getMessage()));
				LOG.debug("Sent Response: " + resp.getMessage());
			}
			else{
				populateResponse(mapRequest, response, resp.getResultCode(), 
				resp.getMessage(), "", "", "", "", "", "00");
			}
		
		} 
		catch (Exception e) {
 			LOG.warn("Unexpected Exception when handleInternal", e);
			String message = messageSource.getMessage(ResultCode.JETS_UNKNOWN_ERROR, null, null);
			message = message.replace(MessagePool.RC, ResultCode.JETS_UNKNOWN_ERROR);
			try {
				Map<String, String> mapRequest = new HashMap<String, String>();
				Properties p = new Properties();
				CommonUtil.parseQueryString(uri, p);
				Enumeration keys = p.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					mapRequest.put(key, URLDecoder.decode(p.getProperty(key), "UTF-8"));
				}  // end while looping keys
				populateResponse(mapRequest, response, ResultCode.JETS_UNKNOWN_ERROR, 
						message, "", "", "", "", "", "00");
			} catch (UnsupportedEncodingException e1) {}
		}
	}
		
	private String populateRequest(HttpRequest httpRequest) throws URISyntaxException, ParseException, IOException
	{		
		String uriStr = "";
		
		if (httpRequest instanceof HttpEntityEnclosingRequest) {
			HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest) httpRequest;
			HttpEntity httpEntity = req.getEntity();
			uriStr = EntityUtils.toString(httpEntity);
		} 
		else 
		{
			URI uri = new URI(httpRequest.getRequestLine().getUri());
			uriStr = uri.getQuery();
		}
		LOG.debug("Get Request: "+ uriStr);
		return uriStr;
	}
	
	public static void parseQueryString(String query, Properties params) {
		StringTokenizer st = new StringTokenizer(query, "?&=", true);
		String previous = null;
		while (st.hasMoreTokens()) {
			String current = st.nextToken();
			if ("?".equals(current) || "&".equals(current)) {
				// ignore
			} else if ("=".equals(current)) {
				String value = "";
				if (st.hasMoreTokens())
					value = st.nextToken();
				if ("&".equals(value))
					value = ""; // ignore &
				params.setProperty(previous, value);
			} else {
				previous = current;
			}
		}
	}
	
	private void populateResponse(Map<String, String> mapRequest,
			HttpResponse response, String resultCode, String message, 
			String keyList, String encKey, String tokenNo, String phoneNo, 
			String mmbsRef, String keyIndex) throws UnsupportedEncodingException {
		String encMessage = message;
		if (!StringUtils.isEmpty(encKey)) {
			encMessage = CipherUtil.encryptDESedePKCS7(message, encKey);
		}
		Map<String, String> mapResponse = new HashMap<String, String>();
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_VERSION, 
				mapRequest.get(JetsConstant.HTTP_PARAM_GPRS_VERSION));
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_DEVICE_ID, 
				mapRequest.get(JetsConstant.HTTP_PARAM_GPRS_DEVICE_ID));
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_INDEX, keyIndex);
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_RESULT_CODE, resultCode);
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_MESSAGE, encMessage);
		mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_TOKEN, tokenNo);
		
		if (StringUtils.isNotEmpty(keyList)) {
			mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_KEY, CipherUtil.encryptDESedePKCS7(keyList, tokenNo));
		}
		if (StringUtils.isNotEmpty(phoneNo)) {
			mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_PHONE_NO, phoneNo);
		}
		if (StringUtils.isEmpty(mmbsRef)) {
			mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_VERIFY_FLAG, "0");
		} else {
			mapResponse.put(JetsConstant.HTTP_PARAM_GPRS_VERIFY_FLAG, "1");
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = mapResponse.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = mapResponse.get(key);
			if (sb.length() > 0)
				sb.append("&");
			sb.append(key).append("=").append(value);
		}
		getLogger().info("Response: " + sb.toString());
		response.setStatusCode(HttpStatus.SC_OK);
		response.setEntity(new StringEntity(sb.toString()));
	}
	
}
