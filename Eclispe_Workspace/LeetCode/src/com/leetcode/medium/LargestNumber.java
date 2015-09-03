package com.leetcode.medium;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;


public class LargestNumber {

    public static void main(String[] args) throws IOException {
/*        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String output = br.readLine();
        String expected = br.readLine();
        if(output.equals(expected))
            System.out.println("Equal hain");
        else{
            System.out.println(output.length());
            System.out.println(expected.length());
            for(int i=0;i<output.length();i++){
                if(output.charAt(i) != expected.charAt(i))
                    System.out.println("Index = "+i+" "+output.charAt(i)+" "+expected.charAt(i));
            }
        }*/
/*        String[] nums = {"122","12","121","382","384"};

        Arrays.sort(nums, new Comparator<String>() {
            @Override
            public int compare(String num1, String num2) {
                if(num1.equals(num2))
                    return 0;
                else {
                    char[] n1 = num1.toCharArray();
                    char[] n2 = num2.toCharArray();
                    if(n1[0] != n2[0]){
                        return n1[0] - n2[0] > 0 ? -1: 1;
                    }else{
                        if (n1.length == n2.length) {// If length of strings are same.
                            int i = 0;
                            while (n1[i] == n2[i])
                                i++;
                            if (i != n1.length)
                                return n1[i] - n2[i] > 0 ? -1 : 1;
                            else
                                return 0;
                        } else{// If length is different then iterate while 1st diff char is found or any string has ended.
                            int i = 0;
                            while(i<n1.length && i<n2.length && n1[i] == n2[i])
                                i++;
                            if(i==n1.length){
                                if(n2[i] < n2[0])
                                    return -1;
                                else if(n2[i] == n2[0])
                                    return -1;
                                else
                                    return 1;
                            }else if(i==n2.length){
                                if(n1[i] > n1[0])
                                    return -1;
                                else if(n1[i] == n1[0])
                                    return 1;
                                else
                                    return 1;
                            }else{
                                return n1[i] - n2[i] > 0 ? -1 : 1;
                            }
                        }
                    }
                }
            }
        });
        for(int i=0;i<nums.length;i++)
            System.out.println(nums[i]);*/
        int[] nums = {
                /*4704, 6306, 9385, 7536, 3462, 4798, 5422, 5529, 8070, 6241, 9094, 7846, 663, 6221,
                216, 6758, 8353, 3650, 3836, 8183, 3516, 5909, 6744, 1548, 5712, 2281, 3664, 7100,
                6698, 7321, 4980, 8937, 3163, 5784, 3298, 9890, 1090, 7605, 1380, 1147, 1495, 3699,
                9448, 5208, 9456, 3846, 3567, 6856, 2000, 3575, 7205, 2697, 5972, 7471, 1763, 1143,
                1417, 6038, 2313, 6554, 9026, 8107, 9827, 7982, 9685, 3905, 8939, 1048, 282, 7423,
                6327, 2970, 4453, 5460, 3399, 9533, 914, 3932, 192, 3084, 6806, 273, 4283, 2060,
                5682, 2, 2362, 4812, 7032, 810, 2465, 6511, 213, 2362, 3021, 2745, 3636, 6265,
                1518, 8398*/2,2281
        };
        System.out.println(largestNumber(nums));
    }

    static public String largestNumber(int[] nums) {
        if(nums.length ==0) return "";
        if(nums.length == 1) return Integer.toString(nums[0]);
        String[] str = new String[nums.length];
        for(int i = 0; i < nums.length; i++)
            str[i] = Integer.toString(nums[i]);
        Arrays.sort(str, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2){
                if(s1.length() == 0 && s2.length() == 0) return 0;
                if(s2.length() == 0) return 1;
                if(s1.length() == 0) return -1;
                for(int i = 0; i < s1.length() && i < s2.length(); i++){
                    if(s1.charAt(i) > s2.charAt(i)){
                        return 1;
                    }else if(s1.charAt(i) < s2.charAt(i)){
                        return -1;
                    }
                }
                if(s1.length() == s2.length()) return 0;
                if(s1.length() > s2.length()){
                    if(s1.charAt(0) < s1.charAt(s2.length()))
                        return 1;
                    else if(s1.charAt(0) > s1.charAt(s2.length()))
                        return -1;
                    else 
                        return compare(s1.substring(s2.length()), s2);
                }else{
                    if(s2.charAt(0) < s2.charAt(s1.length()))
                        return -1;
                    else if(s2.charAt(0) > s2.charAt(s1.length()))
                        return 1;
                    else
                        return compare(s1, s2.substring(s1.length()));
                }
            }
        
        });

        StringBuilder sb = new StringBuilder("");
        for(int i = nums.length-1; i >= 0; i--)
            sb.append(str[i]);
        if(sb.charAt(0) == '0') return "0";
        return sb.toString();
    }

}
