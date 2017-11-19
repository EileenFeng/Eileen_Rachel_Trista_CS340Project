import java.util.*;

class Teacher {
	private final int name;
	private Set<Class> classes;
	private Map<String, Set<Time>> times;

	public Teacher(int name) {
		this.name = name;
		this.classes = new HashSet<>();
		this.times = new HashMap<>();
	}

	public int getName() {
		return name;
	}

	public Set<Class> classes(){
		return classes;
	}

	public Map<String, Set<Time>> times(){
		return times;
	}

	public boolean addTime(String date, Time t){
		if(conflict(date, t)){
			return false;
		}
		times.get(date).add(t);
		return true;
	}

	public boolean conflict(String date, Time t){
		if (times.get(date) == null) {
			times.put(date, new HashSet<>());
			return false;
		}
		for(Time temp: times.get(date)){
			if(temp.getStart() <= t.getEnd() && t.getStart() <= temp.getEnd()){
				return true;
			}
		}
		return false;
	}

}
