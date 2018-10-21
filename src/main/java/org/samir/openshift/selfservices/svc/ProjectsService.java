package org.samir.openshift.selfservices.svc;

import java.util.List;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectBuilder;

@Service
public class ProjectsService {

	@Autowired
	private OpenShiftUtils openShiftUtils;

	public List<Project> getProjects() {
		return openShiftUtils.getUserClient().projects().list().getItems();
	}

	public void createProject(org.samir.openshift.selfservices.dto.Project project) {

		Project ocProject = new ProjectBuilder().withNewMetadata().withName(project.getName())
				.addToAnnotations("openshift.io/display-name", project.getDisplayName())
				.addToAnnotations("openshift.io/description", project.getDescription())

				.addToAnnotations("openshift.io/projectId", project.getName())
				.addToAnnotations("openshift.io/requester", (String) WebUtils.getSessionAttribute("username"))

				.addToAnnotations("openshift.io/quota-id", project.getQuotaId())
				.addToAnnotations("openshift.io/quota-owner", project.getQuotaOwner())
				.addToAnnotations("openshift.io/created-by", "Self-Services App").endMetadata().build();

		openShiftUtils.getSystemClient().projects().create(ocProject);
	}

	public void deleteProject(String projectName) {
		openShiftUtils.getSystemClient().projects().withName(projectName).withGracePeriod(0).delete();
	}
}
