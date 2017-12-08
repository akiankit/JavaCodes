package com.bluestone.app.reporting;

public class GenericReport extends Report {

	private final String[] data;

	public GenericReport(String[] data) {
		this.data = data;
	}

	@Override
	public String[] asCSVRecord() {
		return data;
	}
}
