package org.samir.openshift.selfservices.svc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.samir.openshift.selfservices.dto.Project;
import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.openshift.api.model.ProjectBuilder;

@Service
public class ProjectsService {

	@Autowired
	private OpenShiftUtils openShiftUtils;
	
	private SimpleDateFormat format;
	
	public ProjectsService() {
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public List<Project> getProjects() {
		return conver(openShiftUtils.getUserClient().projects().list().getItems());
	}

	public void createProject(org.samir.openshift.selfservices.dto.Project project) {

		io.fabric8.openshift.api.model.Project ocProject = new ProjectBuilder().withNewMetadata().withName(project.getName())
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
		openShiftUtils.getUserClient().projects().withName(projectName).withGracePeriod(0).delete();
	}
	
	private Project conver(io.fabric8.openshift.api.model.Project ocProject) {
		Project project = new Project();
		project.setName(ocProject.getMetadata().getName());
		project.setDisplayName(ocProject.getMetadata().getAnnotations().get("openshift.io/display-name"));
		project.setDescription(ocProject.getMetadata().getAnnotations().get("openshift.io/description"));
		project.setRequester(ocProject.getMetadata().getAnnotations().get("openshift.io/requester"));
		project.setQuotaId(ocProject.getMetadata().getAnnotations().get("openshift.io/quota-id"));
		project.setQuotaOwner(ocProject.getMetadata().getAnnotations().get("openshift.io/quota-owner"));
		project.setStatus(ocProject.getStatus().getPhase());
		project.setUrl(openShiftUtils.getMasterURL() + "/console/project/" + project.getName());
		
		try {
			project.setCreatedOn(format.parse(ocProject.getMetadata().getCreationTimestamp()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return project;
	}
	
	private List<Project> conver(List<io.fabric8.openshift.api.model.Project> ocProjects) {
		List<Project> projects = new ArrayList<>();
		if(ocProjects != null && ocProjects.size() > 0) {
			for(io.fabric8.openshift.api.model.Project ocProject : ocProjects) {
				projects.add(conver(ocProject));
			}
		}
		return projects;
	}
}
