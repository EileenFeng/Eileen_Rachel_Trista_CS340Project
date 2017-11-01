import java.util.*;

class Student {
	private final int id;
	private Set<Class> classList;

	public Student(int id, Set<Class> classList) {
		this.id = id;
		this.classList = classList;
	}

	public void removeClass(Class klass) {
		classList.remove(klass);
	}
}
