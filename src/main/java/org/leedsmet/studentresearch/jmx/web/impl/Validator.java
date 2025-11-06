package org.leedsmet.studentresearch.jmx.web.impl;

public class Validator {
	
	public static boolean isValueSet(String value)
	{ 
		boolean valid = true;
		
		if(value!=null && value.trim().length()>0)
			return valid;
		else
			return !valid;
	}

}
