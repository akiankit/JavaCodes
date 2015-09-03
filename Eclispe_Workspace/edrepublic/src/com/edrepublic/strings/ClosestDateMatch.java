package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClosestDateMatch {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        String[] strings = str.split("/");
        int inputMonth = Integer.parseInt(strings[0]);
        int inputDate = Integer.parseInt(strings[1]);
//        str = in.readLine();
        str = in.readLine();
        strings = str.split(",");
//        System.out.println(Arrays.toString(strings));
        String output = null;
        boolean positive = false;
        int diff = Integer.MAX_VALUE;
        for (int i = 0; i < strings.length; i++) {
            String[] dateMonth = strings[i].split("/");
            int month = Integer.parseInt(dateMonth[0]);
            int date = Integer.parseInt(dateMonth[1]);
            int tempdiff = (month - inputMonth) * 30 + date - inputDate;
            if(tempdiff >= 0){
                positive = true;
            }
            if (tempdiff < diff && tempdiff >= 0) {
                diff = tempdiff;
                output = strings[i];
            }
        }
        if(output == null && strings.length == 1){
            output = strings[0];
        }else if(output== null && positive == false){
            diff = Integer.MIN_VALUE;
            for (int i = 0; i < strings.length; i++) {
                String[] dateMonth = strings[i].split("/");
                int month = Integer.parseInt(dateMonth[0]);
                int date = Integer.parseInt(dateMonth[1]);
                int tempdiff = (month - inputMonth) * 30 + date - inputDate;
                tempdiff = tempdiff*-1;
                if (tempdiff > diff) {
                    diff = tempdiff;
                    output = strings[i];
                }
            }
        }
        System.out.println(output);
    }
}
