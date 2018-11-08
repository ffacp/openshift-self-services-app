package org.samir.openshift.selfservices.svc;

import java.util.List;

import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import io.fabric8.openshift.api.model.Project;

@Service
public class ProjectsService {

	@Autowired
	private OpenShiftUtils openShiftUtils;

	public Project getProject(String name) {
		try {
			return openShiftUtils.getSystemClient().projects().withName(name).get();
		} catch (HttpClientErrorException e) {
			if(e.getMessage().contains("404")) {
				return null;
			} else {
				throw e;
			}
		}
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
