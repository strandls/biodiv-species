/**
 * 
 */
package com.strandls.species.pojo;

import java.util.List;

import com.strandls.resource.pojo.ResourceData;
import com.strandls.taxonomy.pojo.BreadCrumb;
import com.strandls.taxonomy.pojo.TaxonomyDefinition;
import com.strandls.traits.pojo.FactValuePair;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.UserGroupIbp;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class ShowSpeciesPage {

	private Species species;
	private List<BreadCrumb> breadCrumbs;
	private TaxonomyDefinition taxonomyDefinition;
	private List<ResourceData> resourceData;
	private List<SpeciesFieldData> fieldData;
	private List<FactValuePair> facts;
	private List<UserGroupIbp> userGroups;
	private List<Featured> featured;
//	private List<DocumentMeta> documentMetaList;

	/**
	 * 
	 */
	public ShowSpeciesPage() {
		super();
	}

	/**
	 * @param species
	 * @param breadCrumbs
	 * @param taxonomyDefinition
	 * @param resourceData
	 * @param fieldData
	 * @param facts
	 * @param userGroups
	 * @param featured
	 */
	public ShowSpeciesPage(Species species, List<BreadCrumb> breadCrumbs, TaxonomyDefinition taxonomyDefinition,
			List<ResourceData> resourceData, List<SpeciesFieldData> fieldData, List<FactValuePair> facts,
			List<UserGroupIbp> userGroups, List<Featured> featured) {
		super();
		this.species = species;
		this.breadCrumbs = breadCrumbs;
		this.taxonomyDefinition = taxonomyDefinition;
		this.resourceData = resourceData;
		this.fieldData = fieldData;
		this.facts = facts;
		this.userGroups = userGroups;
		this.featured = featured;
	}

//	/**
//	 * @param species
//	 * @param breadCrumbs
//	 * @param taxonomyDefinition
//	 * @param resourceData
//	 * @param fieldData
//	 * @param facts
//	 * @param userGroups
//	 * @param featured
//	 * @param documentMetaList
//	 */
//	public ShowSpeciesPage(Species species, List<BreadCrumb> breadCrumbs, TaxonomyDefinition taxonomyDefinition,
//			List<ResourceData> resourceData, List<SpeciesFieldData> fieldData, List<FactValuePair> facts,
//			List<UserGroupIbp> userGroups, List<Featured> featured, List<DocumentMeta> documentMetaList) {
//		super();
//		this.species = species;
//		this.breadCrumbs = breadCrumbs;
//		this.taxonomyDefinition = taxonomyDefinition;
//		this.resourceData = resourceData;
//		this.fieldData = fieldData;
//		this.facts = facts;
//		this.userGroups = userGroups;
//		this.featured = featured;
//		this.documentMetaList = documentMetaList;
//	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public List<BreadCrumb> getBreadCrumbs() {
		return breadCrumbs;
	}

	public void setBreadCrumbs(List<BreadCrumb> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}

	public TaxonomyDefinition getTaxonomyDefinition() {
		return taxonomyDefinition;
	}

	public void setTaxonomyDefinition(TaxonomyDefinition taxonomyDefinition) {
		this.taxonomyDefinition = taxonomyDefinition;
	}

	public List<ResourceData> getResourceData() {
		return resourceData;
	}

	public void setResourceData(List<ResourceData> resourceData) {
		this.resourceData = resourceData;
	}

	public List<SpeciesFieldData> getFieldData() {
		return fieldData;
	}

	public void setFieldData(List<SpeciesFieldData> fieldData) {
		this.fieldData = fieldData;
	}

	public List<FactValuePair> getFacts() {
		return facts;
	}

	public void setFacts(List<FactValuePair> facts) {
		this.facts = facts;
	}

	public List<UserGroupIbp> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupIbp> userGroups) {
		this.userGroups = userGroups;
	}

	public List<Featured> getFeatured() {
		return featured;
	}

	public void setFeatured(List<Featured> featured) {
		this.featured = featured;
	}

//	public List<DocumentMeta> getDocumentMetaList() {
//		return documentMetaList;
//	}
//
//	public void setDocumentMetaList(List<DocumentMeta> documentMetaList) {
//		this.documentMetaList = documentMetaList;
//	}

}