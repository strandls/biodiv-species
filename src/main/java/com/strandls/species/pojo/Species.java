package com.strandls.species.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "species")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Species implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1440457930006685830L;
	private Long id;
	private Long version;
	private String guid;
	private Integer percentOfInfo;
	private Long taxonConceptId;
	private String title;
	private Date createdOn;
	private Date updatedOn;
	private Date dateCreated;
	private Date lastUpdated;
	private Integer featureCount;
	private Long habitatId;
	private Boolean hasMedia;
	private Long reprImageId;
	private Boolean isDeleted;
	private Long dataTableId;

	public Species() {
		super();
	}

	public Species(Long id, Long version, String guid, Integer percentOfInfo, Long taxonConceptId, String title,
			Date createdOn, Date updatedOn, Date dateCreated, Date lastUpdated, Integer featureCount, Long habitatId,
			Boolean hasMedia, Long reprImageId, Boolean isDeleted, Long dataTableId) {
		super();
		this.id = id;
		this.version = version;
		this.guid = guid;
		this.percentOfInfo = percentOfInfo;
		this.taxonConceptId = taxonConceptId;
		this.title = title;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
		this.featureCount = featureCount;
		this.habitatId = habitatId;
		this.hasMedia = hasMedia;
		this.reprImageId = reprImageId;
		this.isDeleted = isDeleted;
		this.dataTableId = dataTableId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "version")
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(name = "guid", unique = true)
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Column(name = "percent_of_info")
	public Integer getPercentOfInfo() {
		return percentOfInfo;
	}

	public void setPercentOfInfo(Integer percentOfInfo) {
		this.percentOfInfo = percentOfInfo;
	}

	@Column(name = "taxon_concept_id")
	public Long getTaxonConceptId() {
		return taxonConceptId;
	}

	public void setTaxonConceptId(Long taxonConceptId) {
		this.taxonConceptId = taxonConceptId;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "updated_on")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "date_created")
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "last_updated")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "feature_count")
	public Integer getFeatureCount() {
		return featureCount;
	}

	public void setFeatureCount(Integer featureCount) {
		this.featureCount = featureCount;
	}

	@Column(name = "habitat_id")
	public Long getHabitatId() {
		return habitatId;
	}

	public void setHabitatId(Long habitatId) {
		this.habitatId = habitatId;
	}

	@Column(name = "has_media")
	public Boolean getHasMedia() {
		return hasMedia;
	}

	public void setHasMedia(Boolean hasMedia) {
		this.hasMedia = hasMedia;
	}

	@Column(name = "repr_image_id")
	public Long getReprImageId() {
		return reprImageId;
	}

	public void setReprImageId(Long reprImageId) {
		this.reprImageId = reprImageId;
	}

	@Column(name = "is_deleted")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name = "data_table_id")
	public Long getDataTableId() {
		return dataTableId;
	}

	public void setDataTableId(Long dataTableId) {
		this.dataTableId = dataTableId;
	}

}
