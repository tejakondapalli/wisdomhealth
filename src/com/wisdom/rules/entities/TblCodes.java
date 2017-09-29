package com.wisdom.rules.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.proxy.HibernateProxy;

/**
 * Object mapping for hibernate-handled table: tblcodes.
 * 
 * @author autogenerated
 */

@Entity
@Table(name = "tblCodes")
public class TblCodes implements Cloneable, Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = -558917087L;

	/**
	 * Use a WeakHashMap so entries will be garbage collected once all entities
	 * referring to a saved hash are garbage collected themselves.
	 */
	private static final Map<Serializable, Integer> SAVED_HASHES = Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());

	/** hashCode temporary storage. */
	private volatile Integer hashCode;

	/** Field mapping. */
	private Integer id;

	/** Field mapping. */
	private Set<TblMappingRules> tblMappingRules = new TreeSet<TblMappingRules>();

	/** Field mapping. */
	private Set<TblMeasurementRules> tblMeasurementRules = new TreeSet<TblMeasurementRules>();

	/** Field mapping. */
	private Set<TblVerbFrameRules> tblVerbFrameRules = new TreeSet<TblVerbFrameRules>();

	/** Field mapping. */
	private String value;

	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public TblCodes() {
		// Default constructor
	}

	/**
	 * Constructor taking a given ID.
	 * 
	 * @param id
	 *            to set
	 */
	public TblCodes(Integer id) {
		this.id = id;
	}

	/**
	 * Return the type of this class. Useful for when dealing with proxies.
	 * 
	 * @return Defining class.
	 */
	@Transient
	public Class<?> getClassType() {
		return TblCodes.class;
	}

	/**
	 * Return the value associated with the column: id.
	 * 
	 * @return A Integer object (this.id)
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return this.id;

	}

	/**
	 * Set the value related to the column: id.
	 * 
	 * @param id
	 *            the id value you wish to set
	 */
	public void setId(final Integer id) {
		// If we've just been persisted and hashCode has been
		// returned then make sure other entities with this
		// ID return the already returned hash code
		if ((this.id == null || this.id == 0) && (id != null) && (this.hashCode != null)) {
			SAVED_HASHES.put(id, this.hashCode);
		}
		this.id = id;
	}

	/**
	 * Return the value associated with the column: tblMappingrules.
	 * 
	 * @return A Set&lt;TblMappingrules&gt; object (this.tblMappingrules)
	 */
	@ManyToMany(mappedBy = "tblCodes")
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	@Basic(optional = false)
	@Column(nullable = false)
	@OrderBy("id ASC")
	public Set<TblMappingRules> getTblMappingRules() {
		return this.tblMappingRules;

	}

	/**
	 * Adds a bi-directional link of type TblMappingrules to the tblMappingrules
	 * set.
	 * 
	 * @param tblMappingrules
	 *            item to add
	 */
	public void addTblMappingrules(TblMappingRules tblMappingrules) {
		this.tblMappingRules.add(tblMappingrules);
	}

	/**
	 * Set the value related to the column: tblMappingrules.
	 * 
	 * @param tblMappingRules
	 *            the tblMappingrules value you wish to set
	 */
	public void setTblMappingRules(final Set<TblMappingRules> tblMappingRules) {
		this.tblMappingRules = tblMappingRules;
	}

	/**
	 * Return the value associated with the column: tblMeasurementRules.
	 * 
	 * @return A Set&lt;TblMeasurementRules&gt; object
	 *         (this.tblMeasurementRules)
	 */
	@ManyToMany(mappedBy = "tblCodes")
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	@Basic(optional = false)
	@Column(nullable = false)
	@OrderBy("id ASC")
	public Set<TblMeasurementRules> getTblMeasurementRules() {
		return this.tblMeasurementRules;

	}

	/**
	 * Adds a bi-directional link of type TblMeasurementRules to the
	 * tblMeasurementRules set.
	 * 
	 * @param tblMeasurementRules
	 *            item to add
	 */
	public void addTblMeasurementRules(TblMeasurementRules tblMeasurementRules) {
		this.tblMeasurementRules.add(tblMeasurementRules);
	}

	/**
	 * Set the value related to the column: tblMeasurementRules.
	 * 
	 * @param tblMeasurementRules
	 *            the tblMeasurementRules value you wish to set
	 */
	public void setTblMeasurementRules(final Set<TblMeasurementRules> tblMeasurementRules) {
		this.tblMeasurementRules = tblMeasurementRules;
	}

	/**
	 * Return the value associated with the column: tblVerbframerules.
	 * 
	 * @return A Set&lt;TblVerbframerules&gt; object
	 *         (this.tblVerbframerules)
	 */
	@ManyToMany(mappedBy = "tblCodes")
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	@Basic(optional = false)
	@Column(nullable = false)
	@OrderBy("id ASC")
	public Set<TblVerbFrameRules> getTblVerbFrameRules() {
		return this.tblVerbFrameRules;

	}

	/**
	 * Adds a bi-directional link of type TblVerbframerules to the
	 * tblVerbframerules set.
	 * 
	 * @param tblVerbFrameRules
	 *            item to add
	 */
	public void addTblVerbFrameRules(TblVerbFrameRules tblVerbFrameRules) {
		this.tblVerbFrameRules.add(tblVerbFrameRules);
	}

	/**
	 * Set the value related to the column: tblVerbframerules.
	 * 
	 * @param tblVerbFrameRules
	 *            the tblVerbframerules value you wish to set
	 */
	public void setTblVerbFrameRules(final Set<TblVerbFrameRules> tblVerbFrameRules) {
		this.tblVerbFrameRules = tblVerbFrameRules;
	}

	/**
	 * Return the value associated with the column: value.
	 * 
	 * @return A String object (this.value)
	 */
	@Basic(optional = true)
	@Column(length = 45)
	public String getValue() {
		return this.value;

	}

	/**
	 * Set the value related to the column: value.
	 * 
	 * @param value
	 *            the value value you wish to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Deep copy.
	 * 
	 * @return cloned object
	 * @throws CloneNotSupportedException
	 *             on error
	 */
	@Override
	public TblCodes clone() throws CloneNotSupportedException {

		final TblCodes copy = (TblCodes) super.clone();

		copy.setId(this.getId());
		if (this.getTblMappingRules() != null) {
			copy.getTblMappingRules().addAll(this.getTblMappingRules());
		}
		if (this.getTblMeasurementRules() != null) {
			copy.getTblMeasurementRules().addAll(this.getTblMeasurementRules());
		}
		if (this.getTblVerbFrameRules() != null) {
			copy.getTblVerbFrameRules().addAll(this.getTblVerbFrameRules());
		}
		copy.setValue(this.getValue());
		return copy;
	}

	/**
	 * Provides toString implementation.
	 * 
	 * @see java.lang.Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("id: " + this.getId() + ", ");
		sb.append("value: " + this.getValue());
		return sb.toString();
	}

	/**
	 * Equals implementation.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param aThat
	 *            Object to compare with
	 * @return true/false
	 */
	@Override
	public boolean equals(final Object aThat) {
		Object proxyThat = aThat;

		if (this == aThat) {
			return true;
		}

		if (aThat instanceof HibernateProxy) {
			// narrow down the proxy to the class we are dealing with.
			try {
				proxyThat = ((HibernateProxy) aThat).getHibernateLazyInitializer().getImplementation();
			} catch (org.hibernate.ObjectNotFoundException e) {
				return false;
			}
		}
		if (aThat == null) {
			return false;
		}

		final TblCodes that;
		try {
			that = (TblCodes) proxyThat;
			if (!(that.getClassType().equals(this.getClassType()))) {
				return false;
			}
		} catch (org.hibernate.ObjectNotFoundException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}

		boolean result = true;
		result = result && (((this.getId() == null) && (that.getId() == null)) || (this.getId() != null && this.getId().equals(that.getId())));
		result = result && (((getValue() == null) && (that.getValue() == null)) || (getValue() != null && getValue().equals(that.getValue())));
		return result;
	}

	/**
	 * Calculate the hashcode.
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return a calculated number
	 */
	@Override
	public int hashCode() {
		if (this.hashCode == null) {
			synchronized (this) {
				if (this.hashCode == null) {
					Integer newHashCode = null;

					if (getId() != null) {
						newHashCode = SAVED_HASHES.get(getId());
					}

					if (newHashCode == null) {
						if (getId() != null && getId() != 0) {
							newHashCode = getId();
						} else {

						}
					}

					this.hashCode = newHashCode;
				}
			}
		}
		return (Integer) this.hashCode;
	}

}
