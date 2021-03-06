/**
 * 
 */
package com.strandls.species.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.activity.pojo.Activity;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.esmodule.controllers.EsServicesApi;
import com.strandls.esmodule.pojo.ObservationInfo;
import com.strandls.esmodule.pojo.ObservationMapInfo;
import com.strandls.observation.controller.ObservationServiceApi;
import com.strandls.resource.controllers.ResourceServicesApi;
import com.strandls.resource.pojo.License;
import com.strandls.resource.pojo.Resource;
import com.strandls.resource.pojo.ResourceData;
import com.strandls.resource.pojo.SpeciesPull;
import com.strandls.resource.pojo.SpeciesResourcePulling;
import com.strandls.species.Headers;
import com.strandls.species.dao.ContributorDao;
import com.strandls.species.dao.FieldDao;
import com.strandls.species.dao.FieldHeaderDao;
import com.strandls.species.dao.FieldNewDao;
import com.strandls.species.dao.ReferenceDao;
import com.strandls.species.dao.SpeciesDao;
import com.strandls.species.dao.SpeciesFieldAudienceTypeDao;
import com.strandls.species.dao.SpeciesFieldContributorDao;
import com.strandls.species.dao.SpeciesFieldDao;
import com.strandls.species.dao.SpeciesFieldLicenseDao;
import com.strandls.species.dao.SpeciesFieldUserDao;
import com.strandls.species.pojo.Contributor;
import com.strandls.species.pojo.Field;
import com.strandls.species.pojo.FieldDisplay;
import com.strandls.species.pojo.FieldHeader;
import com.strandls.species.pojo.FieldNew;
import com.strandls.species.pojo.FieldRender;
import com.strandls.species.pojo.Reference;
import com.strandls.species.pojo.ShowSpeciesPage;
import com.strandls.species.pojo.Species;
import com.strandls.species.pojo.SpeciesField;
import com.strandls.species.pojo.SpeciesFieldAudienceType;
import com.strandls.species.pojo.SpeciesFieldContributor;
import com.strandls.species.pojo.SpeciesFieldData;
import com.strandls.species.pojo.SpeciesFieldLicense;
import com.strandls.species.pojo.SpeciesFieldUpdateData;
import com.strandls.species.pojo.SpeciesFieldUser;
import com.strandls.species.pojo.SpeciesPullData;
import com.strandls.species.pojo.SpeciesResourceData;
import com.strandls.species.pojo.SpeciesResourcesPreData;
import com.strandls.species.pojo.SpeciesTrait;
import com.strandls.species.service.SpeciesServices;
import com.strandls.taxonomy.controllers.TaxonomyServicesApi;
import com.strandls.taxonomy.pojo.BreadCrumb;
import com.strandls.taxonomy.pojo.CommonNames;
import com.strandls.taxonomy.pojo.CommonNamesData;
import com.strandls.taxonomy.pojo.TaxonomicNames;
import com.strandls.taxonomy.pojo.TaxonomyDefinition;
import com.strandls.traits.controller.TraitsServiceApi;
import com.strandls.traits.pojo.FactValuePair;
import com.strandls.traits.pojo.FactsUpdateData;
import com.strandls.traits.pojo.TraitsValuePair;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.controller.UserGroupSerivceApi;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupSpeciesCreateData;

