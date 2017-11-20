import java.util.*;
import java.io.*;

public class Scheduler {
	private int studentsAssigned = 0;
	public static int studentPrefsValue = 0;
	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Invalid input");
		} else {
			Scheduler classScheduler = new Scheduler();
			long start = System.currentTimeMillis();
			classScheduler.schedule(args[0], args[1]);
			long end = System.currentTimeMillis();
			System.out.println("Total running time: " + (end - start) + " milliseconds");
		}
	}

	// read in input, process, and output
	public void schedule(String inputFilePath, String constraintFilePath) {
		// class of different level could be assigned at same time
		// already have classes and rooms
		int numScheduled = 0;
		Map<String, List<Class>> classes = new HashMap<>(); // key: department, value: list of classes under the department, order by increasing level
		Map<String, List<Room>> rooms = new HashMap<>(); // key: building, value: list of rooms, sort by decreasing order of size
		Map<String, Set<String>> buildings = new HashMap<>(); // key: building, value: list of departments
		Set<Integer> classesRead = new HashSet<>(); // id of classes processed
		StudentPref sp = new StudentPref();
		//Map<String, Teacher> teachers = new HashMap<>();  //key: teacher name, value: Teacher object
		Constraints constraints = readConstraints(constraintFilePath);
		Time dayTime = constraints.getTime();
		Map<String, Integer> allRoomsCap = constraints.getRoomCaps();
		int numClasses = readInput(allRoomsCap, inputFilePath, classes, rooms, buildings, dayTime, sp, classesRead);
		sp.writePref();
		//sp.readPref("student_prefs.txt");
		sp.invertPrefs();

		BufferedWriter writer = null;
		try {
		   	writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("schedule.txt"), "utf-8"));
		    writer.write("Course"+"\t\t"+"Room"+"\t\t"+"Teacher"+"\t\t"+"Time"+"\t\t"+"Students");
		    writer.newLine();
		    for(String building : buildings.keySet()){
					List<Room> rs = rooms.get(building);
					Set<String> ds = buildings.get(building);
					int roomIndex = 0;
					for (int level = 0; level <= 5; level++) {
						for (String dept : ds) {
							List<Class> cs = classes.get(dept);
							int classIndex = 0;
							Room r = rs.get(roomIndex);
							while (classIndex < cs.size() && !r.isFull()) {
								Class c = cs.get(classIndex);
								if (c.getLevel() <= level && c.getTime() == null) {
									if(r.addClass(c)) {
										writer.write(Integer.toString(c.getId()));
										writer.write('\t');
										if(r.getNumber().equals("")) {
											writer.write("-1");
										} else {
											writer.write(building+" "+r.getNumber());
										}
										writer.write('\t');
										writer.write(Integer.toString(c.getTeacher().getName()));
										writer.write('\t');
										writer.write(c.getMeetDate()+" ");
										String start = "", end = "";
										if (c.getTime().getStart()%60 == 0) {
											start = Integer.toString(c.getTime().getStart()/60) + ":" + Integer.toString(c.getTime().getStart()%60) + "0";
										} else {
											start = Integer.toString(c.getTime().getStart()/60) + ":" + Integer.toString(c.getTime().getStart()%60);
										}
										if (c.getTime().getEnd()%60 == 0) {
											end = Integer.toString(c.getTime().getEnd()/60) + ":" + Integer.toString(c.getTime().getEnd()%60) + "0";
										} else {
											end = Integer.toString(c.getTime().getEnd()/60) + ":" + Integer.toString(c.getTime().getEnd()%60);
										}
										writer.write(start+" ");
										writer.write(end);
										writer.write('\t');
										List<Integer> stdPre = sp.getStdList(c.getId());
										if(stdPre != null) {
											int stdNum = 0;
											while(stdNum < r.getSize() && stdNum < stdPre.size()){
												int stuId = stdPre.get(stdNum);
												c.addStudent(stuId);
												writer.write(Integer.toString(stuId) + " ");
												stdNum ++;
											}
											while(stdNum < stdPre.size()) {
												sp.removeClass(stdPre.get(stdNum), c);
												stdNum ++;
											}
										}
										writer.newLine();
									}
								}
								classIndex++;
							}

							if (roomIndex < rs.size() - 1) {
								roomIndex++;
							}
						}
					}
			}
			for(int id = 0; id < StudentPref.NUM_STUDENTS; id++){
				if(!sp.hasClass(id)){
					studentsAssigned ++;
				}
			}
			writer.write("Total number of students assigned is: " + Integer.toString(studentsAssigned) + " student preference value is: " + studentPrefsValue);//Integer.toString(studentPrefsValue));
		} catch (IOException ex) {
		  	System.out.println("Create and write file failed");
		} finally {
		   try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
	}

		// when there's at least some time slot in a room left and there's at least a class that has not been scheduled
		// when putting a class into a room, consider the following:
		// 1. No two classes should be put into the same time slot in the same room
		// 2. No two classes should be put into the same time slot if they are taught by the same teacher
		// 3. Try not putting a class into a room when the room size is less than class cap
		// 4. 100 classes in the same building should not overlap
		// 5. classes of the same department should not overlap if they are not 100 level

			// constraints:
			// 1. class cap (o)
			// 2. class time overlap (not necessarily fit into a predefined time slot) (x)
			// 3. classes under the same department should be scheduled in different time slots (o)
			// 4. there could be multiple meetings for a single class per week
			// 5. avoid putting classes of the same level in the same time slots (o)
			// 6. avoid putting classes at different colleges at the same time (x)
			// 7. classes under the same department should be put in the same buildings (o)
			// 8. time range 8:00am - 10:00pm (o)
			// 9. time slots have different lengths
		//}
	//}

	public int readInput(Map<String, Integer> roomcap, String filePath, Map<String, List<Class>> classes, Map<String, List<Room>> rooms, Map<String, Set<String>> buildings, Time dayTime, StudentPref sp, Set<Integer> classesRead) {
		int totalClasses = 0;
		String delim = ",";
		BufferedReader reader = null;
		String line = "";

		try {
			reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(delim);
				totalClasses += processLine(roomcap, fields, classes, rooms, buildings, classesRead, dayTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (String dept : classes.keySet()) {
			List<Class> classList = classes.get(dept);
			Collections.sort(classList, (a, b) -> (a.getLevel() - b.getLevel()));
		}

		for (String building : rooms.keySet()) {
			List<Room> roomList = rooms.get(building);
			Collections.sort(roomList, (a, b) -> (b.getSize() - a.getSize()));
		}

		sp.generatePrefs(classes);

		return totalClasses;
	}

	public int processLine(Map<String, Integer> allRoomsCap,String[] fields, Map<String, List<Class>> classes, Map<String, List<Room>> rooms, Map<String, Set<String>> buildings, Set<Integer> classesRead, Time dayTime) {
		if (!Character.isDigit(fields[1].charAt(0))) {
			return 0;
		}
		int id = Integer.parseInt(fields[1]);
		if (classesRead.contains(id)) {
			return 0;
		}
		//int cap = Integer.parseInt(fields[fields.length - 14]);
		//int roomCap = Integer.parseInt(fields[fields.length - 1]);
		String subject = fields[2];
		// check if instructor id is empty
		if (fields[11].length() == 0) {
			return 0;
		}
		classesRead.add(id);
		int level = Integer.parseInt(fields[4]), teacherName = Integer.parseInt(fields[11]);
		//tem.out.println(Arrays.toString(fields));
		if (fields.length < 21) {
			return 0;
		}
		String room = fields.length == 21 ? "" : fields[21];
		String building = fields[20];
		String startTime = fields[13], endTime = fields[16];
		String roomName = building + room;
		int roomCap = 0;
		if (allRoomsCap.containsKey(roomName)){
			roomCap = allRoomsCap.get(roomName);
		}
		int length = convertTimeToMinute(endTime) - convertTimeToMinute(startTime) + 10;
		if (length == 0) {
			//System.out.println("invalid class: " + id + ", discarded");
			return 0;
		}

		Class klass = new Class(id, level, length, subject, new Teacher(teacherName));
		if (!classes.containsKey(subject)) {
			classes.put(subject, new ArrayList<>());
		}
		klass.getTeacher().classes().add(klass);
		classes.get(subject).add(klass);

		// if(! teachers.containsKey(teacherName)) {
		// 	Teacher teacher = new Teacher(teacherName);
		// 	teacher.classes().add(klass)
		// 	teachers.put(teacherName, teacher);
		// }else{
		// 	Teacher temp = teachers.get(teacherName);
		// 	temp.classes().add(klass);
		// }

		if (!rooms.containsKey(building)) {
			rooms.put(building, new ArrayList<>());
		}

		if (!rooms.get(building).contains(room)) {
			rooms.get(building).add(new Room(room, roomCap, dayTime.getStart(), dayTime.getEnd()));
		}

		if (!buildings.containsKey(building)) {
			buildings.put(building, new HashSet<>());
		}
		if (!buildings.get(building).contains(subject)) {
			buildings.get(building).add(subject);
		}

		return 1;
	}

	public int convertTimeToMinute(String time) {
		String[] hours = time.split(":");
		if (hours.length < 2) return 0;
		String[] minutes = hours[1].split("\\s+");
		String[] hourNum = hours[0].split("\\s+");
		int hour = Integer.parseInt(hourNum[hourNum.length-1]) % 12;
		int minute = Integer.parseInt(minutes[0]);
		if (minutes[1].equalsIgnoreCase("PM")) {
			hour += 12;
		}
		return hour * 60 + minute;
	}
//here
	public Constraints readConstraints(String filePath) {
		Constraints constraints = new Constraints();
		BufferedReader reader = null;
		String line = "";
		String startTime = "", endTime = "";

		try {
			reader = new BufferedReader(new FileReader(filePath));
			line = reader.readLine();
			String[] pieces = line.split(" ");
			while (!pieces[0].equals("start")) {
				line = reader.readLine();
				pieces = line.split(" ");
			}

			startTime = pieces[2] + " " + pieces[3];
			line = reader.readLine();
			pieces = line.split(" ");
			endTime = pieces[2] + " " + pieces[3];
			constraints.setTimeRange(new Time(convertTimeToMinute(startTime), convertTimeToMinute(endTime)));

			line = reader.readLine();
			pieces = line.split("\t");
			int numLines = Integer.parseInt(pieces[1]);
			Map<String, Integer> roomCaps = new HashMap<>();
			for (int i = 0; i < numLines; i++) {
				line = reader.readLine();
				pieces = line.split("\t");
				roomCaps.put(pieces[0], Integer.parseInt(pieces[1]));
			}
			constraints.setRoomCaps(roomCaps);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return constraints;
	}
}

class Constraints {
	Time timeRange;
	Map<String, Integer> roomCaps;

	public Constraints() {

	}

	public Constraints(Time timeRange, Map<String, Integer> roomCaps) {
		this.timeRange = timeRange;
		this.roomCaps = roomCaps;
	}

	public void setTimeRange(Time timeRange) {
		this.timeRange = timeRange;
	}

	public void setRoomCaps(Map<String, Integer> roomCaps) {
		this.roomCaps = roomCaps;
	}

	public Time getTime() {
		return timeRange;
	}

	public Map<String, Integer> getRoomCaps() {
		return roomCaps;
	}
}
