package com.initial.util;

class Link {
    public int data1;
    //public double data2;
    public Link nextLink;

    //Link constructor
    public Link(int d1) {
	    data1 = d1;
	  //  data2 = d2;
    }

    //Print Link data
    public void printLink() {
	    //System.out.print("{" + data1 + ", " + data2 + "} ");
    	System.out.print("{" + data1 + "} ");
    }
}

class LinkList {
    private Link first;

    //LinkList constructor
    public LinkList() {
	    first = null;
    }

    //Returns true if list is empty
    public boolean isEmpty() {
	    return first == null;
    }

    //Inserts a new Link at the first of the list
    public void insertAtStart(int d1,double d2) {
	    //Link link = new Link(d1, d2);
    	Link link = new Link(d1);
	    link.nextLink = first;
	    first = link;
    }
    
    public void insertAtLast(int d1,double d2) {
	    //Link link = new Link(d1, d2);
    	Link link = new Link(d1);
    	Link temp = first;
    	if(first != null){
	    	while(temp.nextLink != null){
	    		temp = temp.nextLink;
	    	}
	    	temp.nextLink = link;
    	}else{
    		link.nextLink = first;
    	    first = link;
    	}
    	
	    /*link.nextLink = first;
	    first = link;*/
    }
    public void insertSorted(int d1,double d2) {
	    //Link link = new Link(d1, d2);
    	Link link = new Link(d1);
    	if(first != null){
    		Link temp = first;
    		Link temp2 = null;
    		while(temp != null && temp.data1 < d1){
    			temp2 = temp;
    			temp = temp.nextLink;
    		}
    		if(temp2==null){
    			link.nextLink = first;
    			first = link;
    		}else if(temp == null){
    			temp2.nextLink = link;
    		}else{
    			link.nextLink = temp;
    			temp2.nextLink = link;
    		}
    	}else{
    		link.nextLink = first;
    	    first = link;
    	}
    	
	    /*link.nextLink = first;
	    first = link;*/
    }
    
    

    //Deletes the link at the first of the list
    public Link delete() {
	    Link temp = first;
	    first = first.nextLink;
	    return temp;
    }

    //Prints list data
    public void printList() {
	    Link currentLink = first;
	    System.out.print("List: ");
	    while(currentLink != null) {
		    currentLink.printLink();
		    currentLink = currentLink.nextLink;
	    }
	    System.out.println("");
    }
}  

public class LinkListTest {
    public static void main(String[] args) {
	    LinkList list = new LinkList();

	    list.insertSorted(1, 1.01);
	    list.insertSorted(2, 2.02);
	    list.insertSorted(9, 5.05);
	    list.insertSorted(3, 3.03);
	    list.insertSorted(4, 4.04);
	    list.insertSorted(6, 5.05);
	    list.insertSorted(12, 5.05);
	    list.insertSorted(5, 5.05);
	    list.insertSorted(8, 5.05);

	    list.printList();

	    while(!list.isEmpty()) {
		    Link deletedLink = list.delete();
		    System.out.print("deleted: ");
		    deletedLink.printLink();
		    System.out.println("");
	    }
	    list.printList();
    }
}