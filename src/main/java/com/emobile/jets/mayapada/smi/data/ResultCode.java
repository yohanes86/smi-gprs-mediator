package com.emobile.jets.mayapada.smi.data;

public class ResultCode {
	public static final String SUCCESS_CODE		= "0";
	
	// SMI error - no need jets-messages
	public static final String SMI_UNKNOWN_ERROR			= "4000";
	public static final String SMI_RESPONSE_FAILED			= "4001";
	public static final String SMI_INVALID_DESTINATION		= "4002";
	
	public static final String JETS_UNKNOWN_ERROR 			= "7000";
	
	public static final String JETS_GPRS_STATUS_INACTIVE	= "7200";
	public static final String JETS_GPRS_SMI_TIMEOUT		= "7201";
}
