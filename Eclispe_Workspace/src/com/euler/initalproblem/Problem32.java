package com.euler.initalproblem;

import java.util.LinkedList;
import java.util.List;

public class Problem32 {

    /**
     * @param args
     */
    static int minTwoDigitNumber = 12;
    static int maxTwoDigitNumner = 98;
    static int minThreeDigitNumber = 102;
    static int maxThreeDigitNumber = 908;
    
    public static void main(String[] args) {
        List<Integer> products = new LinkedList<Integer>();
        int sum = 0;
        for(int i=minTwoDigitNumber;i<=maxTwoDigitNumner;i++){
            for(int j=minThreeDigitNumber;j<=maxThreeDigitNumber;j++){
                int product = i*j;
                if(checkIfPandigital(product,i,j) ){
//                    System.out.println("Multiplicand="+i+" Multiplier="+j+" Product="+product);
                    if(!products.contains(product)){
//                        System.out.println("Summing the sum:"+product);
                        sum += product;
                        products.add(product);
                    }
                }else{
                    for(int k=2;k<=9;k++){
                        if(product%k ==0){
                            boolean isPandigital = checkIfPandigital(product, k, product/k);
                            if(isPandigital){
//                                System.out.println("Multiplicand="+i+" Multiplier="+j+" Product="+product);
                                if(!products.contains(product)){
//                                    System.out.println("Summing the sum:"+product);
                                    sum += product;
                                    products.add(product);
                                }
                            }
                        }
                    }
                }
                
            }
        }
        System.out.println(sum);
    }

    private static boolean checkIfPandigital(int product, int i, int j) {
        /*if(product == 6952 || product == 7852){
            System.out.println("In Loop. product is="+product+" Multiplicand="+i+" Multiplier="+j);
        }*/
        boolean[] hasOccured =  new boolean[10];
        int [] numbers = new int[]{product,i,j};
        for (int k = 0; k < numbers.length; k++) {
            int currentNumber = numbers[k];
            while (currentNumber > 0) {
                int number = currentNumber % 10;
                currentNumber = currentNumber / 10;
                if(number == 0){
                    return false;
                }
                if (hasOccured[number] == true) {
                    return false;
                } else {
                    hasOccured[number] = true;
                }
            }
        }
        for(int k=0;k<hasOccured.length;k++){
            if(hasOccured[k] == false && k !=0){
                return false;
            }
        }
        return true;
    }
    /*public static void main(String[] args) {
        System.out.println(run());
    }


    public static String run() {
        // A candidate product has at most 4 digits. This is because if it has 5 digits,
        // then the two multiplicands must have at least 5 digits put together.
        int sum = 0;
        for (int i = 1; i < 10000; i++) {
            if (hasPandigitalProduct(i)){
                System.out.println(i);
                sum += i;
            }
        }
        return Integer.toString(sum);
    }


    private static boolean hasPandigitalProduct(int n) {
        // Find and examine all factors of n
        for (int i = 1; i <= n; i++) {
            if (n % i == 0 && isPandigital("" + n + i + n/i))
                return true;
        }
        return false;
    }


    private static boolean isPandigital(String s) {
        if (s.length() != 9)
            return false;
        char[] temp = s.toCharArray();
        Arrays.sort(temp);
        return new String(temp).equals("123456789");
    }*/

}
