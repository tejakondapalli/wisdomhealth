package com.wisdom.rules.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.proxy.HibernateProxy;


/** 
 * Object mapping for hibernate-handled table: tblverbframes.
 * @author autogenerated
 */

@Entity
@Table(name = "TblVerbFrames")
public class TblVerbFrames implements Cloneable, Serializable{

	/** Serial Version UID. */
	private static final long serialVersionUID = -558917062L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private Byte historical;
	/** Field mapping. */
	private Byte hypothetical;
	/** Field mapping. */
	private Integer id;
	/** Field mapping. */
	private Byte negated;
	/** Field mapping. */
	private String normalizedText;
	/** Field mapping. */
	private Byte otherExperiencer;

	/** Field mapping. */
	private Set<TblVerbFrameRules> tblVerbFrameRules = new TreeSet<TblVerbFrameRules>();

	/** Field mapping. */
	private Set<TblVerbFrames> tblVerbFramesChilds = new TreeSet<TblVerbFrames>();

	/** Field mapping. */
	private Set<TblConcept> tblConcepts = new TreeSet<TblConcept>();

	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public TblVerbFrames() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public TblVerbFrames(Integer id) {
		this.id = id;
	}
	
	/** Constructor taking a given ID.
	 * @param historical Byte object;
	 * @param hypothetical Byte object;
	 * @param id Integer object;
	 * @param negated Byte object;
	 * @param normalizedText String object;
	 * @param otherExperiencer Byte object;
	 */
	public TblVerbFrames(Byte historical, Byte hypothetical, Integer id, 					
			Byte negated, String normalizedText, Byte otherExperiencer) {

		this.historical = historical;
		this.hypothetical = hypothetical;
		this.id = id;
		this.negated = negated;
		this.normalizedText = normalizedText;
		this.otherExperiencer = otherExperiencer;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return TblVerbFrames.class;
	}
 

    /**
     * Return the value associated with the column: historical.
	 * @return A Byte object (this.historical)
	 */
	@Basic( optional = false )
	@Column( nullable = false  )
	public Byte getHistorical() {
		return this.historical;
		
	}
	

  
    /**  
     * Set the value related to the column: historical.
	 * @param historical the historical value you wish to set
	 */
	public void setHistorical(final Byte historical) {
		this.historical = historical;
	}

    /**
     * Return the value associated with the column: hypothetical.
	 * @return A Byte object (this.hypothetical)
	 */
	@Basic( optional = false )
	@Column( nullable = false  )
	public Byte getHypothetical() {
		return this.hypothetical;
		
	}
	

  
    /**  
     * Set the value related to the column: hypothetical.
	 * @param hypothetical the hypothetical value you wish to set
	 */
	public void setHypothetical(final Byte hypothetical) {
		this.hypothetical = hypothetical;
	}

    /**
     * Return the value associated with the column: id.
	 * @return A Integer object (this.id)
	 */
    @Id 
	@Basic( optional = false )
	@Column( name = "id", nullable = false  )
	public Integer getId() {
		return this.id;
		
	}
	

  
    /**  
     * Set the value related to the column: id.
	 * @param id the id value you wish to set
	 */
	public void setId(final Integer id) {
		// If we've just been persisted and hashCode has been
		// returned then make sure other entities with this
		// ID return the already returned hash code
		if ( (this.id == null || this.id == 0) &&
				(id != null) &&
				(this.hashCode != null) ) {
		SAVED_HASHES.put( id, this.hashCode );
		}
		this.id = id;
	}

    /**
     * Return the value associated with the column: negated.
	 * @return A Byte object (this.negated)
	 */
	@Basic( optional = false )
	@Column( nullable = false  )
	public Byte getNegated() {
		return this.negated;
		
	}
	

  
    /**  
     * Set the value related to the column: negated.
	 * @param negated the negated value you wish to set
	 */
	public void setNegated(final Byte negated) {
		this.negated = negated;
	}

    /**
     * Return the value associated with the column: normalizedtext.
	 * @return A String object (this.normalizedtext)
	 */
	@Basic( optional = false )
	@Column( nullable = false, length = 220  )
	public String getNormalizedText() {
		return this.normalizedText;
		
	}
	

  
    /**  
     * Set the value related to the column: normalizedtext.
	 * @param normalizedText the normalizedtext value you wish to set
	 */
	public void setNormalizedText(final String normalizedText) {
		this.normalizedText = normalizedText;
	}

    /**
     * Return the value associated with the column: otherexperiencer.
	 * @return A Byte object (this.otherexperiencer)
	 */
	@Basic( optional = false )
	@Column( nullable = false  )
	public Byte getOtherExperiencer() {
		return this.otherExperiencer;
		
	}
	

  
    /**  
     * Set the value related to the column: otherexperiencer.
	 * @param otherExperiencer the otherexperiencer value you wish to set
	 */
	public void setOtherExperiencer(final Byte otherExperiencer) {
		this.otherExperiencer = otherExperiencer;
	}

    /**
     * Return the value associated with the column: tblVerbframerules.
	 * @return A Set&lt;TblVerbframerules&gt; object (this.tblVerbframerules)
	 */
	@ManyToMany(mappedBy = "tblVerbFrames")
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
	@OrderBy("id ASC")
	public Set<TblVerbFrameRules> getTblVerbFrameRules() {
		return this.tblVerbFrameRules;
		
	}
	
	/**
	 * Adds a bi-directional link of type TblVerbframerules to the tblVerbframerules set.
	 * @param tblVerbFrameRules item to add
	 */
	public void addTblVerbFrameRules(TblVerbFrameRules tblVerbFrameRules) {
		this.tblVerbFrameRules.add(tblVerbFrameRules);
	}

  
    /**  
     * Set the value related to the column: tblVerbframerules.
	 * @param tblVerbFrameRules the tblVerbframerules value you wish to set
	 */
	public void setTblVerbFrameRules(final Set<TblVerbFrameRules> tblVerbFrameRules) {
		this.tblVerbFrameRules = tblVerbFrameRules;
	}

