class Class {
	private final int id, cap, length;
	private final String catalog;
	private final String subject;
	private Teacher teacher;
	private Room room;
	private Boolean[] meetDate = {false, false, false, false, false};;  // if meet Monday, meetDate[0] is true
	private Time time;

	public Class(int id, int cap, String catalog, int length, String subject, Teacher teacher) {
		this.id = id;
		this.cap = cap;
		this.catalog = catalog;
		this.length = length;
		this.subject = subject;
		this.teacher = teacher;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public int getCap() {
		return cap;
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
 		Class that = (Class)obj;
 		return this.id == that.id;
 	}
	 
}
