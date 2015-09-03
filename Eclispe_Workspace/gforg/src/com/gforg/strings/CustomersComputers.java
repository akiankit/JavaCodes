package com.gforg.strings;

//http://www.geeksforgeeks.org/function-to-find-number-of-customers-who-could-not-get-a-computer/
public class CustomersComputers {

    public static void main(String[] args) {
        System.out.println(runCustomerSimulation(3, "GACCBGDDBAEE"));
    }

    private static int runCustomerSimulation(int maxComp, String customers) {
        int[] customersUsing = new int[26];
        int used = 0;
        int customersReturned = 0;
        int[] customerReturned = new int[26];
        for(int i=0;i<customers.length();i++) {
            int temp = customers.charAt(i)-'A';
            if(customersUsing[temp]==1){//That means a customer who was using computer is now going
                used--;
                customersUsing[temp] = 0;
            } else if(customerReturned[temp] ==1) {// Customer who could not get a computer is leaving
                customerReturned[temp] = 0;
            }else{
                if(used < maxComp) {// We can allocate computer to customer
                    used++;
                    customersUsing[temp]=1;
                }else{// All computers are full i.e. we can not alloacate computer to customer
                    customerReturned[temp] = 1;
                    customersReturned++;
                }
            }
        }
        return customersReturned;
    }
}
