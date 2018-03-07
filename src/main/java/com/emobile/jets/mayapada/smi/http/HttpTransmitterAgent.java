package com.emobile.jets.mayapada.smi.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.emobile.jets.mayapada.smi.data.GeneralRespVO;
import com.emobile.jets.mayapada.smi.data.ResultCode;
import com.emobile.jets.mayapada.smi.message.MessagePool;
import com.emobile.jets.mayapada.util.CipherUtil;

public class HttpTransmitterAgent {
	private final static Logger LOG = LoggerFactory.getLogger(HttpTransmitterAgent.class);
	
	@Autowired
	private MessageSource messageSource;
	
	private HttpClient httpClient;
	private String id;
	private int timeout = 30000;  // wait for 30s
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	protected String key;
	
	private boolean needRetry = true;

	public void start() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setDefaultMaxPerRoute(200);
		cm.setMaxTotal(500);
		httpClient = new DefaultHttpClient(cm);
		
		if (!needRetry) {
			LOG.debug("Retry on HTTP Request is disabled");
			((DefaultHttpClient) httpClient).setHttpRequestRetryHandler(
					new DefaultHttpRequestRetryHandler(0, false));
		}
		
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
//		HttpConnectionParams.setSoKeepalive(params, false);
		
		LOG.info("[{}] HttpTransmitterAgent is started, timeout [{}]", new String[] { id, String.valueOf(timeout) });
	}
	
	public void stop() {
		httpClient.getConnectionManager().shutdown();
		LOG.info("[{}] HttpTransmitterAgent has been stopped", new String[] { id });
	}
	
	public GeneralRespVO sendGetMessage(String url, List<NameValuePair> params) {
		HttpContext context = new BasicHttpContext();
        HttpGet httpGet = null;
        GeneralRespVO resp = new GeneralRespVO();
		try {
			URIBuilder builder = new URIBuilder(url);
			for (NameValuePair param: params) {
				builder.setParameter(param.getName(), param.getValue());
			}
			httpGet = new HttpGet(builder.build());
			
			LOG.debug("Url: {} ", new String[] {url} );
			
			HttpResponse response = httpClient.execute(httpGet, context);
			
			if (response.getStatusLine().getStatusCode() == 200) {
                LOG.debug("Response 200");
			} else {
				LOG.warn("Invalid statusCode: {}", new String[] {
						"" + response.getStatusLine().getStatusCode()} );
			}  // end if statusCode != 200
			
			String respString = EntityUtils.toString(response.getEntity());
            LOG.debug("Response: {}", new String[] { respString } );
			String decryptMessage = CipherUtil.decryptDESede(respString.split("=")[1], key);
			resp.setResultCode("0");
            resp.setMessage(decryptMessage);
			return resp;
			
		} catch (URISyntaxException us) {
			LOG.warn("URL [" + url + "] is not valid");
			resp.setResultCode(ResultCode.SMI_INVALID_DESTINATION);
            resp.setMessage("URL [" + url + "] is not valid");
            return resp;
		}catch (IOException e) {
			LOG.warn("No response from Engine: ", e);
			String errMsg = messageSource.getMessage(ResultCode.JETS_GPRS_SMI_TIMEOUT, null, null);
			errMsg = errMsg.replace(MessagePool.RC, ResultCode.JETS_GPRS_SMI_TIMEOUT);
			resp.setResultCode(ResultCode.JETS_GPRS_SMI_TIMEOUT);
			resp.setMessage(errMsg);
			return resp;
		}catch (Exception e) {
			if (httpGet != null)
				httpGet.abort();
            LOG.warn("Unknown Error", e);
            resp.setResultCode(ResultCode.SMI_UNKNOWN_ERROR);
            resp.setMessage("Unknown Error");
            return resp;
        } finally {
        	if (httpGet != null)
        		httpGet.releaseConnection();
        } // end try catch
	}
	
	
	public GeneralRespVO sendPostMessage(String url, List<NameValuePair> params) {
		HttpPost post = new HttpPost(url);
        GeneralRespVO resp = new GeneralRespVO();
        
		try {
			post.setHeader("Accept", 
		             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			post.setHeader("Accept-Language", "en-US,en;q=0.5");
			post.setHeader("Connection", "keep-alive");
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");

			post.setEntity(new UrlEncodedFormEntity(params));
			
			LOG.debug("Url: {} ", new String[] {url} );
			
			HttpResponse response = httpClient.execute(post);
			
			String respString = EntityUtils.toString(response.getEntity());
            LOG.debug("Response: {}", new String[] {
            		respString} );
            resp.setResultCode("0");
            resp.setMessage(respString);
			return resp;
			
		} 
//		catch (URISyntaxException us) {
//			LOG.warn("URL [" + url + "] is not valid");
//			resp.setResultCode(ResultCode.SMI_INVALID_DESTINATION);
//            resp.setMessage("URL [" + url + "] is not valid");
//            return resp;
//		}
		catch (IOException e) {
			LOG.warn("No response from Engine");
			String errMsg = messageSource.getMessage(ResultCode.JETS_GPRS_SMI_TIMEOUT, null, null);
			errMsg = errMsg.replace(MessagePool.RC, ResultCode.JETS_GPRS_SMI_TIMEOUT);
			resp.setResultCode(ResultCode.JETS_GPRS_SMI_TIMEOUT);
			resp.setMessage(errMsg);
			return resp;
		}catch (Exception e) {
			if (post != null)
				post.abort();
            LOG.warn("Unknown Error", e);
            resp.setResultCode(ResultCode.SMI_UNKNOWN_ERROR);
            resp.setMessage("Unknown Error");
            return resp;
//            return ResultCode.SMI_UNKNOWN_ERROR;
        } finally {
        	if (post != null)
        		post.releaseConnection();
        } // end try catch
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setNeedRetry(boolean needRetry) {
		this.needRetry = needRetry;
	}
}
