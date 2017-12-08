package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CardTurning {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        String[] numbers = str.split(",");
        int[] nums = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            nums[i] = Integer.parseInt(numbers[i]);
        }
        Arrays.sort(nums);
        int count = 0;
        boolean visited[] = new boolean[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (visited[i] == true)
                continue;
            if (i < nums.length - 1 && nums[i + 1] == nums[i] + 1) {
                visited[i + 1] = true;
            }
            visited[i] = true;
            count++;
        }
        if(count ==0)
            count =1;
        System.out.println(count);
    }
}
