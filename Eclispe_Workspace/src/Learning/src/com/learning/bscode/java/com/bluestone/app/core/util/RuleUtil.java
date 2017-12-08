package com.bluestone.app.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RuleUtil {
	
	public static enum Compare {ALLHAVE,EQUALTO,NOTEQUAL,CONTAINS,ALLNOTEMPTY,ALLDOESNOTHAVE,ALLEMPTY,LESSTHAN,LESSTHANEQUALS,GREATRERTHAN,GREATERTHANOREQUALTO};
	
	public static enum Logical {AND,OR};
	
	public enum Types {
        PRODUCT, STONE, METAL, TAG
    };
    
    public enum ProductTypes {
        NAME, PRICE, CATEGORY
    };
    
    public enum StoneTypes {
        TYPE, SHAPE, COLOR, SETTING, NOOFSTONES
    };
    
    public enum MetalTypes {
        TYPE, COLOR, PURITY
    };
    
    public enum TagTypes {
        VALUE
    };
	
	
    public static List<String> getVariables() {
        List<String> variables = new ArrayList<String>();

        for (Types type : Types.values()) {
            if(type.toString().equalsIgnoreCase("PRODUCT")){
                for(ProductTypes prodType: ProductTypes.values()){
                    variables.add(type.toString()+"."+prodType.toString());  
                }
            }
            if(type.toString().equalsIgnoreCase("STONE")){
                for(StoneTypes stoneType: StoneTypes.values()){
                    variables.add(type.toString()+"."+stoneType.toString());  
                }
            }
            if(type.toString().equalsIgnoreCase("METAL")){
                for(MetalTypes metalType: MetalTypes.values()){
                    variables.add(type.toString()+"."+metalType.toString());  
                }
            }
            if(type.toString().equalsIgnoreCase("TAG")){
                for(TagTypes tagType: TagTypes.values()){
                    variables.add(type.toString()+"."+tagType.toString());  
                }
            }
            
        }

        return variables;
      }
    
	public static List<String> getCompare() {
		  List<String> compars = new ArrayList<String>();

		  for (Compare compare : Compare.values()) {
		    compars.add(compare.toString());  
		  }

		  return compars;
		}
	
	public static List<String> getLogical() {
		  List<String> logicals = new ArrayList<String>();

		  for (Logical logical : Logical.values()) {
			  logicals.add(logical.toString());  
		  }

		  return logicals;
		}
	
	public static String[][] parseRuleToken(String rule){
		
		String[] subrules = rule.split("\\|");
		String[][] rules = new String[subrules.length][4];
		for (int i = 0; i < subrules.length; i++) {
			int j=0;
				String[] ops = subrules[i].split(" ");
				if(ops != null && ops.length>=3){
					rules[i][j++] = ops[0];
					rules[i][j++] = ops[1];
					rules[i][j++] = ops[2].replace("-", " ");
					if(ops.length > 3){
						rules[i][j++] = ops[3]; 
					}
					
				}
		}
		
	return(rules);
	}
	
	public static Map<String,String[]> parseRule(String rule){
		Map<String,String[]> rules = new LinkedHashMap<String,String[]>(); 
		String[] subrules = rule.split("\\|");
		String[] left = new String[subrules.length];
		String[] compare = new String[subrules.length];
		String[] right = new String[subrules.length];
		String[] logical = new String[subrules.length];
		
		int j=0;
		for (int i = 0; i < subrules.length; i++) {
				String[] ops = subrules[i].split(" ");
				if(ops != null && ops.length>=3){
					left[j] = ops[0];
					compare[j] = ops[1];
					right[j] = ops[2].replace("-", " ");
					if(ops.length > 3){
						logical[j] = ops[3]; 
					}
					j++;
				}
		}
		rules.put("left", left);
		rules.put("compare", compare);
		rules.put("right", right);
		rules.put("logical", logical);
	return(rules);
	}
	
	public static String createRule(String[] left,String[] right,String[] logical,String[] compare){
		String rule="";
		for(int i=0;i<left.length;i++){
			if(left[i].length()>0){
				if(i < left.length-1)
					rule = rule+left[i].trim()+" "+compare[i].trim()+" "+right[i].replace(" ", "-").trim()+" "+logical[i].trim()+"|";
				else
					rule = rule+left[i].trim()+" "+compare[i].trim()+" "+right[i].replace(" ", "-").trim();
			}
		}
		return rule;
	}
	
	public static boolean evaluateRule(Map<String,String[]> parsedRule,Map<Integer,List<String>> leftValues){
		String[] compare = parsedRule.get("compare");
		String[] right = parsedRule.get("right");
		String[] logical = parsedRule.get("logical");
		boolean[] subResult = new boolean[leftValues.size()];
		int i=0;
		for (Map.Entry<Integer,List<String>> entry : leftValues.entrySet()) {
			   
			List<String> subleft = entry.getValue();
			subResult[i] = RuleUtil.evaluateSubRule(subleft, compare[i], right[i]);
			i++;
		}
		if(i>=2){
			return RuleUtil.evaluateLogicalOps(logical, subResult);
		}else{
			return subResult[0];
		}
	}
	
	
	public static boolean evaluateSubRule(List<String> left,String compare,String right){
		Compare compare1 = Compare.valueOf(compare.toUpperCase());
		boolean result = false;
		switch (compare1) {
		case CONTAINS:
			result= RuleUtil.processContains(left, right);  
			break;
		case ALLHAVE:
			result= RuleUtil.processAllEquals(left, right);
			break;
		case ALLDOESNOTHAVE:
			result= RuleUtil.processAllDoesNotHave(left, right);
			break;
		case NOTEQUAL:
			result= RuleUtil.processAllNotEquals(left, right);
			break;
		case ALLEMPTY:
			result= RuleUtil.processEmpty(left);
			break;
		case LESSTHAN: 
			result= RuleUtil.processLessThan(left, right);
			break;
		case LESSTHANEQUALS:
			result= RuleUtil.processLessThanEquals(left, right);
			break;
		case GREATRERTHAN:
			result= RuleUtil.processGreaterThan(left, right);
			break;
		case GREATERTHANOREQUALTO:
			result= RuleUtil.processGreaterThanEquals(left, right);
			break;
		case EQUALTO:
		    result= RuleUtil.processEqualTo(left, right);
		    break;
		case ALLNOTEMPTY:
		    result= RuleUtil.processAllHave(left);
		    break;
		default:
			break;
		}
		return result;
	}
	
	public static boolean evaluateLogicalOps(String[] logical,boolean[] subResult){
		boolean temp=false,left,right;
		int j=0;
		for (int i = 0; i < (logical.length-1); i++) {
			if(logical[i] == null)
				break;
			if(i==0){
				left = subResult[j];
				right = subResult[j+1];
				j=2;
			}else{
				left = temp;
				right = subResult[j];
				j++;
			}
			Logical logicalOp = Logical.valueOf(logical[i].toUpperCase());
			switch (logicalOp) {
			case AND:
					temp = RuleUtil.processAND(left, right);
				break;
			case OR:
				temp = RuleUtil.processOR(left, right);
			break;
			}
		}
		return temp;
	}
	
	public static boolean processAND(boolean left,boolean right){
		return left && right;
		
	}
	
	public static boolean processOR(boolean left,boolean right){
		return left || right;
		
	}
	
	public static boolean processEqualTo(List<String> left,String right){
        for (String string : left) {
            if(string !=null && string.equalsIgnoreCase(right)){
                return true;
            }
        }
        return false;
        
    }
	
	public static boolean processContains(List<String> left,String right){
	    boolean result = false;
		for (String string : left) {
			if(string !=null && string.toLowerCase().contains(right.toLowerCase())){
			    result= true;
			}
		}
		return result;
		
	}
	public static boolean processAllEquals(List<String> left,String right){
		for (String string : left) {
			if(string !=null && !string.equalsIgnoreCase(right)){
				return false;
			}
		}
		if(left.size()>0)	
			return true;
		
		return false;
		
	}
	
	public static boolean processAllNotEquals(List<String> left,String right){
		for (String string : left) {
			if(string !=null && string.equalsIgnoreCase(right)){
				return false;
			}
		}
		if(left.size()>0)	
			return true;
		
		return true;
		
	}
	public static boolean processAllDoesNotHave(List<String> left,String right){
		for (String string : left) {
			if(string !=null && !string.equalsIgnoreCase(right)){
				return true;
			}
		}
		if(left.size()>0)	
			return false;
		
		return false;
		
	}
	
	public static boolean processAllHave(List<String> left){
        if(left == null)
            return false;
        
        for (String string : left) {
            if(string == null){
                return false;
            }
        }
        if(left.size()>0)
        	return true;
        else
        	return false;
        
    }
	public static boolean processEmpty(List<String> left){
		if(left == null)
			return true;
		for (String string : left) {
			if(string != null){
				return false;
			}
		}
		return true;
		
	}
	
	public static boolean processLessThan(List<String> left,String right){
		if(left.size()<=0)	
			return false;
		String value  = left.get(0);
		float leftNumber = Float.parseFloat(value);
		float rightNumber = Float.parseFloat(right);
		if(leftNumber<rightNumber)
			return true;
		
		return false;
		
	}
	
	public static boolean processLessThanEquals(List<String> left,String right){
		if(left.size()<=0)	
			return false;
		String value  = left.get(0);
		float leftNumber = Float.parseFloat(value);
		float rightNumber = Float.parseFloat(right);
		if(leftNumber<=rightNumber)
			return true;
		
		return false;
		
	}
	public static boolean processGreaterThan(List<String> left,String right){
	    
		if(left.size()<=0)	
			return false;
		float leftValue = 0;
		for (String string : left) {
		    leftValue = leftValue + Float.parseFloat(string);
        }
		    
		
		float rightNumber = Float.parseFloat(right);
		if(leftValue>rightNumber)
			return true;
		
		return false;
		
	}
	
	public static boolean processGreaterThanEquals(List<String> left,String right){
		if(left.size()<=0)	
			return false;
		String value  = left.get(0);
		float leftNumber = Float.parseFloat(value);
		float rightNumber = Float.parseFloat(right);
		if(leftNumber>=rightNumber)
			return true;
		
		return false;
		
	}
	

	
}
