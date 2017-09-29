package com.wisdom.rules.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.proxy.HibernateProxy;


/** 
 * Object mapping for hibernate-handled table: TblCodeDefinitions.
 * @author autogenerated
 */

@Entity
@Table(name = "tblCodeDefinitions")
public class TblCodeDefinitions implements Cloneable, Serializable{

	/** Serial Version UID. */
	private static final long serialVersionUID = -558917090L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private Byte allowHistorical;
	/** Field mapping. */
	private Byte allowOtherExperiencer;
	/** Field mapping. */
	private Byte bodyPartFromExam;
	/** Field mapping. */
	private TblCodeFamily fkCodeFamilyId;
	/** Field mapping. */
	private TblCodes fkCodeId;
	/** Field mapping. */
	private Integer id = 0; // init for hibernate bug workaround
	/** Field mapping. */
	private Set<TblSlots> tblSlots = new TreeSet<TblSlots>();

	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public TblCodeDefinitions() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public TblCodeDefinitions(Integer id) {
		this.id = id;
	}
	
	/** Constructor taking a given ID.
	 * @param fkCodeFamilyId Tblcodefamily object;
	 * @param fkCodeId Tblcodes object;
	 * @param id Integer object;
	 */
	public TblCodeDefinitions(TblCodeFamily fkCodeFamilyId, TblCodes fkCodeId, Integer id) {

		this.fkCodeFamilyId = fkCodeFamilyId;
		this.fkCodeId = fkCodeId;
		this.id = id;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return TblCodeDefinitions.class;
	}
 

    /**
     * Return the value associated with the column: allowhistorical.
	 * @return A Byte object (this.allowhistorical)
	 */
	public Byte getAllowHistorical() {
		return this.allowHistorical;
		
	}
	

  
    /**  
     * Set the value related to the column: allowhistorical.
	 * @param allowHistorical the allowhistorical value you wish to set
	 */
	public void setAllowHistorical(final Byte allowHistorical) {
		this.allowHistorical = allowHistorical;
	}

    /**
     * Return the value associated with the column: allowotherexperiencer.
	 * @return A Byte object (this.allowotherexperiencer)
	 */
	public Byte getAllowOtherExperiencer() {
		return this.allowOtherExperiencer;
		
	}
	

  
    /**  
     * Set the value related to the column: allowotherexperiencer.
	 * @param allowOtherExperiencer the allowotherexperiencer value you wish to set
	 */
	public void setAllowOtherExperiencer(final Byte allowOtherExperiencer) {
		this.allowOtherExperiencer = allowOtherExperiencer;
	}

    /**
     * Return the value associated with the column: bodypartfromexam.
	 * @return A Byte object (this.bodypartfromexam)
	 */
	public Byte getBodyPartFromExam() {
		return this.bodyPartFromExam;
		
	}
	

  
    /**  
     * Set the value related to the column: bodypartfromexam.
	 * @param bodyPartFromExam the bodypartfromexam value you wish to set
	 */
	public void setBodyPartFromExam(final Byte bodyPartFromExam) {
		this.bodyPartFromExam = bodyPartFromExam;
	}

    /**
     * Return the value associated with the column: fkcodefamilyid.
	 * @return A Tblcodefamily object (this.fkcodefamilyid)
	 */
	@ManyToOne( cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY )
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@JoinColumn(name = "fkCodeFamilyId", nullable = false )
	public TblCodeFamily getFkCodeFamilyId() {
		return this.fkCodeFamilyId;
		
	}
	

  
    /**  
     * Set the value related to the column: fkcodefamilyid.
	 * @param fkCodeFamilyId the fkcodefamilyid value you wish to set
	 */
	public void setFkCodeFamilyId(final TblCodeFamily fkCodeFamilyId) {
		this.fkCodeFamilyId = fkCodeFamilyId;
	}

    /**
     * Return the value associated with the column: fkcodeid.
	 * @return A Tblcodes object (this.fkcodeid)
	 */
	@ManyToOne( cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY )
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@JoinColumn(name = "fkCodeId", nullable = false )
	public TblCodes getFkCodeId() {
		return this.fkCodeId;
		
	}
	

  
    /**  
     * Set the value related to the column: fkcodeid.
	 * @param fkCodeId the fkcodeid value you wish to set
	 */
	public void setFkCodeId(final TblCodes fkCodeId) {
		this.fkCodeId = fkCodeId;
	}

    /**
     * Return the value associated with the column: id.
	 * @return A Integer object (this.id)
	 */
    @Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
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
     * Return the value associated with the column: tblSlots.
	 * @return A Set&lt;TblSlots&gt; object (this.tblSlots)
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tblCodeDefinitionsSlots", joinColumns = @JoinColumn(name = "fkCodeDefinitionId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fkSlotId", referencedColumnName = "id"))
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
	@OrderBy("id ASC")
	public Set<TblSlots> getTblSlots() {
		return this.tblSlots;
		
	}
	
	/**
	 * Adds a bi-directional link of type TblSlots to the tblSlots set.
	 * @param tblSlots item to add
	 */
	public void addTblSlots(TblSlots tblSlots) {
		this.tblSlots.add(tblSlots);
	}

  
    /**  
     * Set the value related to the column: tblSlots.
	 * @param tblSlots the tblSlots value you wish to set
	 */
	public void setTblSlots(final Set<TblSlots> tblSlots) {
		this.tblSlots = tblSlots;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public TblCodeDefinitions clone() throws CloneNotSupportedException {
		
        final TblCodeDefinitions copy = (TblCodeDefinitions)super.clone();

		copy.setAllowHistorical(this.getAllowHistorical());
		copy.setAllowOtherExperiencer(this.getAllowOtherExperiencer());
		copy.setBodyPartFromExam(this.getBodyPartFromExam());
		copy.setFkCodeFamilyId(this.getFkCodeFamilyId());
		copy.setFkCodeId(this.getFkCodeId());
		copy.setId(this.getId());
		if (this.getTblSlots() != null) {
			copy.getTblSlots().addAll(this.getTblSlots());
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
		
		sb.append("allowHistorical: " + this.getAllowHistorical() + ", ");
		sb.append("allowOtherExperiencer: " + this.getAllowOtherExperiencer() + ", ");
		sb.append("bodyPartFromExam: " + this.getBodyPartFromExam() + ", ");
		sb.append("id: " + this.getId() + ", ");
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
		
		final TblCodeDefinitions that; 
		try {
			that = (TblCodeDefinitions) proxyThat;
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
		result = result && (((getAllowHistorical() == null) && (that.getAllowHistorical() == null)) || (getAllowHistorical() != null && getAllowHistorical().equals(that.getAllowHistorical())));
		result = result && (((getAllowOtherExperiencer() == null) && (that.getAllowOtherExperiencer() == null)) || (getAllowOtherExperiencer() != null && getAllowOtherExperiencer().equals(that.getAllowOtherExperiencer())));
		result = result && (((getBodyPartFromExam() == null) && (that.getBodyPartFromExam() == null)) || (getBodyPartFromExam() != null && getBodyPartFromExam().equals(that.getBodyPartFromExam())));
		result = result && (((getFkCodeFamilyId() == null) && (that.getFkCodeFamilyId() == null)) || (getFkCodeFamilyId() != null && getFkCodeFamilyId().getId().equals(that.getFkCodeFamilyId().getId())));	
		result = result && (((getFkCodeId() == null) && (that.getFkCodeId() == null)) || (getFkCodeId() != null && getFkCodeId().getId().equals(that.getFkCodeId().getId())));	
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