    /**
     * Return the value associated with the column: tblverbframeschilds.
	 * @return A Set&lt;Tblverbframeschilds&gt; object (this.tblverbframeschilds)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "id"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
 	public Set<TblVerbFrames> getTblVerbFramesChilds() {
		return this.tblVerbFramesChilds;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tblverbframeschilds to the tblverbframeschilds set.
	 * @param tblVerbFramesChilds item to add
	 */
	public void addTblVerbFramesChilds(TblVerbFrames tblVerbFramesChilds) {
		this.tblVerbFramesChilds.add(tblVerbFramesChilds);
	}

  
    /**  
     * Set the value related to the column: tblverbframeschilds.
	 * @param tblVerbFramesChilds the tblverbframeschilds value you wish to set
	 */
	public void setTblVerbFramesChilds(final Set<TblVerbFrames> tblVerbFramesChilds) {
		this.tblVerbFramesChilds = tblVerbFramesChilds;
	}

    /**
     * Return the value associated with the column: tblConcept.
	 * @return A Set&lt;TblConcept&gt; object (this.tblConcept)
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tblVerbFramesConcept", joinColumns = @JoinColumn(name = "fkVerbFrameId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fkConceptId", referencedColumnName = "id"))
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
	@OrderBy("id ASC")
	public Set<TblConcept> getTblConcepts() {
		return this.tblConcepts;
		
	}
	
	/**
	 * Adds a bi-directional link of type TblConcept to the tblConcepts set.
	 * @param tblConcept item to add
	 */
	public void addTblConcept(TblConcept tblConcept) {
		this.tblConcepts.add(tblConcept);
	}

  
    /**  
     * Set the value related to the column: tblConcept.
	 * @param tblConcept the tblConcept value you wish to set
	 */
	public void setTblConcepts(final Set<TblConcept> tblConcept) {
		this.tblConcepts = tblConcept;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public TblVerbFrames clone() throws CloneNotSupportedException {
		
        final TblVerbFrames copy = (TblVerbFrames)super.clone();

		copy.setHistorical(this.getHistorical());
		copy.setHypothetical(this.getHypothetical());
		copy.setId(this.getId());
		copy.setNegated(this.getNegated());
		copy.setNormalizedText(this.getNormalizedText());
		copy.setOtherExperiencer(this.getOtherExperiencer());
		if (this.getTblVerbFrameRules() != null) {
			copy.getTblVerbFrameRules().addAll(this.getTblVerbFrameRules());
		}
		if (this.getTblVerbFramesChilds() != null) {
			copy.getTblVerbFramesChilds().addAll(this.getTblVerbFramesChilds());
		}
		if (this.getTblConcepts() != null) {
			copy.getTblConcepts().addAll(this.getTblConcepts());
		}
		return copy;
	}
	


	/** Provides toString implementation.
	 * @see java.lang.Object#toString()
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("historical: " + this.getHistorical() + ", ");
		sb.append("hypothetical: " + this.getHypothetical() + ", ");
		sb.append("id: " + this.getId() + ", ");
		sb.append("negated: " + this.getNegated() + ", ");
		sb.append("normalizedText: " + this.getNormalizedText() + ", ");
		sb.append("otherExperiencer: " + this.getOtherExperiencer() + ", ");
		return sb.toString();		
	}


	/** Equals implementation. 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param aThat Object to compare with
	 * @return true/false
	 */
	@Override
	public boolean equals(final Object aThat) {
		Object proxyThat = aThat;
		
		if ( this == aThat ) {
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
		if (aThat == null)  {
			 return false;
		}
		
		final TblVerbFrames that; 
		try {
			that = (TblVerbFrames) proxyThat;
			if ( !(that.getClassType().equals(this.getClassType()))){
				return false;
			}
		} catch (org.hibernate.ObjectNotFoundException e) {
				return false;
		} catch (ClassCastException e) {
				return false;
		}
		
		
		boolean result = true;
		result = result && (((this.getId() == null) && ( that.getId() == null)) || (this.getId() != null  && this.getId().equals(that.getId())));
		result = result && (((getHistorical() == null) && (that.getHistorical() == null)) || (getHistorical() != null && getHistorical().equals(that.getHistorical())));
		result = result && (((getHypothetical() == null) && (that.getHypothetical() == null)) || (getHypothetical() != null && getHypothetical().equals(that.getHypothetical())));
		result = result && (((getNegated() == null) && (that.getNegated() == null)) || (getNegated() != null && getNegated().equals(that.getNegated())));
		result = result && (((getNormalizedText() == null) && (that.getNormalizedText() == null)) || (getNormalizedText() != null && getNormalizedText().equals(that.getNormalizedText())));
		result = result && (((getOtherExperiencer() == null) && (that.getOtherExperiencer() == null)) || (getOtherExperiencer() != null && getOtherExperiencer().equals(that.getOtherExperiencer())));
		return result;
	}
	
	/** Calculate the hashcode.
	 * @see java.lang.Object#hashCode()
	 * @return a calculated number
	 */
	@Override
	public int hashCode() {
		if ( this.hashCode == null ) {
			synchronized ( this ) {
				if ( this.hashCode == null ) {
					Integer newHashCode = null;

					if ( getId() != null ) {
					newHashCode = SAVED_HASHES.get( getId() );
					}
					
					if ( newHashCode == null ) {
						if ( getId() != null && getId() != 0) {
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
