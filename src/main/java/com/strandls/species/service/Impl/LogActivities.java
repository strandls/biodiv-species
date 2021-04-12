/**
 * 
 */
package com.strandls.species.service.Impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.species.Headers;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class LogActivities {

	@Inject
	private ActivitySerivceApi activityService;

	@Inject
	private Headers headers;

	private final Logger logger = LoggerFactory.getLogger(LogActivities.class);

	public void LogActivity(String authHeader, String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType, MailData mailData) {

		try {
			SpeciesActivityLogging activityLogging = new SpeciesActivityLogging();
			activityLogging.setActivityDescription(activityDescription);
			activityLogging.setActivityId(activityId);
			activityLogging.setActivityType(activityType);
			activityLogging.setRootObjectId(rootObjectId);
			activityLogging.setRootObjectType(rootObjectType);
			activityLogging.setSubRootObjectId(subRootObjectId);
			activityLogging.setMailData(mailData);
			activityService = headers.addActivityHeader(activityService, authHeader);
			activityService.logSpeciesActivities(activityLogging);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
