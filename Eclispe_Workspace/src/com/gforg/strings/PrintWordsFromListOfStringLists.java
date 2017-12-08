package com.gforg.strings;

import java.util.LinkedList;
import java.util.List;

public class PrintWordsFromListOfStringLists {

    public static void main(String[] args) {
        List<List<String>> list = new LinkedList<List<String>>();
        List<String> temp = new LinkedList<String>();
        temp.add("you");
        temp.add("we");
        list.add(new LinkedList<String>(temp));
        temp.clear();
        temp.add("have");
        temp.add("are");
        list.add(new LinkedList<String>(temp));
        temp.clear();
        temp.add("sleep");
        temp.add("eat");
        temp.add("drink");
        list.add(new LinkedList<String>(temp));
        temp.clear();
        System.out.println("list="+list);
        System.out.println("Iterative");
        System.out.println(listOfSentensesIterative(list));
        System.out.println("Recursive");
        System.out.println(listOfSentencesRecursively(list, 1, list.get(0)));
    }

    private static List<String> listOfSentensesIterative(List<List<String>> list) {
        List<String> res = new LinkedList<String>();
        res = list.get(0);
        for(int i=1;i<list.size();i++){
            res = mergeTwoLists(res,list.get(i));
        }
        return res;
    }

    private static List<String> mergeTwoLists(List<String> res, List<String> list) {
        List<String> merge = new LinkedList<String>();
        for (String string : res) {
            for (String string1 : list) {
                merge.add(string + " "+string1);
            }
        }
        return merge;
    }
    
    private static List<String> listOfSentencesRecursively(List<List<String>> list, int index, List<String> res) {
        if(index == list.size())
            return res;
        List<String> merge = new LinkedList<String>();
        for (String string : res) {
            for (String string1 : list.get(index)) {
                merge.add(string + " "+string1);
            }
        }
        return listOfSentencesRecursively(list, index+1, merge);
    }

}
