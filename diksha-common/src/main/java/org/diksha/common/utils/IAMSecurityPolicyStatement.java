package org.diksha.common.utils;

import java.util.ArrayList;

public class IAMSecurityPolicyStatement {
	private ArrayList<IAMSecurityPolicyStatementPermission> iamSecurityPolicyStatementPermissions;
	
	public IAMSecurityPolicyStatement() {
		iamSecurityPolicyStatementPermissions = new ArrayList<IAMSecurityPolicyStatementPermission>();
	}
	public void add(IAMSecurityPolicyStatementPermission iamSecurityPolicyStatementPermission) {
//		System.out.println(iamSecurityPolicyStatementPermissions.size());
		this.iamSecurityPolicyStatementPermissions.add(iamSecurityPolicyStatementPermission);
	}
	
	public String toSecurityPolicyDocument() {
		String retValue = new String();
		if (iamSecurityPolicyStatementPermissions.size() == 1) {
			retValue = retValue + "\"Statement\": " + iamSecurityPolicyStatementPermissions.get(0).toSecurityPolicyDocument();
		}
		else {
			String interimValue = new String();
			for (int cnt = 0; cnt < iamSecurityPolicyStatementPermissions.size(); cnt++) {
				interimValue = interimValue + iamSecurityPolicyStatementPermissions.get(cnt).toSecurityPolicyDocument();
				
				if (cnt <  iamSecurityPolicyStatementPermissions.size()- 1) {
					interimValue = interimValue + ",";
				}
			}
			retValue = retValue + "\"Statement\": [ " + interimValue + " ]";
		}
		return retValue;
	}
	
}
