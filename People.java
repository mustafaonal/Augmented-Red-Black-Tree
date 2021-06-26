package rbt;

public class People {
	int id;
	Date birtdate;
	int age;

	public People(int id, Date birtdate, int age) {
		this.id = id;
		this.birtdate = birtdate;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBirtdate() {
		return birtdate;
	}

	public void setBirtdate(Date birtdate) {
		this.birtdate = birtdate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
