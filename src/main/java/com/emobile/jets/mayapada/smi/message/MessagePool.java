package com.emobile.jets.mayapada.smi.message;

public class MessagePool {
	//special string to be replaced
	public static final String REF_NO_VAR = "@RefNo";
	public static final String SYS_NO = "@SysNo";
	public static final String SYS_DATE = "@SysDate";
	public static final String SYS_TIME = "@SysTime";
	public static final String RC = "@RC";
	
	public static final String CPIN_ORDER_PREFIX = "@CPin";
	
	// pin should be declared hre so it can be masked
	public static final String SYS_PIN = "@SysPin";
		
	// this code is used for code that does not need to be displayed, e.g for web / atm
	public static final String TRX_RESPONSE_SUCCESS		= "trxResponse.success";
	
	// code for messages
	public static final String BALANCE_INQUIRY_SUCCESS 	= "balanceInquiry.success";
	
	public static final String CHANGE_PIN_SUCCESS 	= "changePin.success";
	
	public static final String REGISTRATION_ATM_SUCCESS	= "registrationAtm.success";
	
	public static final String CHANGE_PIN_ATM_SUCCESS	= "changePinAtm.success";
	
	public static final String FUND_TRANSFER_VERIFY		= "fundTransfer.verify";
	public static final String FUND_TRANSFER_SUCCESS	= "fundTransfer.success";
	public static final String FUND_TRANSFER_VERIFY_USSD= "fundTransfer.verify.ussd";
	public static final String FUND_TRANSFER_NOTIFY_EMAIL = "fundTransfer.notify.email";
	
	public static final String FUND_TRANSFER_OTHER_VERIFY	= "fundTransferOther.verify";
	public static final String FUND_TRANSFER_OTHER_SUCCESS	= "fundTransferOther.success";
	public static final String FUND_TRANSFER_OTHER_VERIFY_USSD	= "fundTransferOther.verify.ussd";
	public static final String FUND_TRANSFER_OTHER_NOTIFY_EMAIL = "fundTransferOther.notify.email";
	
	public static final String LAST_5_TRX_INQUIRY_SUCCESS 	= "last5TrxInquiry.success";
	
	public static final String EXCHANGE_RATE_INQUIRY_SUCCESS 	= "exchangeRatesInquiry.success";
	
	public static final String INTEREST_RATES_INQUIRY_SUCCESS 	= "interestRatesInquiry.success";
	
	public static final String BILL_PAYMENT_VERIFY 		= "billPayment.verify";
	public static final String BILL_PAYMENT_SUCCESS 	= "billPayment.success";
	public static final String BILL_PAYMENT_VERIFY_USSD = "billPayment.verify.ussd";
	public static final String BILL_PAYMENT_NOTIFY_EMAIL = "billPayment.notify.email";
	
//	public static final String BILL_PAYMENT_VERIFY_KAI 	   = "billPayment.verify.kai";
//	public static final String BILL_PAYMENT_SUCCESS_KAI    = "billPayment.success.kai";
	
	public static final String AIRTIME_INQ_AMOUNT 		= "airtime.inqAmount";
	public static final String AIRTIME_VERIFY 			= "airtime.verify";
	public static final String AIRTIME_SUCCESS 			= "airtime.success";
	public static final String AIRTIME_REFILL_NOTIFY_EMAIL = "airtimeRefill.success.notify.email";
	public static final String AIRTIME_VERIFY_USSD 		= "airtime.verify.ussd";
	
	public static final String SUGGESTION_SUCCESS 		= "suggestion.success";
	
	public static final String ADVICE_PLN_VERIFY 		= "advicePln.verify";
	public static final String ADVICE_PLN_SUCCESS 		= "advicePln.success";
	
	public static final String HELP_SUCCESS 			= "help.success";
	
	public static final String INFO_SUCCESS 			= "info.success";
	
	public static final String BP_REGISTER_SUCCESS 		= "bpRegister.success";
	public static final String BP_UPDATE_SUCCESS 		= "bpUpdate.success";
	public static final String BP_UNREG_SUCCESS 		= "bpUnreg.success";
	
	//Mbiz
	public static final String MBIZ_BUYER_CONFIRM			   = "mbiz.buyer.confirm";
	public static final String MBIZ_BUYER_USSD_CONFIRM		   = "mbiz.buyer.ussd.confirm";
	public static final String MBIZ_BUYER_SUCCESS 	    	   = "mbiz.buyer.success";
	public static final String MBIZ_BUYER_FAILED 	    	   = "mbiz.buyer.failed";
	public static final String MBIZ_MERCHANT_SUCCESS 		   = "mbiz.merchant.success";	
	public static final String MBIZ_EMAIL 					   = "mbiz.email";
	public static final String MBIZ_BUYER_INQ_STATUS_SUCCESS   = "mbiz.buyer.inqStatus.success";
	
	
	//GPRS registration
	public static final String GPRS_REGISTRATION_VERIFY = "registration.gprs.verify";
	public static final String GPRS_REGISTRATION_SUCCESS = "registration.gprs.success";
	public static final String GPRS_REGISTRATION_NOTIFY ="registration.gprs.notify";
}
