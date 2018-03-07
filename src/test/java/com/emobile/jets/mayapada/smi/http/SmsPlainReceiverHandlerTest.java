//package com.emobile.jets.mayapada.smi.http;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.emobile.jets.mayapada.lib.data.JetsConstant;
//import com.emobile.jets.mayapada.lib.data.ResultCode;
//import com.emobile.jets.mayapada.lib.data.TransactionTO;
//import com.emobile.jets.mayapada.lib.service.AppsCounterService;
//import com.emobile.jets.mayapada.lib.service.AppsTimeService;
//import com.emobile.jets.mayapada.lib.service.SystemSettingService;
//import com.emobile.jets.mayapada.lib.util.CipherUtil;
//import com.emobile.jets.mayapada.lib.util.JetsRouter;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SmsPlainReceiverHandlerTest {
//	private final static Logger LOG = LoggerFactory.getLogger(SmsPlainReceiverHandlerTest.class);
//	
//	@Mock
//	protected JetsRouter router;
//	
//	@Mock
//	protected AppsCounterService counterService;
//	
//	@Mock
//	protected SystemSettingService settingService;
//	
//	@Mock
//	protected AppsTimeService timeService;
//	
//	@InjectMocks
//	private SmsPlainReceiverHandler receiverHandler;
//	
////	@Before
//	public void setup() throws Exception {
//		when(timeService.getCurrentTime()).thenReturn(new Date());
//		when(settingService.getSettingDefaultAsString(
//				SystemSettingService.SETTING_COUNTRY_CODE)).thenReturn("62");
//		when(counterService.getNextSequence()).thenReturn("130522000001");
//		
//		HttpReceiverServer server = new HttpReceiverServer();
//		server.setId("SERVER");
//		server.setPort(1200);
//
//		receiverHandler.setId("SMI-1");
//		server.setRequestHandler(receiverHandler);
//
//		server.start();
//	}
//	
////	@Test
//	public void testSendMessage() {
//		HttpTransmitterAgent agent = new HttpTransmitterAgent();
//		agent.start();
//		
//		String message = "TRF 123456798 10000";
//		List<NameValuePair> listParam = new ArrayList<NameValuePair>();
//		listParam.add(new BasicNameValuePair(JetsConstant.PARAM_PHONE_NO, "+628128138811"));
//		listParam.add(new BasicNameValuePair(JetsConstant.PARAM_MESSAGE, message));
//		
//		String rc = agent.sendGetMessage("http://localhost:1200/test-path", listParam);
//		agent.stop();
//		ArgumentCaptor<TransactionTO> captor = ArgumentCaptor.forClass(TransactionTO.class);
//		
//		verify(router).route(captor.capture());
//		
//		Assert.assertEquals(ResultCode.SUCCESS_CODE, rc);
//		Assert.assertEquals("08128138811", captor.getValue().getPhoneNo());
//		Assert.assertEquals(message, captor.getValue().getMessage());
//		LOG.debug("Transaction: " + captor.getValue());
//	}
//	
//	@Test
//	public void testDecryptMessage() {
//		String input = "3D751BA50F00C780AA016DE8EF46F64DC6DF93850A55AA8F46C71306377D79B46237DF52CE926C56";
//		String key = "1234567890";
//		String resultDecrypt = "/?" + CipherUtil.decryptDESede(input, key);
//		System.out.println("Result: " + resultDecrypt);
//		///?p=+6285718088585&m=SAL&g=1401070000046&
//	}
//}
