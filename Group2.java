import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * Issues i have noticed:
 * -I would be incredibly surprised if the issue is not in how they are setting up and taking down their results
 */

public class Group2 {
    private static final int K = 32; // Threshold for switching to insertion sort
    private static final int N = 128; // Threshold for switching to merge sort

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Usage: java Group6 <input-file> <output-file>");
            System.exit(0);
        }

        String inputFileName = args[0];
		String outFileName = args[1];

		int [][] arrays = readData(inputFileName); // read data as strings
		
		int [][] toSort = arrays.clone(); // clone the data

		long start = System.currentTimeMillis();

        hybridSort(toSort, 0, arrays.length - 1);

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(toSort, outFileName); // write out the results

        /*
        String inputFile = args[0];
        String outputFile = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            List<int[]> arrayList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] stringValues = line.split(",");
                int[] intValues = new int[stringValues.length];
                for (int i = 0; i < stringValues.length; i++) {
                    intValues[i] = Integer.parseInt(stringValues[i].trim());
                }
                arrayList.add(intValues);
            }

            int[][] arrays = arrayList.toArray(new int[0][]);

            long startTime = System.currentTimeMillis();
            hybridSort(arrays, 0, arrays.length - 1);
            long endTime = System.currentTimeMillis();

            System.out.println(endTime - startTime);
            writeOutResult(arrays, outputFile);
            
            // long elapsedTime = endTime - startTime;
            // // Write sorted arrays to output file
            // try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            //     for (int[] array : arrays) {
            //         writer.println(Arrays.toString(array));
            //     }
            // }
            // System.out.println("Elapsed time in milliseconds: " + elapsedTime);
            // Check if arrays are sorted correctly
            // if (isSorted(arrays)) {
            //     System.out.println("Arrays are sorted correctly.");
            // } else {
            //     System.out.println("Arrays are not sorted correctly.");
            // }

        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFile);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in input file.");
        }
        */
    }

    public static void hybridSort(int[][] arrays, int left, int right) {
        if (right - left <= K) {
            insertionSort(arrays, left, right);
        } else if (right - left <= N) {
            heapSort(arrays, left, right);
        } else {
            Arrays.parallelSort(arrays, left, right + 1, Group2::compareArrays);
        }
    }

    public static void insertionSort(int[][] arrays, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int[] key = arrays[i];
            int j = i - 1;
            while (j >= left && compareArrays(arrays[j], key) > 0) {
                arrays[j + 1] = arrays[j];
                j--;
            }
            arrays[j + 1] = key;
        }
    }

    public static void heapSort(int[][] arrays, int left, int right) {
        int n = right - left + 1;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arrays, n, i, left);
        }
        for (int i = n - 1; i > 0; i--) {
            int[] temp = arrays[left];
            arrays[left] = arrays[left + i];
            arrays[left + i] = temp;
            heapify(arrays, i, 0, left);
        }
    }

    private static void heapify(int[][] arrays, int n, int i, int left) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && compareArrays(arrays[left + l], arrays[left + largest]) > 0) {
            largest = l;
        }

        if (r < n && compareArrays(arrays[left + r], arrays[left + largest]) > 0) {
            largest = r;
        }

        if (largest != i) {
            int[] swap = arrays[left + i];
            arrays[left + i] = arrays[left + largest];
            arrays[left + largest] = swap;
            heapify(arrays, n, largest, left);
        }
    }

    public static int compareArrays(int[] array1, int[] array2) {
        for (int i = 0; i < Math.min(array1.length, array2.length); i++) {
            int comparison = Integer.compare(array1[i], array2[i]);
            if (comparison != 0) {
                return comparison;
            }
        }

        int evens1 = countEvens(array1);
        int evens2 = countEvens(array2);
        if (evens1 != evens2) {
            return Integer.compare(evens1, evens2);
        }

        int odds1 = array1.length - evens1;
        int odds2 = array2.length - evens2;
        return Integer.compare(odds2, odds1);
    }

    private static int countEvens(int[] array) {
        int count = 0;
        for (int value : array) {
            if (value % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    // public static boolean isSorted(int[][] arrays) {
    //     for (int i = 0; i < arrays.length - 1; i++) {
    //         if (compareArrays(arrays[i], arrays[i + 1]) > 0) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }

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

    private static int [][] readData(String inputFileName) throws FileNotFoundException {
		ArrayList<int[]> input = new ArrayList<>();
		Scanner in = new Scanner(new File(inputFileName));

		while (in.hasNext()) {
			String str = in.next();
			input.add(Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray());
		}

		in.close();

		return input.toArray(new int[0][]);
	}
}
