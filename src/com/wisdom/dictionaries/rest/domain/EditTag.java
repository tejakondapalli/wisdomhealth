package com.wisdom.dictionaries.rest.domain;
/*package com.wisdom.region.rest.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tblEditTag")
public class EditTag implements java.io.Serializable {

	private static final long serialVersionUID = 1L;


	private EditTagKey id;
	
	
	private Edit edit;
	private Tag tag;

	public EditTag(){
		
	}
	public EditTag(EditTagKey id){
		this.id = id;
	}
	public EditTag(EditTagKey id, Edit edit, Tag tag) {
		this.id = id;
		this.edit = edit;
		this.tag = tag;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "fkTagId", column = @Column(name = "fkTagId", nullable = false)),
			@AttributeOverride(name = "fkEditId", column = @Column(name = "fkEditId", nullable = false)) })
	public EditTagKey getId() {
		return this.id;
	}

	public void setId(EditTagKey id) {
		this.id = id;
	}

	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "fkEditId", nullable = false, insertable = false, updatable = false)
	public Edit getEdit() {
		return this.edit;
	}

	public void setEdit(Edit edit) {
		this.edit = edit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkTagId", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	
	

}
*/