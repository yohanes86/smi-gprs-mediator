package com.emobile.jets.mayapada.smi.data;

public class JetsConstant {
	public static final boolean TEST_MODE = false;
	
	public static final String NEWLINE				= "<CR>";
	public static final String PARAM_NULL_STRING	= "^";  // used in STK n mbiz to indicate null
			
	public static final String LOG_TRX_BTI			= "bti.transaction";
	public static final String LOG_TRX_SMI			= "smi.transaction";
	
	public static final String STATUS_ACTIVE 		= "A";
	public static final String STATUS_TERMINATE 	= "T";
	
	
	public static final String UPDATED_BY_SYSTEM 	= "SYS";
	
	public static final String AUTH_STATUS_NO_NEED_AUTH		= "N";
	public static final String AUTH_STATUS_APPROVE			= "A";
	public static final String AUTH_STATUS_PENDING			= "Y";
	public static final String AUTH_STATUS_REJECT 			= "R";
	
	public static final int DB_TRUE	 = 1;
	public static final int DB_FALSE = 0;
	
	public static final int DAILY 	 = 1;
	public static final int WEEKLY 	 = 2;
	public static final int MONTHLY  = 3;
				
	public static final String CIF_ACCESS_TYPE_ADMIN 	= "1";
	
	public static final String FEATURE_BLAST_SMS		= "1"; //feature blast SMS enable
	
	public static final String ISO_REQUEST      		= "IsoRequest"; //iso request
	
	//BP
	public static final String TRX_TYPE_PAYMENT      = "BP"; //Payment
	public static final String TRX_TYPE_AIRTIME      = "VC"; //Airtime
	public static final String TRX_TYPE_TRANSFER     = "FT"; //Fund Transfer
	
	//BP
	public static final String BP_TRX_CODE_PAYMENT     		 = "BP_BILL_PMT"; //bill payment 
	public static final String BP_TRX_CODE_AIRTIME     		 = "BP_REFILL"; //airtime
	public static final String BP_TRX_CODE_TRANSFER    		 = "BP_TRANSFER"; //fund transfer
	public static final String BP_TRX_CODE_TRANSFER_OTHER    = "BP_TRANSFER_OTH"; //fund transfer other
	
	// param for flag ussd mbiz
	public static final String PARAM_USSD_MBIZ  = "USSD_MBIZ"; //ussd mbiz
	// trx code for MbizTrx
	public static final String TRX_CODE_MBIZ    = "MA_PUR_ATM"; //fund transfer other
	
	//for masking log inq balance
	public static final String TRX_CODE_INQ_BALANCE     = "BAL_INQ"; //inq balance
	public static final String TRX_CODE_INQ_BALANCE_SCH = "BAL_INQ_SCH"; //inq balance by scheduller
	
	public static final String TRX_CODE_FUND_TRANSFER = "FND_TRF";
	public static final String TRX_CODE_FUND_TRANSFER_OTHER = "TRF_OTH";
	
	public static final String BILLER_CODE_PLN_PREPAID  = "plnprepaid"; //biller code for pln prepaid
	public static final String BILLER_CODE_PLN_POSTPAID = "plnpostpaid"; //biller code for pln postpaid
	public static final String BILLER_CODE_PLN_NONTAGLIS= "plnnontag"; //biller code for pln non taglis
	
	public static final String CODE_ADVICE_PLN_POSTPAID = "81";
	
	// used by sms smi
	public static final String PARAM_PHONE_NO			= "p";
	public static final String PARAM_MESSAGE			= "m";
	public static final String PARAM_GATEWAY_REF		= "g";
	public static final String PARAM_HOST_REF			= "h";
	public static final String PARAM_CHANNEL			= "c";
	public static final String PARAM_TRX_CODE			= "t";
	public static final String PARAM_RC					= "rc";
	public static final String PARAM_STATE				= "state";
	public static final String PARAM_MMBSREF			= "mmbsref";
	public static final String PARAM_MESSAGE_ENCRYPTED	= "x";
	
	// gprs
	public static final String HTTP_PARAM_GPRS_VERSION		= "v";
	public static final String HTTP_PARAM_GPRS_PHONE_NO		= "p";
	public static final String HTTP_PARAM_GPRS_TRX_CODE		= "c";
	public static final String HTTP_PARAM_GPRS_DEVICE_ID	= "d";
	public static final String HTTP_PARAM_GPRS_INDEX		= "i";
	public static final String HTTP_PARAM_GPRS_MESSAGE		= "m";
	public static final String HTTP_PARAM_GPRS_TOKEN		= "t";
	public static final String HTTP_PARAM_GPRS_RESULT_CODE	= "rc";
	public static final String HTTP_PARAM_GPRS_KEY			= "k";
	public static final String HTTP_PARAM_GPRS_VERIFY_FLAG	= "f";
	
	public static final String UNKNOWN_TRX_CODE			= "UNKNOWN";
	
	//Mbiz - Fund Flow
	public static final String STAGING 		= "0"; 
	public static final String VENDOR 		= "1";
	public static final String MERCHANT 	= "2";
	public static final String BANK 		= "3";
	public static final String SSP 			= "4";
	public static final String RETAILER 	= "5";		// central for ftransfer
	public static final String CLIENT 		= "6";		// terminal for ftransfer
	public static final String DISTRIBUTOR  = "7";
	public static final String OTHER 		= "8";
	//used in wallet system only
	public static final String GL 			= "9"; 
	
	public static final String SOURCE_PAY_YES = "Y";
	
	public static final String CHANNEL_SMS = "SMS";
	
	
	public static final String CHANNEL_TYPE_GPRS 			= "GPRS";
}
