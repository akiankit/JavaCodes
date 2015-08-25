package com.strings;

import java.util.LinkedList;
import java.util.List;

public class GenerateParentheses {

	static int totalNum = 0;
	public static void main(String[] args) {
		System.out.println(generateParenthesis(4));
	}
	
	public static List<String> generateParenthesis(int n) {
		totalNum = n;
		List<String> list = new LinkedList<String>();
		generate("", 0, 0, list);
        return list;
    }
	
	public static void generate(String paran, int open, int close,List<String> list){
		if (close > open)
			return;
		if (open == totalNum && close == totalNum) {
			list.add(paran);
			return;
		} 
		if (open < totalNum) {
			if(open == close)
				generate(paran + "(", open + 1, close, list);
			if(open > close) {
				generate(paran + ")", open, close + 1, list);
				generate(paran + "(", open+1, close, list);
			}
		}
		if(open == totalNum) {
			generate(paran +")", open, close +1, list);
		}
	} 
}
