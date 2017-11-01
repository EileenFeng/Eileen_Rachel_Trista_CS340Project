import java.util.*;

class Room {
	private final String[] weekdays = {"M", "T", "W", "TH", "F"};
	private final String number;
	private final int size;
	private Map<String, Time> time;  // specify the free time for each day in a week
	private Map<String, Map<Time, Class>> classSchedule;  

	public Room(String number, int size, int startTime, int endTime) {
		this.number = number;
		this.classSchedule = new HashMap<>();
		for (int i = 0; i < 5; i++) {
			Time freeTime = new Time(startTime, endTime);
			this.time.put(weekdays[i], freeTime);
		}
		this.size = size;
	}

	public String getNumber() {
		return number;
	}

	public int getSize() {
		return size;
	}

// add time based on the start and end minutes of a class
// for each room, add class from the beginning (0 min) to the last minutes in order
	public boolean addClass(Class klass) {
		int len = klass.getLength();
		Teacher teacher = klass.getTeacher();
		int days = 0;
		if(len <= 60){ // if length of class smaller than 60, MWF
			days = 3;
		} else if(len > 60 && len <= 90) { // between 60 - 90, meet twice TTH
			days = 2;
		} else if(len > 90) { // otherwise meet once, F
			days = 1;
		}
		int interval = 5 - days;  // the number of days between meeting times each week
		// check if have time or conflict teacher
		// check teacher, the addTime function for teacher needs change: need to check for specific dates!
		for (int i = 0; i < 5; i += interval) {
		    Time temp = new Time(time.get(weekdays[i]).getStart(), time.get(weekdays[i]).getStart() + len);
			if(!teacher.addTime(weekdays[i], temp)){
				return false;
			}else if (len > time.get(weekdays[i]).getInterval()){
				return false;
			}
		}
		// schedule the class
		for (int i = 0; i < 5; i += interval) {
			int oldStartTime = time.get(weekdays[i]).getStart();
			int newStartTime = oldStartTime + len;
			Time temp = new Time(oldStartTime, newStartTime);
			klass.setTime(temp);
			klass.setMeetDate(i);  // each class only have one meeting time, but several meeting date, therefore only need to set one Time for each class but several dates for each class
			time.get(weekdays[i]).setStart(newStartTime);
			Map<Time, Class> newClass = new HashMap<>();
			newClass.put(temp, klass);
			classSchedule.put(weekdays[i], newClass);
		} 
		return true;
	}

	public boolean isFull() {
		for (int i = 0; i < 5; i++) {
			if(time.get(weekdays[i]).getInterval() < 60) {
				return false;
			}
		}
		return true;
	}

	public Map<String, Map<Time, Class>> getSchedule() {
		return classSchedule;
	}
}