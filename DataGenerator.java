import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Generates a specified number of permutations (1 per line) 
 * of a random number of integers
 *
 * The length is between 3 and 9.
 */

public class DataGenerator {
	private static int seed = 4242; // change the seed to get different data
	private static Random r = new Random(seed);

        public static String arrayToString(List<Integer> arr) { //Borrowed from https://www.geeksforgeeks.org/convert-array-of-integers-to-comma-separated-string/#
            // Variable to store the answer
    	    StringBuilder answer = new StringBuilder();
            int N = arr.size();
	    // Iterate over all the elements in arr[]
            for (int i = 0; i < N; i++) {
            	answer.append(arr.get(i));
	        if (i < N - 1) {
        	    answer.append(",");
                } 
            }
            return answer.toString();
        } 

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length < 3) {
			System.out.println(
					"Please run with three command line arguments: output file name and the number of items");
			System.exit(0);
		}
		String outFileName = args[0];
		int n = Integer.parseInt(args[1]);
		double rate = Double.parseDouble(args[2]);
//		double rate = 1.0 / 500.0;
		Random rand = new Random();
		
		PrintWriter out = new PrintWriter(outFileName);
		
		for(int i = 0; i < n; ++i) {
			double expRandom = -Math.log(1 - rand.nextDouble()) / rate;
			int m = (int) Math.ceil(expRandom);
			// generate permutation
			
			List<Integer> list = new ArrayList<>();

			for(int j = 1; j <= m; ++j) { //Create numbers to permutae
			    list.add(j);
			}
			
			Collections.shuffle(list); //Maybe should just do the printing in the previous loop?
			out.println(arrayToString(list));

//			for(int j = 0; i < m; i++){
//				out.print(list.get(j)+(j<m-1 ? ",":""));
//			}
//			out.print("\n");
		}
		out.close();
	}
	


}
