import java.util.*;
import java.io.*;

class StudentPref {
	private final int NUM_PREFS = 4;
	private final int NUM_STUDENTS = 100;
	private Map<Integer, List<Integer>> prefs; // key: student id, value: class ids

	public StudentPref() {
		prefs = new HashMap<>();
	}

	public Map<Integer, List<Integer>> getPrefs() {
		return prefs;
	}

	public void readPref(String filePath) {
		BufferedReader reader = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader(filePath));
			line = reader.readLine();
			String[] firstLine = line.split("\\W+");
			int numStudents = Integer.parseInt(firstLine[1]);
			for (int i = 0; i < numStudents; i++) {
				line = reader.readLine();
				String[] pieces = line.split("\\W+");
				int[] nums = new int[5];
				for (int j = 0; j < pieces.length; j++) {
					nums[j] = Integer.parseInt(pieces[j]);
				}
				if (!prefs.containsKey(nums[0])) {
					prefs.put(nums[0], new ArrayList<>());
				}
				for (int j = 1; j < 5; j++) {
					prefs.get(nums[0]).add(nums[j]);
				}
			}
		} catch (IOException ex) {
			System.out.println("Read student pref failed");
		}
	}

	public void generatePrefs(Map<String, List<Class>> classes) {
		Set<Integer> ids = new HashSet<>();
		List<Integer> idList = new ArrayList<>();
		for (String dept : classes.keySet()) {
			for (Class klass : classes.get(dept)) {
				ids.add(klass.getId());
			}
		}
		idList.addAll(ids);
		int[] rands = new int[NUM_PREFS];
		for (int id = 1; id <= NUM_STUDENTS; id++) {
			prefs.put(id, new ArrayList<>());
			for (int i = 0; i < NUM_PREFS; i++) {
				rands[i] = (int)(Math.random() * idList.size());
				prefs.get(id).add(rands[i]);
			}
		}

	}
}