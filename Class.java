import java.util.*;

class Class {
	private final int id, length;
	private final String catalog;
	private final String subject;
	private Teacher teacher;
	private Room room;
	private Boolean[] meetDate = {false, false, false, false, false};;  // if meet Monday, meetDate[0] is true
	private Time time;
	private List<Integer> students;

	public Class(int id, String catalog, int length, String subject, Teacher teacher) {
		this.id = id;
		this.catalog = catalog;
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
		return catalog.charAt(1) - '0';
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
			return "MTF";
		} else if(meetDate[1] && meetDate[3]) {
			return "TTH";
		} else if(meetDate[4]) {
			return "F";
		}
		return "";
	}

	public int addStudent(int stdId) {
		students.add(stdId);
		return stdId;
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
