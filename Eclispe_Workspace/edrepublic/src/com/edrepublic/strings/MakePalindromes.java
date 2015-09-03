package com.edrepublic.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MakePalindromes {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        String[] fronts = str.split(",");
        str = in.readLine();
        String[] numbers = str.split(",");

        Card[] cards = new Card[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            cards[i] = new Card(i, fronts[i], Integer.parseInt(numbers[i]));
        }
        
        Arrays.sort(cards);
        int[] nums = new int[numbers.length];
        boolean[] visited = new boolean[numbers.length];
        for(int i=0;i<cards.length;i++) {
            fronts[i] = cards[i].front;
            nums[i] = cards[i].back;
        }
        long cost = 0;
        boolean midPalindrome = false;
        for (int i = 0; i < cards.length; i++) {
            if (visited[i] == true)
                continue;
            String front = cards[i].front;
            String reverse = reverseString(front);
            boolean found = false;
            int j = i + 1;
            for (; j < cards.length; j++) {
                if(visited[j] == true)
                    continue;
                if (fronts[j].equalsIgnoreCase(reverse)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                cost = cost + nums[j] + nums[i];
                visited[j] = true;
            } else if(!midPalindrome) {
                if (front.equalsIgnoreCase(reverse)) {
                    cost = cost + nums[i];
                    midPalindrome = true;
                }
            }
        }
        System.out.println(cost);
    }
    
    private static String reverseString(String string) {
        char[] array = string.toCharArray();
        int n = array.length;
        for (int i = 0; i < n / 2; i++) {
            char temp = array[i];
            array[i] = array[n - 1 - i];
            array[n - i - 1] = temp;
        }
        return String.valueOf(array);
    }

    
}

class Card implements Comparable<Card>{
    int index;
    String front;
    int back;
    
    public Card(int index, String front, int back) {
        this.back = back;
        this.front = front;
        this.index = index;
    }

    @Override
    public int compareTo(Card card) {
        return -this.back + card.back;
    }
    
    @Override
    public boolean equals(Object obj) {
        Card card = (Card)obj;
        return card.index == this.index;
    }
    
    @Override
    public int hashCode() {
        return this.index;
    }
    
    @Override
    public String toString() {
        return "front="+front+" back="+back;
    }
}
