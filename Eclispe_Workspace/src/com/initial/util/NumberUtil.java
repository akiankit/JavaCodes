package com.initial.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NumberUtil {

	private static Map<Integer, String> uniqueWords = new HashMap<>();

	static {
		uniqueWords.put(0, "");
		uniqueWords.put(1, "one");
		uniqueWords.put(2, "two");
		uniqueWords.put(3, "three");
		uniqueWords.put(4, "four");
		uniqueWords.put(5, "five");
		uniqueWords.put(6, "six");
		uniqueWords.put(7, "seven");
		uniqueWords.put(8, "eight");
		uniqueWords.put(9, "nine");
		uniqueWords.put(10, "ten");
		uniqueWords.put(11, "eleven");
		uniqueWords.put(12, "twelve");
		uniqueWords.put(13, "thirteen");
		uniqueWords.put(14, "fourteen");
		uniqueWords.put(15, "fifteen");
		uniqueWords.put(16, "sixteen");
		uniqueWords.put(17, "seventeen");
		uniqueWords.put(18, "eighteen");
		uniqueWords.put(19, "nineteen");
		uniqueWords.put(20, "twenty");
		uniqueWords.put(30, "thirty");
		uniqueWords.put(40, "forty");
		uniqueWords.put(50, "fifty");
		uniqueWords.put(60, "sixty");
		uniqueWords.put(70, "seventy");
		uniqueWords.put(80, "eighty");
		uniqueWords.put(90, "ninety");
		uniqueWords.put(100, "hundred");
		uniqueWords.put(1000, "one thousand");
	}

	static public String getLetterRepresentation(int num) {
		StringBuilder numInWords = new StringBuilder();
		if (num <= 20) {
			numInWords.append(uniqueWords.get(num));
		} else if (num < 100) {
			numInWords.append(uniqueWords.get((num / 10) * 10)).append(" ").append(uniqueWords.get(num % 10));
		} else if (num == 1000) {
			numInWords.append(uniqueWords.get(num));
		} else if (num % 100 == 0) {
			numInWords.append(uniqueWords.get(num / 100)).append(" ").append(uniqueWords.get(100));
		} else if (num < 1000) {
			int temp = num % 100;
			numInWords.append(uniqueWords.get((num / 100))).append(" ").append(uniqueWords.get(100)).append(" ")
			                .append("and").append(" ");
			if (temp <= 20) {
				numInWords.append(uniqueWords.get(temp));
			} else {
				numInWords.append(uniqueWords.get((temp / 10) * 10)).append(" ").append(uniqueWords.get(temp % 10));
			}
		}

		return numInWords.toString();
	}

	static public String calculateLargePower(long num, long b) {
		String res = "1";
		String num1 = String.valueOf(num);
		while (b != 0) {
			if ((b & 1) == 1)
				res = multiplyTwoLargeNumberFaster(res, num1);
			num1 = multiplyTwoLargeNumberFaster(num1, num1);
			b = b >> 1;
		}
		return res;
	}

	static public long calculateModPower(long a, long b, long mod) {
		long maxAllowed = (long) Math.sqrt(Long.MAX_VALUE);
		if (mod >= maxAllowed) {
			return calculateModPower(a, b, new BigInteger(String.valueOf(mod))).longValue();
		}
		long res = 1;
		while (b != 0) {
			if ((b & 1) == 1)
				res = (res * a) % mod;
			// if (res < 0) {
			// res = res + mod;
			// }
			a = (a * a) % mod;
			b = b >> 1;
		}
		return res;
	}

	static public BigInteger calculateModPower(long a, long b, BigInteger mod) {
		BigInteger res = new BigInteger("1");
		BigInteger a1 = new BigInteger(String.valueOf(a));
		while (b != 0) {
			if ((b & 1) == 1)
				res = res.multiply(a1).mod(mod);
			// if (res < 0) {
			// res = res + mod;
			// }
			a1 = a1.multiply(a1).mod(mod);
			b = b >> 1;
		}
		return res;
	}

	static public long calculatePower(long a, long b) {
		long res = 1;
		while (b != 0) {
			if ((b & 1) == 1)
				res = res * a;
			a = a * a;
			b = b >> 1;
		}
		return res;
	}

	static String addLargeNumbersOfSameLength(String num1, String num2) {
		List<String> sum = new LinkedList<>();
		// Assuming length of num1 and num2 are same.
		int carry = 0;
		for (int i = num1.length() - 1; i >= 0; i--) {
			int temp1 = num1.charAt(i) - 48;
			int temp2 = num2.charAt(i) - 48;
			int tempSum = temp1 + temp2 + carry;
			sum.add(String.valueOf(tempSum % 10));
			carry = tempSum / 10;
		}
		if (carry > 0) {
			sum.add(String.valueOf(carry));
		}
		StringBuilder finalsum = new StringBuilder();
		for (int i = sum.size() - 1; i >= 0; i--) {
			finalsum.append(Integer.parseInt(sum.get(i)));
		}

		return finalsum.toString();
	}

	static public String addLargeNumbersOfSameLengthUseBlock(String num1, String num2) {
		// StringBuilder sum = new StringBuilder();
		List<String> sum = new LinkedList<>();
		int blockSize = String.valueOf(Integer.MAX_VALUE).length() - 1;
		int lengthOfNumber = num1.length();
		if (lengthOfNumber <= blockSize) {
			String temp = String.valueOf(Integer.parseInt(num1) + Integer.parseInt(num2));
			if (temp.length() < num1.length()) {
				int diff = num1.length() - temp.length();
				temp = num1.substring(0, diff) + temp;
			}
			return temp;
		}
		// Assuming length of num1 and num2 are same.
		int carry = 0;
		for (int i = lengthOfNumber - 1; i >= 0; i -= blockSize) {
			int startIndex = i - blockSize + 1;
			if (startIndex < 0) {
				startIndex = 0;
			}
			String tempNum1 = num1.substring(startIndex, i + 1);
			String tempNum2 = num2.substring(startIndex, i + 1);
			String tempSum = String.valueOf(Integer.parseInt(tempNum1) + Integer.parseInt(tempNum2) + carry);
			if (tempSum.length() > blockSize && startIndex != 0) {
				carry = 1;
				tempSum = tempSum.substring(1);
			} else {
				carry = 0;
			}
			if (tempSum.length() < tempNum1.length()) {
				int diff = tempNum1.length() - tempSum.length();
				tempSum = tempNum1.substring(0, diff) + tempSum;
			}
			sum.add(0, tempSum);
		}
		StringBuilder finalSum = new StringBuilder();
		for (String string : sum) {
			finalSum.append(string);
		}
		/*
		 * if (!finalSum.toString().equals((new BigInteger(num1).add(new
		 * BigInteger(num2))).toString())) { System.out.println("temp"); }
		 */
		return finalSum.toString();
	}

	static public String addTwoLargeNumbers(String num1, String num2) {
		String res = "";
		if (num1.length() == num2.length()) {
			res = addLargeNumbersOfSameLengthUseBlock(num1, num2);
		} else {
			res = addLargeNumbersOfDifferentLength(num1, num2);
		}

		return res;
	}

	private static String addLargeNumbersOfDifferentLength(String num1, String num2) {
		int length1 = num1.length();
		int length2 = num2.length();
		StringBuilder newNum = new StringBuilder();
		String sum = "";
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			sum = addLargeNumbersOfSameLengthUseBlock(num1, newNum.toString());
		} else {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			sum = addLargeNumbersOfSameLengthUseBlock(newNum.toString(), num2);
		}
		return sum;
	}

	static public long getLCM(long lcm2, long b) {
		long lcm = lcm2 * b;
		long gcd = getGCD(lcm2, b);
		lcm = lcm / gcd;
		return lcm;
	}

	static public long getGCD(long lcm2, long b) {
		int gcd = 1;
		if (lcm2 == 0 || b == 0)
			return 1;
		else if (lcm2 == 1 || b == 1)
			return 1;
		else {
			if (lcm2 % b == 0)
				return b;
			else if (b % lcm2 == 0)
				return lcm2;
			else if (lcm2 > b)
				return getGCD(lcm2 % b, b);
			else if (lcm2 < b)
				return getGCD(lcm2, b % lcm2);
		}
		return gcd;
	}

	static public int getBigMod(int a, String b) {
		int res = 0;
		for (int i = 0; i < b.length(); i++) {
			res = (res * 10 + (b.charAt(i) - '0')) % a;
		}
		return res;
	}

	static public long getGCdEuclid(long a, long b) {
		if (b == 0)
			return a;
		return getGCdEuclid(b, a % b);
	}

	static public int getPrimesLessThanNSieve(int n) {
		int[] arr = new int[n + 1];
		for (int i = 1; i < n + 1; i++) {
			arr[i] = 1;
		}
		int sqrtN = (int) Math.sqrt(n);
		for (int i = 3; i <= sqrtN; i += 2) {
			for (int j = 3 * i; j <= n; j += (i + i)) {
				arr[j] = 0;
			}
		}
		int count = 1;
		for (int i = 3; i <= n; i += 2) {
			if (arr[i] == 1)
				count++;
		}
		return count;
	}

	public static void main(String[] args) {
		long[] digitsInNumber = getDigitsInNumber(12342);
		System.out.println(Arrays.toString(digitsInNumber));
		digitsInNumber = getDigitsCountsInNumber(12342);
		System.out.println(Arrays.toString(digitsInNumber));
		Arrays.equals(digitsInNumber, null);
	}

	static public Set<Long> getPrimesInRange(int n, int m) {
		int[] arr = new int[m + 1];
		if (n % 2 == 0) {
			n = n + 1;
		}
		for (int i = n; i < m + 1; i++) {
			arr[i] = 1;
		}
		int sqrtN = (int) Math.sqrt(m);
		for (int i = 3; i <= sqrtN; i += 2) {
			for (int j = 3 * i; j <= m; j += (i + i)) {
				if (j < n) {
					continue;
				}
				arr[j] = 0;
			}
		}
		Set<Long> primes = new HashSet<>();
		for (int i = n; i <= m; i += 2) {
			if (arr[i] == 1) {
				primes.add((long) i);
			}
		}
		return primes;
	}

	static public Set<Long> getPrimesListLessThanNSieve(int n) {
		int[] arr = new int[n + 1];
		for (int i = 1; i < n + 1; i++) {
			arr[i] = 1;
		}
		int sqrtN = (int) Math.sqrt(n);
		for (int i = 3; i <= sqrtN; i += 2) {
			for (int j = 3 * i; j <= n; j += (i + i)) {
				arr[j] = 0;
			}
		}
		Set<Long> primes = new HashSet<>();
		for (int i = 3; i <= n; i += 2) {
			if (arr[i] == 1)
				primes.add((long) i);
		}
		if (n > 2) {
			primes.add((long) 2);
		}
		return primes;
	}

	static public boolean isPrime(long n) {
		boolean isPrime = true;
		if (1 == n)
			return false;
		else if (2 == n || n == 5)
			return true;
		else if ((n & 1) == 0)
			return false;
		else if (n % 10 == 5)
			return false;

		double sqrt = Math.sqrt(n);
		for (int i = 3; i <= sqrt; i += 2) {
			if (n % i == 0) {
				// System.out.println(n+" is not a prime number.Divisible by
				// "+i);
				isPrime = false;
				break;
			}
		}
		/*
		 * if(true == isPrime){ System.out.println(n +" is a prime number"); }
		 */
		return isPrime;
	}

	public static boolean isPalindrome(String inputNumber) {
		boolean isPalindrome = true;
		String num1 = new StringBuilder(inputNumber).reverse().toString();
		if (!num1.equals(inputNumber)) {
			isPalindrome = false;
		}
		return isPalindrome;
	}

	static public boolean isPalindrome(int n) {
		return isPalindrome(String.valueOf(n));
	}

	static public int getNumOfDigits(long number) {
		int digitsCount = 0;
		while (number > 0) {
			number = number / 10;
			digitsCount++;
		}
		return digitsCount;
	}

	static public long getNthPrime(int n) {
		long[] primes = getListOfFirstNPrimes(n);
		return primes[n - 1];
	}

	/**
	 * This method does not use SIEVE method to get primes list. To get list of
	 * primes <b>below 1,000,000 it took 1216 ms.</b> :
	 * 
	 * <pre>
	 * Please use this method : NumberUtil#getPrimesListLessThanNSieve(int)
	 * </pre>
	 *
	 * @param n
	 *            -- number below which all primes
	 * @return List of primes in a sorted list
	 */
	static public List<Long> getListOfPrimesLessThanN(int n) {
		List<Long> primes = new LinkedList<>();
		if (n >= 2) {
			primes.add(2l);
			long j = 3;
			if (n > 2) {
				for (; j <= n; j++) {
					boolean isPrime = true;
					long temp = primes.get(0);
					for (int k = 1; temp <= Math.sqrt(j); k++) {
						if (j % temp == 0) {
							isPrime = false;
							break;
						}
						temp = primes.get(k);
					}
					if (true == isPrime && j <= n) {
						primes.add(j);
					}
					if (j > n) {
						break;
					}
				}
			}

		}
		return primes;
	}

	static public long[] getListOfFirstNPrimes(int n) {
		long primes[] = new long[n];
		primes[0] = 2;
		long j = 3;
		for (int i = 1; i < n; j++) {
			boolean isPrime = true;
			for (int k = 0; primes[k] <= Math.sqrt(j); k++) {
				long temp = primes[k];
				if (j % temp == 0) {
					isPrime = false;
					break;
				}
			}
			if (true == isPrime) {
				primes[i++] = j;
			}
		}
		return primes;
	}

	static public int getNumOfFactors(long n) {
		if (n == 1)
			return 1;
		else if (isPrime(n))
			return 2;
		else {
			int numOfFactors = 1;
			Set<Long> primeFactors = getPrimeFactors(n);
			int[] primeFactorsPowers = new int[primeFactors.size()];
			int k = 0;
			for (Long long1 : primeFactors) {
				long temp = n;
				int count = 1;
				while (0 == temp % long1) {
					temp = temp / long1;
					count++;
				}
				primeFactorsPowers[k++] = count;
			}
			if (primeFactors.size() == 0)
				return 2;
			for (int i : primeFactorsPowers) {
				numOfFactors *= i;
			}
			return numOfFactors;
		}

	}

	static public int getEulerValue(int num) {
		int i = 2;
		int res = num;
		for (; i * i <= num; i++) {
			if (num % i == 0) {
				res = res / i;
				res = res * (i - 1);
			}
			while (num % i == 0) {
				num = num / i;
			}
		}
		if (num > 1) {
			res = res / num;
			res = res * (num - 1);
		}
		return res;
	}

	static public int totalNumberOfFactors(long num) {
		long numbers[] = new long[500];
		long factors[] = new long[500];
		getPrimeFactors(num, numbers, factors);
		int res = (int) (factors[0] + 1);
		for (int i = 1; i < factors.length && factors[i] != 0; i++) {
			res *= (factors[i] + 1);
		}
		return res;
	}

	static public void getPrimeFactors(long num, long numbers[], long factors[]) {
		int j = 0;
		int count = 0;
		long i = 2;
		for (; i * i <= num; i++) {
			count = 0;
			while (num % i == 0) {
				count++;
				num = num / i;
			}
			if (count != 0) {
				numbers[j] = i;
				factors[j] = count;
				j++;
			}
		}
		if (num > 1) {
			numbers[j] = (int) num;
			factors[j] = 1;
			j++;
		}
	}

	// Modify it with prime factorization
	public static Set<Long> getPrimeFactors(long n) {
		Set<Long> primeFactors = new HashSet<>(50);
		int sqrt = (int) Math.sqrt(n);
		// List<Long> primes_Less_Than_N = list_Of_Primes_Less_Than_N(sqrt);
		for (long temp = 2; temp <= sqrt; temp++) {
			if (n % temp == 0) {
				primeFactors.add(temp);
			}
		}
		Set<Long> primeFactors2 = new HashSet<>(25);
		for (Long long1 : primeFactors) {
			long temp = n / long1;
			if (isPrime(temp)) {
				primeFactors2.add(temp);
			}
		}
		primeFactors.addAll(primeFactors2);
		primeFactors2.clear();
		for (Long long1 : primeFactors) {
			if (isPrime(long1)) {
				primeFactors2.add(long1);
			}
		}
		return primeFactors2;
	}

	public static List<Long> getAllFactors(long n) {
		List<Long> primeFactors = new LinkedList<>();
		primeFactors.add(1l);
		if (n != 1) {
			int sqrt = (int) Math.sqrt(n);
			for (long temp = 2; temp <= sqrt; temp++) {
				if (n % temp == 0) {
					primeFactors.add(temp);
				}
			}
			List<Long> primeFactors2 = new LinkedList<>();
			for (int i = primeFactors.size() - 1; i >= 0; i--) {
				long long1 = primeFactors.get(i);
				long temp = n / long1;
				if (false == primeFactors.contains(temp)) {
					primeFactors2.add(temp);
				}
			}
			primeFactors.addAll(primeFactors2);
		}

		return primeFactors;
	}

	static public List<Long> gePrimesLessThanN(int n) {
		List<Long> primes = new LinkedList<>();
		int[] numbers = new int[n + 1];
		boolean[] isPrime = new boolean[n + 1];
		for (int i = 0; i <= n; i++) {
			isPrime[i] = true;
		}
		for (int i = 0; i <= n; i++) {
			numbers[i] = i;
		}
		for (int i = 2; i <= Math.sqrt(n); i++) {
			for (int j = 2; i * j <= n; j++) {
				isPrime[i * j] = false;
			}
		}
		for (int i = 2; i <= n; i++) {
			if (isPrime[i] == true)
				primes.add((long) i);
		}
		return primes;
	}

	static public List<Long> gePrimesLessThanNGreaterThanM(long M, long N) {
		if (M == 1)
			M = 2;
		List<Long> primes = new LinkedList<>();
		int totalNumbers = (int) (N - M + 1);
		long[] numbers = new long[totalNumbers];
		boolean[] isPrime = new boolean[totalNumbers];
		for (int i = 0; i < totalNumbers; i++) {
			isPrime[i] = true;
		}
		for (int i = 0; i < totalNumbers; i++) {
			numbers[i] = (M + i);
		}
		for (int i = 2; i <= Math.sqrt(N); i++) {
			for (int j = Math.max(2, (int) (M / i)); i * j <= N; j++) {
				if (i * j < M)
					continue;
				else
					isPrime[(int) ((i * j) - M)] = false;
			}
		}
		for (int i = 0; i < totalNumbers; i++) {
			if (isPrime[i] == true)
				primes.add(numbers[i]);
		}
		return primes;
	}

	static public String getFactorialUsingSum(int n) {
		String factorial = "1";
		for (int i = 1; i <= n; i++) {
			String temp = factorial;
			for (int j = 1; j < i; j++) {
				factorial = addTwoLargeNumbers(factorial, temp);
			}
		}
		return factorial;
	}

	static public long getFactorialMod(long n, long mod) {
		long fact = 1;
		for (int i = 1; i <= n; i++) {
			fact = (fact * i) % mod;
		}
		return fact;
	}

	static public String getFactorialUsingMultiplication(int n) {
		String factorial = "1";
		for (int i = 1; i <= n; i++) {
			factorial = multiplyTwoLargeNumberFaster(factorial, String.valueOf(i));
		}
		return factorial;
	}

	static public List<Long> getProperDivisor(long n) {
		List<Long> allFactors = getAllFactors(n);
		allFactors.remove(allFactors.size() - 1);
		return allFactors;
	}

	static public String multiplyTwoLargeNumberFaster(String num1, String num2) {
		int length1 = num1.length();
		int length2 = num2.length();
		if (length1 > length2) {
			String temp = num1;
			num1 = num2;
			num2 = temp;
			length1 = num1.length();
			length2 = num2.length();
		}
		String partialMulti[] = new String[length2];
		String[] multiplications = new String[10];
		for (int i = length2 - 1, k = 0; i >= 0; i--, k++) {
			int temp1 = num2.charAt(i) - 48;
			int carry = 0;
			StringBuilder sb = new StringBuilder();
			// Max no. of different digits in multiplicand can be 10.
			// So storing multiplication of multiplicant by each digit to avoid
			// calculation again
			// and again
			if (multiplications[temp1] != null && !multiplications[temp1].isEmpty()) {
				sb.append(multiplications[temp1]);
			} else {
				for (int j = length1 - 1; j >= 0; j--) {
					int temp2 = num1.charAt(j) - 48;
					int multi = temp2 * temp1 + carry;
					sb.append(multi % 10);
					carry = multi / 10;
				}
				if (carry != 0) {
					sb.append(carry);
				}
				sb = sb.reverse();
				multiplications[temp1] = sb.toString();
			}
			for (int l = 0; l < k; l++) {
				sb.append(0);
			}
			partialMulti[k] = sb.toString();
		}
		String multiplication = "";
		for (String partial : partialMulti) {
			multiplication = addTwoLargeNumbers(multiplication, partial);
		}
		return multiplication;
	}

	static public String getFibonacciNthTerm(int n) {
		List<String> terms = getFibonacciSeriesNTerms(n);
		return terms.get(n - 1);
	}

	static public String getFibonacciNthTermAddition(String n) {
		if (n.equals("0") || n.equals("1"))
			return "1";
		else {
			String a = "1";
			String b = "1";
			String i = "2";
			while (!n.equalsIgnoreCase(i)) {
				String c = addLargeNumbersOfDifferentLength(a, b);
				b = a;
				a = c;
				i = addLargeNumbersOfDifferentLength(i, "1");
			}
			return a;
		}
	}

	static public List<String> getFibonacciSeriesNTerms(int n) {
		List<String> series = new LinkedList<>();
		String nthTerm = "";
		String f0 = "1";
		String f1 = "1";
		if (n == 1) {
			series.add(f0);
		} else if (n == 2) {
			series.add(f0);
			series.add(f1);
		} else {
			series.add(f0);
			series.add(f1);
			for (int i = 1; i < n - 1; i++) {
				nthTerm = addTwoLargeNumbers(f0, f1);
				series.add(nthTerm);
				f0 = f1;
				f1 = nthTerm;
			}
		}
		return series;
	}

	// Assuming sorted permutations are there
	public static String getNthPermutation(int n, List<Integer> digits) {
		List<Integer> digits1 = new LinkedList<>();
		digits1.addAll(digits);
		int location = n - 1;
		int digitsCount = digits1.size();
		StringBuilder number = new StringBuilder();
		for (int i = 0; i < digitsCount; i++) {
			String factorial = NumberUtil.getFactorialUsingMultiplication(digitsCount - i - 1);
			int index = Integer.parseInt(getQuotientByDividing(String.valueOf(location), factorial));
			number.append(String.valueOf(digits1.get(index)));
			digits1.remove(index);
			location = location - index * Integer.parseInt(factorial);
		}
		return number.toString();
	}

	public static boolean isPermutation(int m, int n) {
		int[] arr = new int[10];

		int temp = n;
		while (temp > 0) {
			arr[temp % 10]++;
			temp /= 10;
		}

		temp = m;
		while (temp > 0) {
			arr[temp % 10]--;
			temp /= 10;
		}

		for (int i = 0; i < 10; i++) {
			if (arr[i] != 0) {
				return false;
			}
		}
		return true;
	}

	public static String subtractTwoNumbers(String num1, String num2) {
		String result = "";
		int length1 = num1.length();
		int length2 = num2.length();
		if (length1 == length2) {
			result = subTractTwoNumbersOfSameLength(num1, num2, length1);
		} else {
			result = subTractTwoNumbersOfDifferentLength(num1, num2, length1, length2);
		}
		return result;
	}

	private static String subTractTwoNumbersOfDifferentLength(String num1, String num2, int length1, int length2) {
		String result = "";
		StringBuilder newNum = new StringBuilder();
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			result = subTractTwoNumbersOfSameLength(num1, newNum.toString(), length1);
		} else {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			result = subTractTwoNumbersOfSameLength(newNum.toString(), num2, length2);
		}
		return result;
	}

	public static boolean isResultPositive(String num1, String num2, int length) {
		for (int i = 0; i < length; i++) {
			if (num1.charAt(i) > num2.charAt(i))
				return true;
			if (num1.charAt(i) < num2.charAt(i))
				return false;
			else {
				continue;
			}
		}
		return true;
	}

	private static boolean isResultPositive(String num1, String num2, int length1, int length2) {
		StringBuilder newNum = new StringBuilder();
		if (length1 > length2) {
			for (int i = 0; i < length1 - length2; i++) {
				newNum.append("0");
			}
			newNum.append(num2);
			return isResultPositive(num1, newNum.toString(), length1);
		} else if (length2 > length1) {
			for (int i = 0; i < length2 - length1; i++) {
				newNum.append("0");
			}
			newNum.append(num1);
			return isResultPositive(newNum.toString(), num2, length2);
		} else
			return isResultPositive(num1, num2, length1);
	}

	private static String subTractTwoNumbersOfSameLength(String num1, String num2, int length) {
		// subtract num2 from num1(num1 -num2)
		StringBuilder result = new StringBuilder();
		int carry = 0;
		// if result is not positive then swap numbers to make it positive
		boolean resultPositive = isResultPositive(num1, num2, length);
		if (resultPositive == false) {
			String temp = num1;
			num1 = num2;
			num2 = temp;
		}
		for (int i = length - 1; i >= 0; i--) {
			int temp = 0;
			int digit1 = num1.charAt(i) - 48;
			int digit2 = num2.charAt(i) - 48;
			if (digit1 >= digit2) {
				if (carry == 1) {
					digit1--;
					carry = 0;
					// when digit1(before subtracting the carry) and digit2 are
					// equal
					if (digit1 + 1 == digit2) {
						digit1 += 10;
						carry = 1;
					}
				}
				temp = digit1 - digit2;
			} else {
				if (carry == 0) {
					carry = 1;
					digit1 += 10;
				} else {
					digit1 = digit1 - 1 + 10;
				}
				temp = digit1 - digit2;
			}
			result.append(temp);
		}
		result = removeLeadingZeroes(result);
		if (resultPositive == false) {
			result.append("-");
		}
		result = result.reverse();
		return result.toString();
	}

	public static String getQuotientByDividing(String dividend, String divisor) {
		String[] divisionResult = divideTwoNumbers(dividend, divisor);
		return divisionResult[0];
	}

	public static String getRemainderByDividing(String dividend, String divisor) {
		String[] divisionResult = divideTwoNumbers(dividend, divisor);
		return divisionResult[1];
	}

	public static String[] divideTwoNumbers(String dividend, String divisor) {
		StringBuilder result = new StringBuilder();
		boolean done = false;
		int length = divisor.length();
		int index = length;
		int dividendLength = dividend.length();
		if (false == isResultPositive(dividend, divisor, dividendLength, length))
			return new String[] { "0", dividend };
		String temp = dividend.substring(0, length);
		while (done == false) {
			while (false == isResultPositive(temp, divisor, temp.length(), length)) {
				result.append("0");
				if (index >= dividendLength) {
					break;
				}
				temp = temp + dividend.substring(index, index + 1);
				index++;
			}
			int count = 0;
			while (true == isResultPositive(temp, divisor, temp.length(), length)) {
				temp = subtractTwoNumbers(temp, divisor);
				count++;
			}
			if (temp.equalsIgnoreCase("0")) {
				temp = "";
			}
			if (count > 0) {
				result.append(count);
			}
			if (index >= dividendLength) {
				done = true;
				break;
			} else {
				temp = temp + dividend.substring(index, index + 1);
				index++;
			}

		}
		return new String[] { removeLeadingZeroes(result.reverse()).reverse().toString(), temp };
	}

	private static StringBuilder removeLeadingZeroes(StringBuilder result) {
		int i = result.length() - 1;
		for (i = result.length() - 1; i > 0; i--) {
			if (result.charAt(i) != '0') {
				break;
			}
		}
		result.replace(i + 1, result.length(), "");
		return result;
	}

	public static long[] getDigitsInNumber(long number) {
		int numOfDigits = getNumOfDigits(number);
		long[] digits = new long[numOfDigits];
		if (number == 0) {
			digits[0] = 0;
			return digits;
		}
		int i = numOfDigits - 1;
		while (number > 0) {
			digits[i--] = number % 10;
			number = number / 10;
		}
		return digits;
	}

	public static long[] getDigitsCountsInNumber(long number) {
		long[] digits = new long[10];
		if (number == 0) {
			digits[0] = 1;
			return digits;
		}
		while (number > 0) {
			digits[(int) (number % 10)]++;
			number = number / 10;
		}
		return digits;
	}

	public static long[] getAllCircularRotations(Long number) {
		int digitsinNumber = getNumOfDigits(number);
		long[] numbers = new long[digitsinNumber];
		long tenthPower = (long) Math.pow(10, digitsinNumber - 1);
		long numberCopy = number;
		for (int i = 0; i < digitsinNumber; i++) {
			long num1 = numberCopy % tenthPower;
			long num2 = numberCopy / tenthPower;
			numberCopy = num1 * 10 + num2;
			numbers[i] = numberCopy;
		}
		return numbers;
	}

	public static boolean isAllDigitsNine(long[] digits) {
		for (int i = 0; i < digits.length; i++)
			if (digits[i] != 9)
				return false;
		return true;
	}

	public static long getNextPalindrome(long number) {
		long[] digits = getDigitsInNumber(number);
		int len = digits.length;
		if (len == 1 && digits[0] != 9)
			return digits[0] + 1;
		if (isAllDigitsNine(digits)) {
			int num = 10;
			for (int i = 0; i < len - 1; i++) {
				num *= 10;
			}
			num += 1;
			return num;
		}
		int mid = len / 2;
		int i = mid - 1;
		int j = len % 2 == 1 ? (mid + 1) : mid;
		while (i >= 0 && digits[i] == digits[j]) {
			i--;
			j++;
		}
		boolean isleftSmall = false;
		if (i < 0 || digits[i] < digits[j]) {
			isleftSmall = true;
		}
		if (!isleftSmall) {
			while (i >= 0) {
				digits[j++] = digits[i--];
			}
		} else {
			long carry = 1l;
			i = mid - 1;
			if (len % 2 != 0) {
				digits[mid] += carry;
				carry = digits[mid] / 10;
				digits[mid] = digits[mid] % 10;
				j = mid + 1;
			} else {
				j = mid;
			}
			while (i >= 0) {
				digits[i] += carry;
				carry = digits[i] / 10;
				digits[i] = digits[i] % 10;
				digits[j++] = digits[i--];
			}
		}
		long ret = 0l;
		int x = 0;
		for (x = 0; x < len - 1; x++) {
			ret += digits[x];
			ret *= 10;
		}
		ret += digits[x];
		return ret;
	}

	public static boolean doesNumberContainEvenNumber(Long number) {
		if (number == 0)
			return true;
		while (number > 0) {
			long num = number % 10;
			number = number / 10;
			if (num % 2 == 0)
				return true;
		}
		return false;
	}

	/* public static long getNextPrime */
}
