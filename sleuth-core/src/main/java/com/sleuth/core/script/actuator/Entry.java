package com.sleuth.core.script.actuator;

import java.util.Objects;

public class Entry {
	
	private String name;
	private Object value;
	
	public Entry() {
		
	}
	
	public Entry(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entry) {
			Entry entry = (Entry) obj;
			if (entry.getName().equals(this.name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.value);
	}

	@Override
	public String toString() {
		return "Entry [name=" + name + ", value=" + value + "]";
	}
	
}