import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class SpeciesServiceImpl implements SpeciesServices {

	private final Logger logger = LoggerFactory.getLogger(SpeciesServiceImpl.class);

	@Inject
	private Headers headers;

//	Injection of Dao

	@Inject
	private ContributorDao contributorDao;

	@Inject
	private FieldDao fieldDao;

	@Inject
	private FieldNewDao fieldNewDao;

	@Inject
	private FieldHeaderDao fieldHeaderDao;

	@Inject
	private ReferenceDao referenceDao;

	@Inject
	private SpeciesDao speciesDao;

	@Inject
	private SpeciesFieldDao speciesFieldDao;

	@Inject
	private SpeciesFieldUserDao sfUserDao;

	@Inject
	private SpeciesFieldAudienceTypeDao sfAudienceTypeDao;

	@Inject
	private SpeciesFieldContributorDao sfContributorDao;

	@Inject
	private SpeciesFieldLicenseDao sfLicenseDao;

//	injection of services

	@Inject
	private ActivitySerivceApi activityService;

	@Inject
	private EsServicesApi esService;

//	@Inject
//	private DocumentServiceApi documentService;

	@Inject
	private LogActivities logActivity;

	@Inject
	private ObservationServiceApi observationService;

	@Inject
	private ResourceServicesApi resourceServices;

	@Inject
	private SpeciesHelper speciesHelper;

	@Inject
	private UserServiceApi userService;

	@Inject
	private UserGroupSerivceApi ugService;

	@Inject
	private TaxonomyServicesApi taxonomyService;

	@Inject
	private TraitsServiceApi traitService;

	@Override
	public ShowSpeciesPage showSpeciesPage(Long speciesId) {
		try {
			List<ResourceData> resourceData = null;

			Species species = speciesDao.findById(speciesId);
			if (!species.getIsDeleted()) {
//				resource data
				resourceData = resourceServices.getImageResource("SPECIES", species.getId().toString());

//				traits
				List<FactValuePair> facts = traitService.getFacts("species.Species", speciesId.toString());

//				species Field
				List<SpeciesField> speciesFields = speciesFieldDao.findBySpeciesId(speciesId);

				List<SpeciesFieldData> fieldData = new ArrayList<SpeciesFieldData>();

//				segregating on the basis of multiple data
				for (SpeciesField speciesField : speciesFields) {

					SpeciesFieldData speciesFieldData = getSpeciesFieldData(speciesField);

					if (speciesFieldData != null)
						fieldData.add(speciesFieldData);

				}

				List<BreadCrumb> breadCrumbs = taxonomyService
						.getTaxonomyBreadCrumb(species.getTaxonConceptId().toString());

				TaxonomyDefinition taxonomyDefinition = taxonomyService
						.getTaxonomyConceptName(species.getTaxonConceptId().toString());

//				List<DocumentMeta> documentMetaList = documentService
//						.getDocumentByTaxonConceptId(species.getTaxonConceptId().toString());

				List<UserGroupIbp> userGroupList = ugService.getSpeciesUserGroup(speciesId.toString());
				List<Featured> featured = ugService.getAllFeatured("species.Species", speciesId.toString());

//				common name and synonyms
				TaxonomicNames names = taxonomyService.getNames(species.getTaxonConceptId().toString());

//				temporal data
				ObservationInfo observationInfo = esService.getObservationInfo("extended_observation", "_doc",
						species.getTaxonConceptId().toString(), false);

				Map<String, Long> temporalData = observationInfo.getMonthAggregation();

				ShowSpeciesPage showSpeciesPage = new ShowSpeciesPage(species, breadCrumbs, taxonomyDefinition,
						resourceData, fieldData, facts, userGroupList, featured, names, temporalData);

//				ShowSpeciesPage showSpeciesPage = new ShowSpeciesPage(species, breadCrumbs, taxonomyDefinition,
//						resourceData, fieldData, facts, userGroupList, featured, documentMetaList);

				return showSpeciesPage;

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	private SpeciesFieldData getSpeciesFieldData(SpeciesField speciesField) {

		try {

			FieldNew field = fieldNewDao.findById(speciesField.getFieldId());
			FieldHeader fieldHeader = fieldHeaderDao.findByFieldId(field.getId(), 205L);

			SpeciesFieldAudienceType sfAudienceType = sfAudienceTypeDao.findById(speciesField.getId());

			SpeciesFieldLicense sfLicense = sfLicenseDao.findById(speciesField.getId());
			License sfLicenseData = resourceServices.getLicenseResource(sfLicense.getLicenseId().toString());

			List<Reference> references = referenceDao.findBySpeciesFieldId(speciesField.getId());

//			this is actually the attribution of speciesField and a String 

			SpeciesFieldContributor sfAttribution = sfContributorDao.findBySpeciesFieldId(speciesField.getId());
			Contributor attribution = null;
			if (sfAttribution != null)
				attribution = contributorDao.findById(sfAttribution.getContributorId());

//			species field user is the contributor of species field

			List<Long> userList = sfUserDao.findBySpeciesFieldId(speciesField.getId());
			List<UserIbp> contributors = new ArrayList<UserIbp>();
			for (Long userId : userList) {
				UserIbp contributor = userService.getUserIbp(userId.toString());
				contributors.add(contributor);
			}

//			resources of speciesField
			List<ResourceData> sfResources = resourceServices.getImageResource("SPECIES_FIELD",
					speciesField.getId().toString());

			SpeciesFieldData speciesFieldData = new SpeciesFieldData(speciesField.getId(), field.getId(),
					field.getDisplayOrder(), field.getLabel(), fieldHeader.getHeader(), fieldHeader.getDescription(),
					speciesField, references, attribution != null ? attribution.getName() : null, contributors,
					(sfAudienceType != null) ? sfAudienceType.getAudienceType() : null, sfLicenseData, sfResources);

			return speciesFieldData;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

//	migration of field to new structure

	Map<String, FieldNew> conceptMap = new HashMap<String, FieldNew>();
	Map<String, FieldNew> categoryMap = new HashMap<String, FieldNew>();
	Map<String, FieldNew> subCatMap = new HashMap<String, FieldNew>();

	Map<String, Long> count = new HashMap<String, Long>();
	Long fieldListSize = null;

	@Override
	public void migrateField() {

		try {
			List<Field> fieldList = fieldDao.findByLanguageId(205L);
			fieldListSize = Long.parseLong(String.valueOf(fieldList.size()));
			for (Field field : fieldList) {

				String header = field.getConcept();
				String lable = "Concept";

				if (!conceptMap.containsKey(header)) {
					FieldNew fieldNew = new FieldNew((field.getCategory() == null) ? field.getId() : null, null,
							Long.parseLong(String.valueOf((conceptMap.size() + 1))), lable, header);

//					save the object

					if (fieldNew.getId() == null) {
						fieldListSize++;
						fieldNew.setId(fieldListSize);

					}

					fieldNewDao.save(fieldNew);

					FieldHeader fieldHeader = new FieldHeader(null, fieldNew.getId(), header,
							(field.getCategory() == null) ? field.getDescription() : null,
							(field.getCategory() == null) ? field.getUrlIdentifier() : null, field.getLanguageId());

					fieldHeaderDao.save(fieldHeader);

					conceptMap.put(header, fieldNew);
					count.put(header, 1L);

					if (field.getCategory() != null) {
						saveCategory(field);
					}

				} else {
					if (field.getCategory() != null) {
						if (count.get(field.getConcept() + ":" + field.getCategory()) == null) {
							Long value = count.get(header);
							count.put(header, value + 1);
						}
						saveCategory(field);
					}

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	private void saveCategory(Field field) {

		try {
			String key = field.getConcept() + ":" + field.getCategory();
			String header = field.getCategory();
			String lable = "Category";
			if (!categoryMap.containsKey(key)) {

				Long fieldId = null;

				if (field.getSubCategory() == null) {
					fieldId = field.getId();
				} else {
					fieldListSize++;
					fieldId = fieldListSize;
				}

				FieldNew fieldNew = new FieldNew(fieldId, conceptMap.get(field.getConcept()).getId(),
						count.get(field.getConcept()), lable, header);

				fieldNewDao.save(fieldNew);

				FieldHeader fieldHeader = new FieldHeader(null, fieldNew.getId(), header, field.getDescription(),
						field.getUrlIdentifier(), field.getLanguageId());

//				save the obj

				fieldHeaderDao.save(fieldHeader);

				categoryMap.put(key, fieldNew);
				count.put(key, 1L);

				if (field.getSubCategory() != null) {
					saveSubCategory(field);
				}

			} else {

				if (field.getSubCategory() != null) {
					saveSubCategory(field);
					if (count.get(field.getCategory() + ":" + field.getSubCategory()) == null) {
						Long value = count.get(key);
						count.put(key, value + 1);
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	private void saveSubCategory(Field field) {
		try {
			String key = field.getCategory() + ":" + field.getSubCategory();
			String header = field.getSubCategory();
			String label = "SubCategory";
			if (!subCatMap.containsKey(key)) {
				FieldNew fieldNew = new FieldNew(field.getId(),
						categoryMap.get(field.getConcept() + ":" + field.getCategory()).getId(),
						count.get(field.getConcept() + ":" + field.getCategory()), label, header);

				fieldNewDao.save(fieldNew);
				FieldHeader fieldHeader = new FieldHeader(null, fieldNew.getId(), header, field.getDescription(),
						field.getUrlIdentifier(), field.getLanguageId());

//				save obj

				fieldHeaderDao.save(fieldHeader);

				subCatMap.put(key, fieldNew);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public List<FieldRender> getFields() {

		List<FieldRender> renderList = new ArrayList<FieldRender>();

//		extract all the concept fields in display order
		List<FieldNew> concpetFields = fieldNewDao.findNullParent();

		for (FieldNew concpetField : concpetFields) {

			List<FieldDisplay> categorySubCat = new ArrayList<FieldDisplay>();

//			extract all the category fields in display order
			List<FieldNew> categoryFields = fieldNewDao.findByParentId(concpetField.getId());
			for (FieldNew catField : categoryFields) {
//				extract all the subCategory fields in display order
				List<FieldNew> subCatField = fieldNewDao.findByParentId(catField.getId());
				categorySubCat.add(new FieldDisplay(catField, subCatField));
			}

			renderList.add(new FieldRender(concpetField, categorySubCat));
		}

		return renderList;

	}

	@Override
	public List<SpeciesTrait> getSpeciesTraitsByTaxonomyId(Long taxonomyId) {
		try {
			List<TraitsValuePair> traitValuePairLIst = traitService.getSpeciesTraits(taxonomyId.toString());
			List<SpeciesTrait> arranged = arrangeTraits(traitValuePairLIst);
			return arranged;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private List<SpeciesTrait> arrangeTraits(List<TraitsValuePair> traitValuePairList) {

		TreeMap<String, List<TraitsValuePair>> arrangedPair = new TreeMap<String, List<TraitsValuePair>>();

		for (TraitsValuePair traitsValuePair : traitValuePairList) {
			String name = "";
			Long fieldId = traitsValuePair.getTraits().getFieldId();
			name = fieldHierarchyString(fieldId);
			System.out.println(name);
			if (arrangedPair.containsKey(name)) {
				List<TraitsValuePair> pairList = arrangedPair.get(name);
				pairList.add(traitsValuePair);
				arrangedPair.put(name, pairList);
			} else {
				List<TraitsValuePair> pairList = new ArrayList<TraitsValuePair>();
				pairList.add(traitsValuePair);
				arrangedPair.put(name, pairList);
			}

		}

		List<SpeciesTrait> result = new ArrayList<SpeciesTrait>();
		for (Entry<String, List<TraitsValuePair>> entry : arrangedPair.entrySet()) {
			result.add(new SpeciesTrait(entry.getKey(), entry.getValue()));
		}

		return result;

	}

	private String fieldHierarchyString(Long fieldId) {
		FieldNew fieldNew = null;
		String name = "";
		do {
			fieldNew = fieldNewDao.findById(fieldId);
			name = fieldHeaderDao.findByFieldId(fieldNew.getId(), 205L).getHeader() + " > " + name;
			fieldId = fieldNew.getParentId();

		} while (fieldNew.getParentId() != null);
		name = name.substring(0, name.length() - 3);
		return name;
	}

	@Override
	public List<SpeciesTrait> getAllSpeciesTraits() {
		try {
			List<TraitsValuePair> traitsValuePairList = traitService.getAllSpeciesTraits();
			List<SpeciesTrait> arranged = arrangeTraits(traitsValuePairList);
			return arranged;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<UserGroupIbp> updateUserGroup(HttpServletRequest request, String speciesId,
			UserGroupSpeciesCreateData ugSpeciesCreateData) {
		try {
			ugService = headers.addUserGroupHeader(ugService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<UserGroupIbp> result = ugService.createUserGroupSpeciesMapping(speciesId, ugSpeciesCreateData);
			updateLastRevised(Long.parseLong(speciesId));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Featured> createFeatured(HttpServletRequest request, FeaturedCreate featuredCreate) {
		try {
			FeaturedCreateData featuredCreateData = new FeaturedCreateData();
			featuredCreateData.setFeaturedCreate(featuredCreate);
			featuredCreateData.setMailData(null);
			ugService = headers.addUserGroupHeader(ugService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<Featured> result = ugService.createFeatured(featuredCreateData);
			updateLastRevised(featuredCreate.getObjectId());
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Featured> unFeatured(HttpServletRequest request, String speciesId, List<Long> userGroupList) {
		try {

			UserGroupMappingCreateData userGroupData = new UserGroupMappingCreateData();
			userGroupData.setUserGroups(userGroupList);
			userGroupData.setUgFilterData(null);
			userGroupData.setMailData(null);
			ugService = headers.addUserGroupHeader(ugService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<Featured> result = ugService.unFeatured("species", speciesId, userGroupData);
			updateLastRevised(Long.parseLong(speciesId));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void updateLastRevised(Long speciesId) {
		Species species = speciesDao.findById(speciesId);
		species.setLastUpdated(new Date());
		speciesDao.update(species);
	}

	@Override
	public List<FactValuePair> updateTraits(HttpServletRequest request, String speciesId, String traitId,
			FactsUpdateData factsUpdateData) {
		try {
			traitService = headers.addTraitsHeader(traitService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<FactValuePair> result = traitService.updateTraits("species.Species", speciesId, traitId,
					factsUpdateData);
			updateLastRevised(Long.parseLong(speciesId));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public SpeciesFieldData updateSpeciesField(HttpServletRequest request, Long speciesId,
			SpeciesFieldUpdateData sfdata) {

		try {

			Boolean isValid = speciesHelper.validateSpeciesFieldData(sfdata);
			if (!isValid)
				return null;
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());

//			this needs to be checked
			List<Long> sfUserList = sfUserDao.findBySpeciesFieldId(sfdata.getSpeciesFieldId());
			JSONArray userRole = (JSONArray) profile.getAttribute("roles");

			if (userRole.contains("ROLE_ADMIN") || sfUserList.contains(userId)) {

//				speciesField core update
				SpeciesField speciesField = updateCreateSpeciesField(speciesId, userId, sfdata);
				if (speciesField == null)
					return null;

//				attribution update
//				this is actually the attribution of speciesField and a String
				SpeciesFieldContributor sfAttribution = sfContributorDao.findBySpeciesFieldId(speciesField.getId());
				Contributor attribution = null;
				if (sfAttribution != null) {
					attribution = contributorDao.findById(sfAttribution.getContributorId());
				}
				if (attribution != null && sfdata.getIsEdit()) {
//					update attributions
					attribution.setName(sfdata.getAttributions());
					contributorDao.update(attribution);

				} else {
//						create new attributions
					Contributor contributor = new Contributor(null, sfdata.getAttributions(), null);
					contributor = contributorDao.save(contributor);

					sfAttribution = new SpeciesFieldContributor(sfdata.getSpeciesFieldId(), contributor.getId(), null);
					sfContributorDao.save(sfAttribution);
				}

//				species field resource
				updateCreateSpeciesResource(request, "SPECIES_FIELD", speciesField.getId().toString(),
						sfdata.getIsEdit(), sfdata.getSpeciesFieldResource());

//				sf user contributor
				if (sfdata.getIsEdit()) {
//					deleting existing contributors
					for (Long existingUserId : sfUserList) {
						if (!sfdata.getContributorIds().contains(existingUserId)) {
							SpeciesFieldUser sfUser = sfUserDao.findBySpeciesFieldIdUserId(speciesField.getId(),
									existingUserId);
							sfUserDao.delete(sfUser);
						}
					}
//					adding new user contributors
					for (Long newUserId : sfdata.getContributorIds()) {
						if (!sfUserList.contains(newUserId)) {
							SpeciesFieldUser sfUser = new SpeciesFieldUser(speciesField.getId(), newUserId, null);
							sfUserDao.save(sfUser);
						}
					}
				} else {
					for (Long newUserId : sfdata.getContributorIds()) {
						SpeciesFieldUser sfUser = new SpeciesFieldUser(speciesField.getId(), newUserId, null);
						sfUserDao.save(sfUser);
					}
				}

//				sf license 
				if (sfdata.getIsEdit()) {
					SpeciesFieldLicense sfLicense = sfLicenseDao.findById(speciesField.getId());
					if (!sfLicense.getLicenseId().equals(sfdata.getLicenseId())) {
						sfLicense.setLicenseId(sfdata.getLicenseId());
						sfLicenseDao.update(sfLicense);
					}
				} else {
					SpeciesFieldLicense sfLicense = new SpeciesFieldLicense(speciesField.getId(),
							sfdata.getLicenseId());
					sfLicenseDao.save(sfLicense);
				}

				String fieldHierarchy = fieldHierarchyString(sfdata.getFieldId());

				if (sfdata.getIsEdit()) {
					updateLastRevised(speciesId);
					String desc = "Updated species field : " + fieldHierarchy;
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), desc, speciesId, speciesId,
							"species", speciesField.getId(), "Updated species field", null);
				} else {
					String desc = "Added species field : " + fieldHierarchy;
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), desc, speciesId, speciesId,
							"species", speciesField.getId(), "Added species field", null);
				}

				return getSpeciesFieldData(speciesField);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private List<Resource> updateCreateSpeciesResource(HttpServletRequest request, String objectType, String objectId,
			Boolean isEdit, List<SpeciesResourceData> speciesResourceData) {

		try {
			if (speciesResourceData != null && !speciesResourceData.isEmpty()) {
				List<Resource> resources = speciesHelper.createResourceMapping(request, objectType,
						speciesResourceData);

				if (resources != null && !resources.isEmpty()) {
					resourceServices = headers.addResourceHeaders(resourceServices,
							request.getHeader(HttpHeaders.AUTHORIZATION));

					if (isEdit) {
						resources = resourceServices.updateResources(objectType, objectId, resources);
					} else {
						resources = resourceServices.createResource(objectType, objectId, resources);
					}
					return resources;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	private SpeciesField updateCreateSpeciesField(Long speciesId, Long uploaderId, SpeciesFieldUpdateData sfData) {

		SpeciesField field = null;
		if (sfData.getIsEdit()) {
//			update the species field
			field = speciesFieldDao.findById(sfData.getSpeciesFieldId());
			if (field.getIsDeleted())
				return null;
			field.setDescription(sfData.getSfDescription());
			field.setStatus(sfData.getSfStatus());
			field.setLastUpdated(new Date());

			speciesFieldDao.update(field);

		} else {
//			create the species field
			field = new SpeciesField(null, 0L, sfData.getSfDescription(), sfData.getFieldId(), speciesId,
					sfData.getSfStatus(), "species.SpeciesField", null, new Date(), new Date(), new Date(), uploaderId,
					205L, null, false);
			field = speciesFieldDao.save(field);
		}

		return field;
	}

	@Override
	public Boolean removeSpeciesField(HttpServletRequest request, Long speciesfieldId) {
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		JSONArray userRoles = (JSONArray) profile.getAttribute("roles");
		Long userId = Long.parseLong(profile.getId());
		List<Long> sfUserList = sfUserDao.findBySpeciesFieldId(speciesfieldId);

		if (userRoles.contains("ROLE_ADMIN") || sfUserList.contains(userId)) {

			SpeciesField speciesfield = speciesFieldDao.findById(speciesfieldId);
			speciesfield.setIsDeleted(true);
			speciesFieldDao.update(speciesfield);

			updateLastRevised(speciesfield.getSpeciesId());

			String fieldHierarchy = fieldHierarchyString(speciesfield.getFieldId());

			String desc = "Deleted species field : " + fieldHierarchy;
			logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), desc, speciesfield.getSpeciesId(),
					speciesfield.getSpeciesId(), "species", speciesfield.getId(), "Deleted species field", null);

			return true;
		}

		return false;
	}

	@Override
	public List<CommonNames> updateAddCommonName(HttpServletRequest request, Long speciesId,
			CommonNamesData commonNamesData) {
		try {
			taxonomyService = headers.addTaxonomyHeader(taxonomyService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<CommonNames> result = taxonomyService.updateAddCommonNames(commonNamesData);
			updateLastRevised(speciesId);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<CommonNames> removeCommonName(HttpServletRequest request, Long speciesId, String commonNameId) {
		try {
			taxonomyService = headers.addTaxonomyHeader(taxonomyService, request.getHeader(HttpHeaders.AUTHORIZATION));
			List<CommonNames> result = taxonomyService.removeCommonName(commonNameId);
			updateLastRevised(speciesId);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<SpeciesPull> getObservationResource(Long speciesId, Long offset) {
		try {
			Species species = speciesDao.findById(speciesId);
			ObservationInfo observationInfo = esService.getObservationInfo("extended_observation", "_doc",
					species.getTaxonConceptId().toString(), false);
			List<ObservationMapInfo> observations = observationInfo.getLatlon();
			List<Long> objectIds = new ArrayList<Long>();
			for (ObservationMapInfo obs : observations) {
				objectIds.add(obs.getId());
			}

			List<SpeciesPull> resources = resourceServices.getBulkResources("observation", offset.toString(),
					objectIds);
			return resources;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<ResourceData> pullResource(HttpServletRequest request, Long speciesId,
			List<SpeciesPullData> speciesPullData) {

		try {

			Species species = speciesDao.findById(speciesId);
			Set<Long> observationIds = new HashSet<Long>();
			List<Long> resourcesIds = new ArrayList<Long>();

			for (SpeciesPullData speciesPull : speciesPullData) {
				resourcesIds.add(speciesPull.getResourceId());
				observationIds.add(speciesPull.getObservationId());
			}
			SpeciesResourcePulling resourcePulling = new SpeciesResourcePulling();
			resourcePulling.setSpeciesId(speciesId);
			resourcePulling.setResourcesIds(resourcesIds);

			resourceServices = headers.addResourceHeaders(resourceServices,
					request.getHeader(HttpHeaders.AUTHORIZATION));
			List<ResourceData> resourceResult = resourceServices.pullResource(resourcePulling);

//			validate the observation

			List<Long> observationIdsList = new ArrayList<Long>(observationIds);
			observationService = headers.addObservationHeader(observationService,
					request.getHeader(HttpHeaders.AUTHORIZATION));
			observationService.speciesPullObservationValidation(species.getTaxonConceptId().toString(),
					observationIdsList);

			return resourceResult;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<ResourceData> getSpeciesResources(HttpServletRequest request, Long speciesId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userRoles = (JSONArray) profile.getAttribute("roles");

			if (userRoles.contains("ROLE_ADMIN")) {
				List<ResourceData> resourceData = resourceServices.getImageResource("SPECIES", speciesId.toString());
				return resourceData;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<ResourceData> updateSpciesResources(HttpServletRequest request, Long speciesId,
			List<SpeciesResourcesPreData> preDataList) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userRoles = (JSONArray) profile.getAttribute("roles");

			if (userRoles.contains("ROLE_ADMIN")) {
				List<SpeciesPullData> speciesPullDatas = new ArrayList<SpeciesPullData>();
				List<SpeciesResourceData> speciesResourceData = new ArrayList<SpeciesResourceData>();
				for (SpeciesResourcesPreData preData : preDataList) {
					if (preData.getObservationId() != null) {
						speciesPullDatas.add(new SpeciesPullData(preData.getObservationId(), preData.getResourcesId()));
					} else {
						speciesResourceData.add(new SpeciesResourceData(preData.getPath(), preData.getUrl(),
								preData.getType(), preData.getCaption(), preData.getRating(), preData.getLicenceId()));
					}
				}
				pullResource(request, speciesId, speciesPullDatas);
				updateCreateSpeciesResource(request, "SPECIES", speciesId.toString(), true, speciesResourceData);
				updateLastRevised(speciesId);
				logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), null, speciesId, speciesId,
						"species", speciesId, "Updated species gallery", null);

				return getSpeciesResources(request, speciesId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Activity addSpeciesComment(HttpServletRequest request, CommentLoggingData loggingData) {
		try {
			activityService = headers.addActivityHeader(activityService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Activity result = activityService.addComment("species", loggingData);
			updateLastRevised(loggingData.getRootHolderId());
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}