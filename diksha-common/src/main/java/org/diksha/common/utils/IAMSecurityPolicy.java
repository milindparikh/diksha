package org.diksha.common.utils;

import java.util.ArrayList;

public class IAMSecurityPolicy {
	
private ArrayList<IAMSecurityPolicyStatement> iamSecurityPolicyStatements;
	
	public IAMSecurityPolicy() {
		iamSecurityPolicyStatements = new ArrayList<IAMSecurityPolicyStatement>();
	}
	public void add(IAMSecurityPolicyStatement iamSecurityPolicyStatement) {
		this.iamSecurityPolicyStatements.add(iamSecurityPolicyStatement);
	}
	
	public String toSecurityPolicyDocument() {
	
		String retValue = new String();
		String interimValue = new String();
		for (int cnt = 0; cnt < iamSecurityPolicyStatements.size(); cnt++) {
			interimValue = interimValue + iamSecurityPolicyStatements.get(cnt).toSecurityPolicyDocument();
		}
			retValue = retValue + "{ \"Version\":    \"2012-10-17\",   " + interimValue + " }";
		
		return retValue;
	}

}
