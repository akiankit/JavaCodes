package com.leetcode.medium;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Queue_Reconstruction_By_Height {

	public static void main(String[] args) {
		List<Person> inputList = new ArrayList<>();
		inputList.add(new Person(7, 0));
		inputList.add(new Person(5, 0));
		// [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
		inputList.add(new Person(4, 4));
		inputList.add(new Person(7, 1));
		inputList.add(new Person(5, 2));
		inputList.add(new Person(6, 1));
		System.out.println(inputList);
		inputList.sort(new Comparator<Person>() {

			@Override
			public int compare(Person o1, Person o2) {
				if (o1.height == o2.height) {
					return o1.people - o2.people;
				}
				return o1.height - o2.height;
			}
		});
		System.out.println(inputList);
		// List<Integer> options = new ArrayList<>();
		// options.add(0);
		// List<Person> outputList = new ArrayList<>();
		// construct(inputList, options, outputList, 0);
		// System.out.println(outputList);
	}

	private static boolean construct(List<Person> inputList, List<Integer> options, List<Person> outputList,
	                int maxOption) {
		List<Person> possibles = getPossibleOptions(inputList, options);
		// if (possibles == null || possibles.isEmpty()) {
		// List<Integer> tempOptions = new ArrayList<>(options);
		// tempOptions.add(maxOption + 1);
		// construct(inputList, tempOptions, outputList, maxOption + 1);
		// return;
		// }
		boolean valid = false;
		for (Person person : possibles) {
			List<Person> tempInputList = new ArrayList<>(inputList);
			// List<Person> tempOutputList = new ArrayList<>(outputList);
			List<Integer> tempOptions = new ArrayList<>(options);
			if (!validateLastElement(outputList, person)) {
				continue;
			}
			outputList.add(person);
			tempInputList.remove(person);
			tempOptions.add(maxOption + 1);
			valid = construct(tempInputList, tempOptions, outputList, maxOption + 1);
			if (!valid) {
				outputList.remove(person);
			} else {
				return true;
			}
		}
		return valid;
		// return outputList;
	}

	private static boolean validateLastElement(List<Person> outputList, Person lastElement) {
		if (outputList.isEmpty()) {
			return true;
		}
		int count = 0;
		for (Person person : outputList) {
			if (person.height >= lastElement.height) {
				count++;
			}
		}
		return count == lastElement.people;
	}

	private static List<Person> getPossibleOptions(List<Person> inputList, List<Integer> options) {
		List<Person> possibles = new ArrayList<>();
		for (Person person : inputList) {
			if (options.contains(person.people)) {
				possibles.add(person);
			}
		}
		return possibles;
	}

}

class Person {

	public int height;
	public int people;

	public Person(int height, int people) {
		this.height = height;
		this.people = people;
	}

	@Override
	public String toString() {
		return "[" + height + "," + people + "]";
		// return "Person(Height:" + height + ",People:" + people + ")";
	}
}
