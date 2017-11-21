import java.util.*;

class Class {
	private final int id, length, level;
	private final String subject;
	private Teacher teacher;
	private Room room;
	private Boolean[] meetDate = {false, false, false, false, false};;  // if meet Monday, meetDate[0] is true
	private Time time;
	private List<Integer> students;

	public Class(int id, int level, int length, String subject, Teacher teacher) {
		this.id = id;
		this.level = level;
		this.length = length;
		this.subject = subject;
		this.teacher = teacher;
		this.students = new ArrayList<Integer>();
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public String getSubject() {
		return subject;
	}

	public int getLevel() {
		return level;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setMeetDate(int index) {
		meetDate[index] = true;
	}

	public String getMeetDate() {
		if((meetDate[0] && meetDate[2]) && meetDate[4]) {
			return "MWF";
		} else if(meetDate[1] && meetDate[3]) {
			return "TTH";
		} else if(meetDate[4]) {
			return "F";
		}
		return "";
	}

	public boolean addStudent(int stdId) {
		List<Integer> stdprefs = StudentPref.prefs.get(stdId);
		for(int c : stdprefs){
			Class temp = Scheduler.classMap.get(c);
			if(temp.getTime() != null && temp.getId() != id && temp.getTime().equals(time)){
				StudentPref.prefs.get(stdId).remove((Integer)id);
				return false;
			}
		}
		students.add(stdId);
		return true;
	}
	
 	@Override
 	public int hashCode() {
 		return id;
 	}

 	@Override
 	public boolean equals(Object o) {
 		if (o == this) return true;
       	if (!(o instanceof Class)) {
         	return false;
      	}
 		Class that = (Class)o;
 		return this.id == that.id;
 	}

}
