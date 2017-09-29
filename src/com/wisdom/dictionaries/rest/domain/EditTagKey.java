package com.wisdom.dictionaries.rest.domain;
/*package com.wisdom.region.rest.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EditTagKey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tagId;
	private Integer editId;

	public EditTagKey() {
	}

	public EditTagKey(Integer tagId, Integer editId) {
		this.tagId = tagId;
		this.editId = editId;
	}

	@Column(name = "fkTagId", nullable = false)
	public Integer getTagId() {
		return this.tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	@Column(name = "fkEditId", nullable = false)
	public Integer getEditId() {
		return this.editId;
	}

	public void setEditId(Integer editId) {
		this.editId = editId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof EditTagKey)) {
			return false;
		}
		EditTagKey castOther = (EditTagKey) other;

		return (this.getTagId() == castOther.getTagId())
				&& (this.getEditId() == castOther.getEditId());
	}

	public int hashCode() {
		Integer result = 17;

		result = 37 * result + this.getTagId();
		result = 37 * result + this.getEditId();
		return result;
	}

}
*/