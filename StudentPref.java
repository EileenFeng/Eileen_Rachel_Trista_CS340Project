import java.util.*;
import java.io.*;

class StudentPref {
	private final int NUM_PREFS = 4;
	private final int NUM_STUDENTS = 2000;
	private Map<Integer, List<Integer>> prefs; // key: student id, value: class ids
	private Map<Integer, List<Integer>> invertedPrefs; // key: class id, value: student ids

	public StudentPref() {
		prefs = new HashMap<>();
		invertedPrefs = new HashMap<>();
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

	public void writePref() {
		BufferedWriter writer = null;
		try {
	   	writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream("student_prefs.txt"), "utf-8"));
	    writer.write("Students");
	    writer.write('\t');
	    writer.write('\t');
	    writer.write(Integer.toString(NUM_STUDENTS));
	    writer.newLine();
	    for (Integer studentId : prefs.keySet()) {
	    	writer.write(Integer.toString(studentId));
	    	List<Integer> prefList = prefs.get(studentId);
	    	writer.write('\t');
	    	int index = 0;
	    	while(index < prefList.size()) {
	    		writer.write(Integer.toString(prefList.get(index)) + " ");
	    		index ++;
	    	}
	    	writer.newLine();
	    } 
	  } catch (IOException ex) {
	    	System.out.println("Can't write!!!!!!!!!!!!!!!!!!");
	    } finally {
	    	try {
	    		writer.close();
	    	} catch (Exception ex) {
	    		System.out.println("Failed to close writer");
	    	}
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
		//int[] rands = new int[NUM_PREFS];
		for (int id = 1; id <= NUM_STUDENTS; id++) {
			prefs.put(id, new ArrayList<>());
			for (int i = 0; i < NUM_PREFS; i++) {
				int randId = (int)(Math.random() * idList.size());
				prefs.get(id).add(idList.get(randId));
			}
		}

	}

	public void removeClass(int id, Class klass) {
		List<Integer> pref = prefs.get(id);
		int classId = klass.getId();
		int index = -1;
		for (int i = 0; i < pref.size(); i++) {
			if (pref.get(i) == classId) {
				index = i;
			}
		}
		if (index != -1) {
			pref.remove(index);
		}
	}

	public void invertPrefs() {
		for (Integer student : prefs.keySet()) {
			List<Integer> classIds = prefs.get(student);
			for (Integer classId : classIds) {
				if (!invertedPrefs.containsKey(classId)) {
					invertedPrefs.put(classId, new ArrayList<>());
				}
				invertedPrefs.get(classId).add(student);
			}
		}
	}

	public Map<Integer, List<Integer>> getInvertedPref() {
		return invertedPrefs;
	}

	public List<Integer> getStdList(int classId) {
		return invertedPrefs.get(classId);
	}
}
