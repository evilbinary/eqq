/*
 * Create By EvilBinary 小E
 * 2011-10-20
 * rootntsd@gmail.com
 */
package org.evilbinary.client;

public class Category {
	protected String index;
	protected String sort;
	protected String name;
	public Category(String index, String sort, String name) {
		super();
		this.index = index;
		this.sort = sort;
		this.name = name;
	}
	public Category() {
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	 
	
}
