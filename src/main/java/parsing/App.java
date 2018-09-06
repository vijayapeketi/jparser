package parsing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		JsonFactory jsonFactory = new JsonFactory(); // or, for data binding,
														// org.codehaus.jackson.mapper.MappingJsonFactory
		String file = "users-1.json";
		int size = 1000;
		int[] age = new int[size];
		int[] regyear = new int[size];
		int[] frndsCount = new int[size];
		float[] balance = new float[size];
		int[] unreadMsg = new int[size];

		try {
			// JsonParser jp = new JsonFactory().createParser(new File(file));
			// JsonParser jp = jsonFactory.createJsonParser(file);
			MappingJsonFactory f = new MappingJsonFactory();
			org.codehaus.jackson.JsonParser jp = f.createJsonParser(new File(
					file));
			// System.out.println("first::"+jp.nextToken());
			int count = 0;
			while (jp.nextToken() != JsonToken.END_ARRAY) {
				jp.nextToken();
				// read the record into a tree model,
				// this moves the parsing position to the end of it
				JsonNode node = jp.readValueAsTree();
				// And now we have random access to everything in the object
				//if (node != null)
				{
				if (node.get("age") != null) {

						age[count] = node.get("age").getIntValue();

					}
				if ( node.get("balance") != null) {

					String value = (String) (node.get("balance").getValueAsText()).replace(",", "").replace("$", "").trim();
					balance[count] = Float.parseFloat(value);
				}
				
				if (node.get("registered")!= null){
					 
					 regyear[count]= Integer.parseInt(node.get("registered").getTextValue().substring(0,4));
					
				 }	
				if (node.get("friends")!= null) {
					try {
						JsonNode arrNode = node.get("friends");
						int cnt  =0;
						if (arrNode.isArray()) {
						    for (final JsonNode objNode : arrNode) {
						    	cnt++;
						    }
						}
						String strs =  "";//node.get("friends").get
						
						
						//System.out.println("friends"+cnt);
						frndsCount[count] =cnt;
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			if (node.get("isActive")!= null && node.get("gender")!= null && node.get("greeting")!= null ) {
					boolean active = false;
					if (node.get("isActive").getBooleanValue()== true)
						active = true;
					if(active){
						if (node.get("gender").getTextValue().equals("female")) {
							String message = node.get("greeting").getTextValue();
							int cnt = Integer.parseInt(message.substring(message.indexOf("You have ")+9, message.indexOf("unread")).trim());							
							unreadMsg[count] =cnt;
							}
					}

				}
				
				}
				count++;
			//	System.out.println(count);
				if (count == size) {
					printsummary(age, regyear, frndsCount, balance, unreadMsg);
					count = 0;
					age = new int[size];
					regyear = new int[size];
					frndsCount = new int[size];
					balance = new float[size];
					unreadMsg = new int[size];
				}
			}
	
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // or URL, Stream, Reader, String, byte[]
	}

	private static int getValue(JsonParser jp) {
		try {
			System.out.println("getBooleanValue" + jp.getBooleanValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {
			System.out.println("IntValue" + jp.getIntValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {
			System.out.println("DoubleValue" + jp.getDoubleValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {
			System.out.println("LongValue" + jp.getLongValue());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {
			System.out.println("TextValue" + jp.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return 0;
	}

	private static void printsummary(int[] age, int[] regyear,
			int[] frndsCount, float[] balance, int[] unreadMsg) {
		System.out.println("PrintSummary::");
		StringBuilder bldr = new StringBuilder();
		bldr.append("Users registered in each Year ::" + count(regyear) + "\n");
		bldr.append("Median for Number of Friends::"
				+ calculateMedian(frndsCount) + "\n");
		bldr.append("Median age for Users::" + calculateMedian(age) + "\n");
		bldr.append("Mean Balance Amount::" + calculateMean(balance) + "\n");
		bldr.append("Mean for number of Unread messages for Active females::"
				+ calculateMean(unreadMsg) + "\n");
		System.out.println(bldr);

	}

	private static String calculateMean(float[] arr) {
		float sum = 0;

		// Sum all values in our array
		for (int i = 0; i < arr.length; i++) {
			sum = sum + arr[i];
		}

		// Calculate mean
		double mean = ((double) sum) / ((double) arr.length);

		return mean + "";
	}

	private static String count(int[] regyear) {
		HashMap map = new HashMap();
		for (int i = 0; i < regyear.length; i++) {
			if (regyear[i] != 0)
				if (map.get(regyear[i]) == null)
					map.put(regyear[i] + "", 1);
				else {
					System.out.println(regyear[i] + "::"+map.get(regyear[i]));
					map.put(regyear[i], (int) map.get(regyear[i] + "") + 1);
				}
			;

		}
		String count = map + "";
		return count;
	}

	/**
	 * This method computes the mean of the values in the input array.
	 * 
	 * @param arr
	 *            - an array of ints
	 * @return mean - the mean of the input array
	 */
	public static double calculateMean(int[] arr) {
		arr = remZeros(arr);
		int sum = 0;

		// Sum all values in our array
		for (int i = 0; i < arr.length; i++) {
			sum = sum + arr[i];
		}

		// Calculate mean
		double mean = ((double) sum) / ((double) arr.length);

		return mean;
	}

	/**
	 * This method computes the median of the values in the input array.
	 * 
	 * @param arr
	 *            - an array of ints
	 * @return median - the median of the input array
	 */
	public static double calculateMedian(int[] tmp) {
		int[] arr = remZeros(tmp);
		if (arr.length == 0)
			return 0;
	//	print(arr);

		// Sort our array
		int[] sortedArr = bubbleSort(arr);
		// print(sortedArr);

		double median = 0;

		// If our array's length is even, then we need to find the average of
		// the two centered values
		if (arr.length % 2 == 0) {
			int indexA = (arr.length - 1) / 2;
			int indexB = arr.length / 2;

			median = ((double) (sortedArr[indexA] + sortedArr[indexB])) / 2;
		}
		// Else if our array's length is odd, then we simply find the value at
		// the center index
		else {
			int index = (sortedArr.length - 1) / 2;
			median = sortedArr[index];
		}

		// Print the values of the sorted array
		for (int v : sortedArr) {
			// System.out.println(v);
		}

		return median;
	}

	private static void print(int[] arr) {
		StringBuilder bldr = new StringBuilder();
		for (int i = 0; i < arr.length; i++)
			bldr.append(arr[i] + " ");
		System.out.println(bldr);

	}

	/**
	 * This program returns a sorted version of the input array.
	 * 
	 * @param arr
	 * @return
	 */
	public static int[] bubbleSort(int[] arr) {

		// We must sort the array. We will use an algorithm called Bubble Sort.
		boolean performedSwap = true;
		int tempValue = 0;

		// If we performed a swap at some point in an iteration, this means that
		// array
		// wasn't sorted and we need to perform another iteration
		while (performedSwap) {
			performedSwap = false;

			// Iterate through the array, swapping pairs that are out of order.
			// If we performed a swap, we set the "performedSwap" flag to true
			for (int i = 0; i < arr.length - 1; i++) {
				if (arr[i] > arr[i + 1]) {
					tempValue = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = tempValue;

					performedSwap = true;
				}
			}
		}

		return arr;
	}

	private static int[] remZeros(int[] array) {
		int targetIndex = 0;
		for (int sourceIndex = 0; sourceIndex < array.length; sourceIndex++) {
			if (array[sourceIndex] != 0)
				array[targetIndex++] = array[sourceIndex];
		}
		int[] newArray = new int[targetIndex];
		System.arraycopy(array, 0, newArray, 0, targetIndex);
		return newArray;
	}

}
