package org.samir.openshift.selfservices.map;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.samir.openshift.selfservices.dto.ProjectDto;
import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.openshift.api.model.Project;

@Component
public class ProjectMapper {

	private OpenShiftUtils openShiftUtils;

	private SimpleDateFormat format;

	@Autowired
	public ProjectMapper(OpenShiftUtils openShiftUtils) {
		this.openShiftUtils = openShiftUtils;

		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public ProjectDto map(Project source) {
		ProjectDto destination = new ProjectDto();
		
		destination.setName(source.getMetadata().getName());
		destination.setDisplayName(source.getMetadata().getAnnotations().get("openshift.io/display-name"));
		destination.setDescription(source.getMetadata().getAnnotations().get("openshift.io/description"));
		destination.setRequester(source.getMetadata().getAnnotations().get("openshift.io/requester"));
		destination.setQuotaId(source.getMetadata().getAnnotations().get("openshift.io/quota-id"));
		destination.setQuotaOwner(source.getMetadata().getAnnotations().get("openshift.io/quota-owner"));
		destination.setStatus(source.getStatus().getPhase());
		destination.setUrl(openShiftUtils.getMasterURL() + "/console/project/" + source.getMetadata().getName());

		try {
			destination.setCreatedOn(format.parse(source.getMetadata().getCreationTimestamp()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return destination;
	}

	public Project map(ProjectDto source) {
		Project destination = new Project();
			
		destination.setMetadata(new ObjectMeta());
		destination.getMetadata().setName(source.getName());
		destination.getMetadata().setAnnotations(new HashMap<String, String>());
		destination.getMetadata().getAnnotations().put("openshift.io/display-name", source.getDisplayName());				
		destination.getMetadata().getAnnotations().put("openshift.io/description", source.getDescription());
		destination.getMetadata().getAnnotations().put("openshift.io/requester", source.getRequester());
		destination.getMetadata().getAnnotations().put("openshift.io/quota-id", source.getQuotaId());
		destination.getMetadata().getAnnotations().put("openshift.io/quota-owner", source.getQuotaOwner());
		destination.getMetadata().getAnnotations().put("openshift.io/created-by", "Self-Services App");
		
		return destination;
	}
	
	public List<ProjectDto> map(List<Project> sourceList) {
		List<ProjectDto> destinationList = new ArrayList<>();
		
		if (sourceList != null && sourceList.size() > 0) {
			for (Project source : sourceList) {
				destinationList.add(map(source));
			}	
		}
		return destinationList;
	}
}
