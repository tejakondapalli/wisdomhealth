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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.proxy.HibernateProxy;


/** 
 * Object mapping for hibernate-handled table: TblCodefamily.
 * @author autogenerated
 */

@Entity
@Table(name = "tblCodeFamily")
public class TblCodeFamily implements Cloneable, Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = -558917088L;

	/** Use a WeakHashMap so entries will be garbage collected once all entities 
		referring to a saved hash are garbage collected themselves. */
	private static final Map<Serializable, Integer> SAVED_HASHES =
		Collections.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	
	/** hashCode temporary storage. */
	private volatile Integer hashCode;
	

	/** Field mapping. */
	private String codeFamilyNcid;
	/** Field mapping. */
	private Integer id = 0; // init for hibernate bug workaround
	/** Field mapping. */
	private Set<TblCodeDefinitions> tblCodeDefinitions = new TreeSet<TblCodeDefinitions>();

	/** Field mapping. */
	private Set<TblDateTags> tblDateTags = new TreeSet<TblDateTags>();

	/** Field mapping. */
	private Set<TblMappingRules> tblMappingRules = new TreeSet<TblMappingRules>();

	/** Field mapping. */
	private Set<TblVerbFrameActions> tblVerbFrameActions = new TreeSet<TblVerbFrameActions>();

	/** Field mapping. */
	private Set<TblVerbFrameRules> tblVerbFrameRules = new TreeSet<TblVerbFrameRules>();

	/**
	 * Default constructor, mainly for hibernate use.
	 */
	public TblCodeFamily() {
		// Default constructor
	} 

	/** Constructor taking a given ID.
	 * @param id to set
	 */
	public TblCodeFamily(Integer id) {
		this.id = id;
	}
	
	/** Constructor taking a given ID.
	 * @param codefamilncid String object;
	 * @param id Integer object;
	 */
	public TblCodeFamily(String codefamilncid, Integer id) {

		this.codeFamilyNcid = codefamilncid;
		this.id = id;
	}
	
 


 
	/** Return the type of this class. Useful for when dealing with proxies.
	* @return Defining class.
	*/
	@Transient
	public Class<?> getClassType() {
		return TblCodeFamily.class;
	}
 

    /**
     * Return the value associated with the column: codefamilncid.
	 * @return A String object (this.codefamilncid)
	 */
	@Basic( optional = false )
	@Column( nullable = false, length = 45  )
	public String getCodeFamilyNcid() {
		return this.codeFamilyNcid;
		
	}
	

  
    /**  
     * Set the value related to the column: codefamilncid.
	 * @param codeFamilyNcid the codefamilncid value you wish to set
	 */
	public void setCodeFamilyNcid(final String codeFamilyNcid) {
		this.codeFamilyNcid = codeFamilyNcid;
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
     * Return the value associated with the column: tblcodedefinitions.
	 * @return A Set&lt;Tblcodedefinitions&gt; object (this.tblcodedefinitions)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "fkCodeFamilyId"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
	public Set<TblCodeDefinitions> getTblCodeDefinitions() {
		return this.tblCodeDefinitions;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tblcodedefinitions to the tblcodedefinitionss set.
	 * @param tblCodeDefinitions item to add
	 */
	public void addTblCodeDefinitions(TblCodeDefinitions tblCodeDefinitions) {
		tblCodeDefinitions.setFkCodeFamilyId(this);
		this.tblCodeDefinitions.add(tblCodeDefinitions);
	}

  
    /**  
     * Set the value related to the column: tblcodedefinitions.
	 * @param tblCodeDefinitions the tblcodedefinitions value you wish to set
	 */
	public void setTblCodeDefinitions(final Set<TblCodeDefinitions> tblCodeDefinitions) {
		this.tblCodeDefinitions = tblCodeDefinitions;
	}

    /**
     * Return the value associated with the column: tbldatetags.
	 * @return A Set&lt;Tbldatetags&gt; object (this.tbldatetags)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "fkCodeFamilyId"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
	public Set<TblDateTags> getTblDateTags() {
		return this.tblDateTags;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tbldatetags to the tbldatetagss set.
	 * @param tblDateTags item to add
	 */
	public void addTblDateTags(TblDateTags tblDateTags) {
		tblDateTags.setFkCodeFamilyId(this);
		this.tblDateTags.add(tblDateTags);
	}

  
    /**  
     * Set the value related to the column: tbldatetags.
	 * @param tblDateTags the tbldatetags value you wish to set
	 */
	public void setTblDateTags(final Set<TblDateTags> tblDateTags) {
		this.tblDateTags = tblDateTags;
	}

    /**
     * Return the value associated with the column: tblmappingrules.
	 * @return A Set&lt;Tblmappingrules&gt; object (this.tblmappingrules)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "fkCodeFamilyId"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
	public Set<TblMappingRules> getTblMappingRules() {
		return this.tblMappingRules;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tblmappingrules to the tblMappingRules set.
	 * @param tblMappingRules item to add
	 */
	public void addTblmappingrules(TblMappingRules tblMappingRules) {
		tblMappingRules.setFkCodeFamilyId(this);
		this.tblMappingRules.add(tblMappingRules);
	}

  
    /**  
     * Set the value related to the column: tblmappingrules.
	 * @param tblMappingRules the tblmappingrules value you wish to set
	 */
	public void setTblMappingRules(final Set<TblMappingRules> tblMappingRules) {
		this.tblMappingRules = tblMappingRules;
	}

    /**
     * Return the value associated with the column: tblverbframeactions.
	 * @return A Set&lt;Tblverbframeactions&gt; object (this.tblverbframeactions)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "fkCodeFamilyId"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
	public Set<TblVerbFrameActions> getTblVerbFrameActions() {
		return this.tblVerbFrameActions;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tblverbframeactions to the tblVerbFrameActions set.
	 * @param tblVerbFrameActions item to add
	 */
	public void addTblverbframeactions(TblVerbFrameActions tblVerbFrameActions) {
		tblVerbFrameActions.setFkCodeFamilyId(this);
		this.tblVerbFrameActions.add(tblVerbFrameActions);
	}

  
    /**  
     * Set the value related to the column: tblverbframeactions.
	 * @param tblVerbFrameActions the tblverbframeactions value you wish to set
	 */
	public void setTblVerbFrameActions(final Set<TblVerbFrameActions> tblVerbFrameActions) {
		this.tblVerbFrameActions = tblVerbFrameActions;
	}

    /**
     * Return the value associated with the column: tblverbframerules.
	 * @return A Set&lt;Tblverbframerules&gt; object (this.tblverbframerules)
	 */
 	@OneToMany( fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "fkCodeFamilyId"  )
 	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@Basic( optional = false )
	@Column( nullable = false  )
 	@OrderBy("id ASC")
	public Set<TblVerbFrameRules> getTblVerbFrameRules() {
		return this.tblVerbFrameRules;
		
	}
	
	/**
	 * Adds a bi-directional link of type Tblverbframerules to the tblVerbFrameRules set.
	 * @param tblVerbFrameRules item to add
	 */
	public void addTblverbframerules(TblVerbFrameRules tblVerbFrameRules) {
		tblVerbFrameRules.setFkCodeFamilyId(this);
		this.tblVerbFrameRules.add(tblVerbFrameRules);
	}

  
    /**  
     * Set the value related to the column: tblverbframerules.
	 * @param tblVerbFrameRules the tblverbframerules value you wish to set
	 */
	public void setTblVerbFrameRules(final Set<TblVerbFrameRules> tblVerbFrameRules) {
		this.tblVerbFrameRules = tblVerbFrameRules;
	}


   /**
    * Deep copy.
	* @return cloned object
	* @throws CloneNotSupportedException on error
    */
    @Override
    public TblCodeFamily clone() throws CloneNotSupportedException {
		
        final TblCodeFamily copy = (TblCodeFamily)super.clone();

		copy.setCodeFamilyNcid(this.getCodeFamilyNcid());
		copy.setId(this.getId());
		if (this.getTblCodeDefinitions() != null) {
			copy.getTblCodeDefinitions().addAll(this.getTblCodeDefinitions());
		}
		if (this.getTblDateTags() != null) {
			copy.getTblDateTags().addAll(this.getTblDateTags());
		}
		if (this.getTblMappingRules() != null) {
			copy.getTblMappingRules().addAll(this.getTblMappingRules());
		}
		if (this.getTblVerbFrameActions() != null) {
			copy.getTblVerbFrameActions().addAll(this.getTblVerbFrameActions());
		}
		if (this.getTblVerbFrameRules() != null) {
			copy.getTblVerbFrameRules().addAll(this.getTblVerbFrameRules());
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
		
		sb.append("codefamilncid: " + this.getCodeFamilyNcid() + ", ");
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
		
		final TblCodeFamily that; 
		try {
			that = (TblCodeFamily) proxyThat;
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
		result = result && (((getCodeFamilyNcid() == null) && (that.getCodeFamilyNcid() == null)) || (getCodeFamilyNcid() != null && getCodeFamilyNcid().equals(that.getCodeFamilyNcid())));
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