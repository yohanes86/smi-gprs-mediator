package com.emobile.jets.mayapada.smi.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GeneralRespVO  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String resultCode;
	private String message;
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
