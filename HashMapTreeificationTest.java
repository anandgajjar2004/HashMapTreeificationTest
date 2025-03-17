package com.java8.stream;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HashMapTreeificationTest {

	static int currentBucketSize = 16;

	public static void main(String[] args) throws Exception {
		
		//Very Important Note
		// Please run this Java class with below VM argument 
		//--add-opens java.base/java.util=ALL-UNNAMED

		HashMap<Integer, String> map = new HashMap<>();

		// Forcing keys to always land in bucket 0 initially
		// Keys are selected so that key % bucket_size = 0
		// This means all keys initially fall into bucket 0
		/**
		 * | Key  | Bucket Size | Bucket Number |
		 * |------|------------|--------------|
		 * |  16  |     16     |      0       |
		 * |  32  |     16     |      0       |
		 * |  48  |     16     |      0       |
		 * |  64  |     16     |      0       |
		 * |  80  |     16     |      0       |
		 * |  96  |     16     |      0       |
		 * | 112  |     16     |      0       |
		 * | 128  |     16     |      0       |
		 */

		// Adding 8 elements that all go to bucket index 0
		putAndCheck(map, 16, "Value1");
		putAndCheck(map, 32, "Value2");
		putAndCheck(map, 48, "Value3");
		putAndCheck(map, 64, "Value4");
		putAndCheck(map, 80, "Value5");
		putAndCheck(map, 96, "Value6");
		putAndCheck(map, 112, "Value7");
		putAndCheck(map, 128, "Value8");

		// At this point, the bucket size will double from 16 to 32
		// The elements are redistributed based on the new bucket size
		
		/**	
		 * | Key  | Bucket Size | Bucket Number |
		 * |------|------------|--------------|
		 * |  16  |     32     |      16      |
		 * |  32  |     32     |      0       |
		 * |  48  |     32     |      16      |
		 * |  64  |     32     |      0       |
		 * |  80  |     32     |      16      |
		 * |  96  |     32     |      0       |
		 * | 112  |     32     |      16      |
		 * | 128  |     32     |      0       |		 
		 */

		// We need to add 4 more elements to bucket 0 to trigger another rehashing

		putAndCheck(map, 160, "Value10");
		putAndCheck(map, 192, "Value12");
		putAndCheck(map, 224, "Value14");
		putAndCheck(map, 256, "Value16");

		// Rehashing will occur, doubling bucket size from 32 to 64
		// Elements will now be re-mapped to the new bucket structure

		/**
		 * | Key  | Bucket Size | Bucket Number |
		 * |------|------------|--------------|
		 * |  16  |     64     |      16      |
		 * |  32  |     64     |      32      |
		 * |  48  |     64     |      48      |
		 * |  64  |     64     |      0       |
		 * |  80  |     64     |      16      |
		 * |  96  |     64     |      32      |
		 * | 112  |     64     |      48      |
		 * | 128  |     64     |      0       |
		 * | 160  |     64     |      32      |
		 * | 192  |     64     |      0       |
		 * | 224  |     64     |      32      |
		 * | 256  |     64     |      0       |
		 */

		// Now, we need to add 4 more elements to bucket 0
		// This will convert bucket 0 into a Red-Black Tree

		putAndCheck(map, 320, "Value20");
		putAndCheck(map, 384, "Value24");
		putAndCheck(map, 448, "Value28");
		putAndCheck(map, 512, "Value32");

		// At this point, bucket 0 has 8 elements, triggering treeification
		putAndCheck(map, 576, "Value35");

		// There you go. in console you will see the value of Is Red-Black Tree used ? will be True.

	}

	private static void putAndCheck(HashMap<Integer, String> map, int key, String value) throws Exception {
		System.out.print("Adding Key: " + key + "\tValue: " + value + "\t");
		map.put(key, value);

		int size = getBucketSize(map);

		if (size > currentBucketSize) {
			currentBucketSize = size;
			System.out.print(" 🔄 Rehashing happened! New Table Size: " + size);
		} else {
			System.out.print("");
		}

		checkIfRedBlackTreeUsed(map);
		System.out.println("");
	}

	// Reflection method to get HashMap's internal table size
	private static int getBucketSize(HashMap<?, ?> map) {
		try {
			Field tableField = HashMap.class.getDeclaredField("table");
			tableField.setAccessible(true);
			Object[] table = (Object[]) tableField.get(map);
			return (table != null) ? table.length : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static void checkIfRedBlackTreeUsed(Map<Integer, String> map) {
		try {
			Field tableField = HashMap.class.getDeclaredField("table");
			tableField.setAccessible(true);
			Object[] table = (Object[]) tableField.get(map);

			if (table == null) {
				System.out.print(" | HashMap bucket array not initialized yet.");
				return;
			}

			boolean treeUsed = false;

			for (Object bucket : table) {
				if (bucket != null) {
					Class<?> nodeClass = bucket.getClass();
					if (nodeClass.getName().contains("TreeNode")) {
						treeUsed = true;
						break;
					}
				}
			}
			
			System.out.print(" | Is Red-Black Tree used? " + treeUsed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}