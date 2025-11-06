package org.leedsmet.studentresearch.jmx.assembler.tests.mbeans;

public interface StudentMBean {

	String getName();
	void setName(String name);

	int getAge();
	void setAge(int age);
	public int addMarks(int module1,int module2);

}
