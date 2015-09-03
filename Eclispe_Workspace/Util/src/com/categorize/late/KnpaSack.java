
package com.categorize.late;

//http://www.youtube.com/watch?v=UhFvK3uERGg

public class KnpaSack {

    static int[] weights = {
            2,3,4,5
    };

    static int[] benefits = {
            4, 7, 5, 3
    };

    static int maxWeight = 11;

    static int maxBenefit = 0;

    public static void main(String[] args) {
        maxBenefit = getMaxBenefit(weights, benefits, weights.length - 1, maxWeight, maxBenefit);
        System.out.println(maxBenefit);
    }

    // TODO Optimize using memoization
    static int getMaxBenefit(int[] weights, int[] benefits, int index, int maxWeight, int maxBenefit) {
        if (index < 0 || maxWeight < 0) {
            return 0;
        }
        if (maxWeight == 0) {
            return maxBenefit;
        }
        if (index == 0) {
            if (maxWeight >= weights[index]) {
                maxBenefit += benefits[index];
            } else {
                return maxBenefit;
            }
        } else {
            int benefit1 = getMaxBenefit(weights, benefits, index - 1, maxWeight, maxBenefit);
            int benefit2 = getMaxBenefit(weights, benefits, index - 1, maxWeight - weights[index],
                    maxBenefit + benefits[index]);
            maxBenefit = benefit1 > benefit2 ? benefit1 : benefit2;
        }
        return maxBenefit;
    }

}
