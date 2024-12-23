import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.lang.Math;

// References: https://www.programiz.com/dsa/bucket-sort

// To run on a single core, compile and then run as:
// taskset -c 0 java GroupN
// To avoid file reading/writing connections to the server, run in /tmp 
// of your lab machine.

public class Group1 {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		if (args.length < 2) {
			System.out.println(
					"Running tests since input and output file names not specified");
			SortingCompetitionComparator.runComparatorTests();
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];
		
		// Uncomment to test comparator methods

		int [][] data = readData(inputFileName); // read data as strings
		
		int [][] toSort = data.clone(); // clone the data

		sort(toSort); // call the sorting method once for JVM warmup
		
		toSort = data.clone(); // clone again

		Thread.sleep(10); // to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();

		sort(toSort); // sort again

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(toSort, outFileName); // write out the results

	}


	private static int [][] readData(String inputFileName) throws FileNotFoundException {
		ArrayList<int[]> input = new ArrayList<>();
		Scanner in = new Scanner(new File(inputFileName));
		// int lenLongestString = 0;

		while (in.hasNext()) {
			String str = in.next();
			input.add(Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray());
		}

		in.close();

		// the string array is passed just so that the correct type can be created
		return input.toArray(new int[0][]);
	}

	private static void sort(int [][] toSort) {
		Arrays.sort(bucket(toSort), new SortingCompetitionComparator());
		// bucket(toSort);
	}

	private static int[][] bucket(int[][] toBucket) {
		int r1size = 400, r2size = 17000/*r1size*r1size*/, r3size = Math.min(r2size*r1size, 2000000);
	
		// makes linked list arrays for all three rounds and instantiate
		LinkedList<int[]>[] round1 = new LinkedList[r1size];
		for (int i = 0; i < round1.length; i++) {
			round1[i] = new LinkedList<int[]>();
		}
		LinkedList<int[]>[]  round2 = new LinkedList[r2size];
		for (int i = 0; i < round2.length; i++) {
			round2[i] = new LinkedList<int[]>();
		}

		for (int[] entry : toBucket) {
			round1[Math.min(entry[0],r1size)-1].add(entry);
		}
		int pointer = 0;
		for (int i = 0; i < r1size; i++) {
			for (int[] entry : round1[i]) {
				if(entry.length > 1 ) {
					round2[Math.min(entry[1]*(i+1)/2,r2size-1)].add(entry);
					// take our estimate for placement or put in last cell of the array (min fcn)
					// entry[1]*(i+1)/2
						// entry[1] : value at second spot in int[]
						// i : location from last bucket loop, (i+1) grantees index is in bounds
						// "/2" makes the distribution of values in the third round denser
							// changing this number causes the output to fail, think we are doing division wrong. I hate modulo, couldn't get floor to work
					// want to make overall number smaller so we can then shorten the array length for round2
				} else toBucket[pointer++] = entry;
			}
		}

		if (toBucket.length >= 750000) {
			LinkedList<int[]>[] round3 = new LinkedList[r3size];
			for (int i = 0; i < round3.length; i++) {
				round3[i] = new LinkedList<int[]>();
			}

			for (int i = 1; i < r2size; i++) {
				for (int[] entry : round2[i]) {
					if(entry.length > 2 ) {
						round3[Math.min(entry[2]*i*(1/2),r3size-1)].add(entry);  // keep at 1/2 (tried: 1, 1/4, 3/4)
					} else round3[1].add(entry);
				}
			}

			for (int i = 0; i < r3size; i++) {
				for (int[] entry : round3[i]) {
					toBucket[pointer++] = entry;
				}
			}
		} else {
			for (int i = 0; i < r2size; i++) {
				for (int[] entry : round2[i]) {
					toBucket[pointer++] = entry;
				}
			}
		}
		return toBucket;
	}

	private static class SortingCompetitionComparator implements Comparator<int []> {
		
		@Override
		public int compare(int [] seq1, int [] seq2) {
			// looking for different elements in the same positions
			for (int i = 0; i < seq1.length && i < seq2.length ; ++i) {
				int diff = seq1[i] - seq2[i];
				if (diff != 0) return diff;
			}
			
			// two sequences are identical:
			if (seq1.length == seq2.length) return 0;
			
			// one sequence is a prefix of the other:
			
			// comparing even values:
			int seq1_evens = 0;
			for (int i = 0; i < seq1.length; ++i) {
				if (seq1[i] % 2 == 0) seq1_evens++;
			}
			
			int seq2_evens = 0;
			for (int i = 0; i < seq2.length; ++i) {
				if (seq2[i] % 2 == 0) seq2_evens++;
			}
			
			int diff = seq1_evens - seq2_evens;
			if (diff != 0) return diff; 
			
			// return the negated difference of odds 
			return (seq2.length - seq2_evens) - (seq1.length - seq1_evens);
		}



		public static void runComparatorTests() {
			int [] arr1 = {1, 3, 2};
			int [] arr2 = {1, 2, 3};
			System.out.println("Comparing arr1 and arr2");
			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr2));	
			System.out.println((new SortingCompetitionComparator()).compare(arr2, arr1));
			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr1));
			
			int [] arr3 = {1, 3, 2, 5, 4};
			
			System.out.println("Comparing arr1 and arr3");
			// arr3 should be larger:
			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr3));
			System.out.println((new SortingCompetitionComparator()).compare(arr3, arr1));
			
			int [] arr4 = {1, 3, 2, 7, 6, 5, 4};
			
			System.out.println("Comparing arr1 and arr4");
			// arr1 should be larger since they have the same number of evens, but the number
			// of odds is higher in arr4, and the comparison goes the opposite way:
			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr4));
			System.out.println((new SortingCompetitionComparator()).compare(arr4, arr1));
			
			System.out.println("Comparing arr4 and arr3");
			System.out.println((new SortingCompetitionComparator()).compare(arr4, arr3));
			
			int [] arr5 = {1, 3, 2, 5, 6, 7, 4};
			
			System.out.println("Comparing arr1 and arr5");

			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr5));
			System.out.println((new SortingCompetitionComparator()).compare(arr5, arr1));			
			
			System.out.println("Comparing arr5 and arr3");
			System.out.println((new SortingCompetitionComparator()).compare(arr5, arr3));
			
			System.out.println("Comparing arr5 and arr4");
			System.out.println((new SortingCompetitionComparator()).compare(arr5, arr4));
			
			int [] arr6 = {1, 3, 2, 6, 5, 7, 4};
			System.out.println("Comparing arr1 and arr6");
			System.out.println((new SortingCompetitionComparator()).compare(arr1, arr6));
			
			System.out.println("Comparing arr3 and arr6");
			System.out.println((new SortingCompetitionComparator()).compare(arr3, arr6));
			
			System.out.println("Comparing arr5 and arr6");
			System.out.println((new SortingCompetitionComparator()).compare(arr5, arr6));
			
			
			System.out.println("Comparing arr4 and arr6");
			System.out.println((new SortingCompetitionComparator()).compare(arr4, arr6));
			 
		}
	}
	
	private static void writeOutResult(int [][] sorted, String outputFilename) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputFilename);
		for (int [] s : sorted) {
			for (int i = 0; i < s.length; ++i) {
				out.print(s[i]+(i<s.length-1?",":""));
			}
			out.println();
		}
		out.close();
	}
}

