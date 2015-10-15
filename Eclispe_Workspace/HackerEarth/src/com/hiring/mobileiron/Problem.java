package com.hiring.mobileiron;

import java.util.Scanner;
import java.util.Stack;


class myCalculator{
	  
	  int power(int n,int p) throws Exception
	  {
		  if(n<0 || p<0) throw new Exception("n and p should be non-negative");
		  if(p==0) return 1;
		  return n*power(n,p-1);
	  }
}

class MyRegex extends Problem{
    public static String pattern ="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    public static String Ip;
    public MyRegex(String IP){ 
        Ip = IP;
        
    }

    public static boolean isMatches(){
        return Ip.matches(pattern);
    }
}

public class Problem {
	public static void main(String []argh)
	{
		Scanner in = new Scanner(System.in);
		while(in.hasNext())
		{
			String IP = in.next();
            MyRegex mr = new MyRegex(IP);
			System.out.println(mr.isMatches());
		}
    }

	public static void divide(int a, int b){
		try {
			int c = a/b;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception");
		}finally{
			System.out.println("Finally");
		}
	}
	
	static String[] Braces(String[] values) {
		String[] res = new String[values.length];
		for(int i=0;i<values.length;i++){
			if(isValid(values[i])){
				res[i] = "YES";
			}else{
				res[i] = "NO";
			}
		}
		return res;
	}
	
	static boolean isValid(String input) {
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '(' || c == '{' || c == '[') {
				stack.push(c);
			} else if (c == ')') {
				if (stack.isEmpty())
					return false;
				else {
					char temp = stack.pop();
					if (temp != '(')
						return false;
				}
			} else if (c == '}') {
				if (stack.isEmpty())
					return false;
				else {
					char temp = stack.pop();
					if (temp != '{')
						return false;
				}
			} else if (c == ']') {
				if (stack.isEmpty())
					return false;
				else {
					char temp = stack.pop();
					if (temp != '[')
						return false;
				}
			}
		}
		return stack.isEmpty();
	}
}
