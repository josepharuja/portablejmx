package org.leedsmet.studentresearch.jmx.assembler.tests.mbeans;

public class Student implements StudentMBean {

	private String name;

	private int age;

	public Student() {
		// TODO Auto-generated method stub
		this.name = "Joseph Aruja";
		this.age = 31;

	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.leedsmet.studentresearch.jmx.assembler.tests.mbeans.StudentMBean#addMarks(int,
	 *      int)
	 */
	public int addMarks(int module1, int module2) {

		// TODO Auto-generated method stub
		return module1 + module2;

	}

}
