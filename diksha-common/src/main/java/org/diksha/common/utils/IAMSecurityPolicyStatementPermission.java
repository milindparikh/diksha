package org.diksha.common.utils;

import java.util.ArrayList;

public class IAMSecurityPolicyStatementPermission {
	private String effect;
	private ArrayList<String> actions;
	private ArrayList<String> resources;

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public ArrayList<String> getAction() {
		return actions;
	}

	public void setAction(String action) {
		this.actions = new ArrayList<String>();
		this.actions.add(action);

	}

	public void addAction(String action) {
		if (this.actions == null)
			this.actions = new ArrayList<String>();
		this.actions.add(action);
	}

	public ArrayList<String> getResource() {
		return resources;
	}

	public void setResource(String resource) {
		this.resources = new ArrayList<String>();
		this.resources.add(0, resource);
	}

	public void addResource(String resource) {
		if (this.resources == null)
			this.resources = new ArrayList<String>();
		this.resources.add(resource);

	}

	public String toSecurityPolicyDocument() {

		String retValue = new String();

		retValue = "{ \"Effect\": \"" + this.effect + "\", " + "\"Action\": ";
		if (this.actions.size() == 1)
			retValue = retValue + "\"" + this.actions.get(0) + "\"";
		else {
			retValue = retValue + " [ ";
			for (int cnt = 0; cnt < this.actions.size(); cnt++) {
				retValue = retValue + "\"" + this.actions.get(cnt) + "\"";
				if (cnt < this.actions.size() - 1) {
					retValue = retValue + ",";
				}
			}
			retValue = retValue + " ] ";
		}

		retValue = retValue + ", \"Resource\": ";
		if (this.resources.size() == 1)
			retValue = retValue + "\"" + this.resources.get(0) + "\"";
		else {
			retValue = retValue + " [ ";
			for (int cnt = 0; cnt < this.resources.size(); cnt++) {
				retValue = retValue + "\"" + this.resources.get(cnt) + "\"";
				if (cnt < this.resources.size() - 1) {
					retValue = retValue + ",";
				}
			}
			retValue = retValue + " ] ";
		}

		retValue = retValue + " }";

		return retValue;
	}
}
