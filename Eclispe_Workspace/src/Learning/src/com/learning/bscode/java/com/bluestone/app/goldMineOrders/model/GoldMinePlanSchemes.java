package com.bluestone.app.goldMineOrders.model;

public enum GoldMinePlanSchemes {
	ONE_YEAR("11+1", 12), 
	ONE_AND_HALF_YEAR("16+2", 18), 
	TWO_YEAR("21+3", 24);

	String schemeName;
	int term;

	GoldMinePlanSchemes(String schemeName, int term) {
		this.schemeName = schemeName;
		this.term = term;
	}
	
	public static int getSchemeTerm(String schemeName) {
		int term =0;
		GoldMinePlanSchemes[] values = GoldMinePlanSchemes.values();
		for (GoldMinePlanSchemes goldMinePlanSchemes : values) {
			if(goldMinePlanSchemes.schemeName.equalsIgnoreCase(schemeName)){
				term =goldMinePlanSchemes.term;
			}
		}
		return term;
	}
	
	public static String getSchemeName(int term) {
		String schemeName = "";
		switch (term) {
			case 12 :
				schemeName = ONE_YEAR.schemeName;
				break;
			case 18 :
				schemeName = ONE_AND_HALF_YEAR.schemeName;
				break;
			case 24 :
				schemeName = TWO_YEAR.schemeName;
				break;
			default :
				schemeName = ONE_YEAR.schemeName;
				break;
		}
		return schemeName;
	}
	
	public static int getUserPayments(int term) {
		String schemeName = getSchemeName(term);
		String[] split = schemeName.split("\\+");
		return Integer.valueOf(split[0]);
	}
	
	public static int getBluestonePayments (int term) {
		String schemeName = getSchemeName(term);
		String[] split = schemeName.split("\\+");
		return Integer.valueOf(split[1]);
	}

}
