package com.takebox.wedding;

public class CategoryEditData {
	public String category;
	public String cata_seq;
	
	public CategoryEditData (String category, String cata_seq){
		this.category = category;
		this.cata_seq = cata_seq;
	}

	@Override
	public String toString() {
		return category.toString();
	}
}
