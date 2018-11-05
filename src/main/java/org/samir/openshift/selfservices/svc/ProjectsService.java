package org.samir.openshift.selfservices.svc;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.openshift.api.model.Project;

@Service
public class ProjectsService {

	@Autowired
	private OpenShiftUtils openShiftUtils;
	
	private SimpleDateFormat format;
	
	public ProjectsService() {
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public Project getProject(String name) {
		return openShiftUtils.getSystemClient().projects().withName(name).get();
	}
	
	public List<Project> getProjects() {
		return openShiftUtils.getUserClient().projects().list().getItems();
	}

	public void saveProject(Project project) {
		openShiftUtils.getSystemClient().projects().createOrReplace(project);
	}
	
	public void deleteProject(String projectName) {
		openShiftUtils.getUserClient().projects().withName(projectName).withGracePeriod(0).delete();
	}
}
