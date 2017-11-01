class Time {
	private int start, end; // represent as minutes

	public Time(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setStart(int newStart) {
		start = newStart;
	}

	public void setEnd(int newEnd) {
		end = newEnd;
	}


	public int getInterval() {
		return (start - end);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + start;
		hash = 31 * hash + end;
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
      	if (!(o instanceof Time)) {
        	return false;
     	}
		Time that = (Time)o;
		return this.start == that.getStart() && this.end == that.getEnd();
	}

}