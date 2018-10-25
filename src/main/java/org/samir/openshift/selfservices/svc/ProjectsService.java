package org.samir.openshift.selfservices.svc;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.samir.openshift.selfservices.dto.Project;
import org.samir.openshift.selfservices.utils.OpenShiftUtils;
import org.samir.openshift.selfservices.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.ObjectReferenceBuilder;
import io.fabric8.kubernetes.api.model.rbac.KubernetesRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.KubernetesRoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.KubernetesRoleRefBuilder;
import io.fabric8.kubernetes.api.model.rbac.KubernetesSubjectBuilder;
import io.fabric8.openshift.api.model.ProjectBuilder;
import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.api.model.ProjectRequestBuilder;
import io.fabric8.openshift.api.model.RoleBinding;
import io.fabric8.openshift.api.model.RoleBindingBuilder;
import io.fabric8.openshift.api.model.Template;

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

		io.fabric8.openshift.api.model.Project request = new ProjectBuilder()
				.withNewMetadata().withName(project.getName())
				.addToAnnotations("openshift.io/display-name", project.getDisplayName())
				.addToAnnotations("openshift.io/description", project.getDescription())

				.addToAnnotations("openshift.io/projectId", project.getName())
				.addToAnnotations("openshift.io/requester", (String) WebUtils.getSessionAttribute("username"))

				.addToAnnotations("openshift.io/quota-id", project.getQuotaId())
				.addToAnnotations("openshift.io/quota-owner", project.getQuotaOwner())
				.addToAnnotations("openshift.io/created-by", "Self-Services App").endMetadata()
				.build();

		openShiftUtils.getSystemClient().projects().create(request);
		
		// system:deployers
		RoleBinding rb = new RoleBindingBuilder()
		        .withNewMetadata().withName("system:deployers").withNamespace(project.getName()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName("system:deployer")
	                .build()
		        )
		        .addNewSubject().withKind("ServiceAccount").withNamespace(project.getName()).withName("deployer").endSubject()
		        .build();
		
		openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).createOrReplace(rb);
		
		// system:image-builder
		rb = new RoleBindingBuilder()
		        .withNewMetadata().withName("system:image-builders").withNamespace(project.getName()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName("system:image-builder")
	                .build()
		        )
		        .addNewSubject().withKind("ServiceAccount").withNamespace(project.getName()).withName("builder").endSubject()
		        .build();
		
		openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).createOrReplace(rb);
		
		// system:image-pullers
		rb = new RoleBindingBuilder()
		        .withNewMetadata().withName("system:image-pullers").withNamespace(project.getName()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName("system:image-puller")
	                .build()
		        )
		        .addNewSubject().withKind("Group").withName("system:serviceaccounts:" + project.getName()).endSubject()
		        .build();
		
		openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).createOrReplace(rb);
		
		// Admin
		rb = new RoleBindingBuilder()
		        .withNewMetadata().withName("admin").withNamespace(project.getName()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName("admin")
	                .build()
		        )
		        .addNewSubject().withKind("User").withName((String) WebUtils.getSessionAttribute("username")).endSubject()
		        .build();
		
		openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).createOrReplace(rb);
		
		//Owner
		rb = new RoleBindingBuilder()
		        .withNewMetadata().withName("owner").withNamespace(project.getName()).endMetadata()
		        .withRoleRef(new ObjectReferenceBuilder()
	                .withKind("Role")
	                .withName("admin")
	                .build()
		        )
		        .addNewSubject().withKind("User").withName(project.getQuotaOwner()).endSubject()
		        .build();
		
		openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).createOrReplace(rb);
      
      
      
		/*io.fabric8.openshift.api.model.Project ocProject = openShiftUtils.getSystemClient()
				.projects().withName(project.getName()).get();
		
		Map<String, String> annotations = ocProject.getMetadata().getAnnotations();
		annotations.put("openshift.io/quota-id", project.getQuotaId());
		annotations.put("openshift.io/quota-owner", project.getQuotaOwner());
		annotations.put("openshift.io/requester", (String) WebUtils.getSessionAttribute("username"));
		annotations.put("openshift.io/created-by", "Self-Services App");*/
		
		//openShiftUtils.getSystemClient()
		//.projects().withName(project.getName()).edit().editMetadata().addToAnnotations("openshift.io/quota-id", project.getQuotaId());
		
		/*Map<String,String> parameters = new HashMap<>();
		parameters.put("PROJECT_NAME", project.getName());
		parameters.put("PROJECT_DISPLAYNAME", project.getDisplayName());
		parameters.put("PROJECT_DESCRIPTION", project.getDescription());
		parameters.put("PROJECT_REQUESTING_USER", (String) WebUtils.getSessionAttribute("username"));
		parameters.put("QUOTA_ID", project.getQuotaId());
		parameters.put("QUOTA_OWNER", project.getQuotaOwner());
		
		KubernetesList l = openShiftUtils.getSystemClient().templates()
		.load(ProjectsService.class.getResourceAsStream("/project-template.yaml"))
		.process(parameters);
		
		for(HasMetadata m : l.getItems()) {
			if(m instanceof io.fabric8.openshift.api.model.Project) {
				io.fabric8.openshift.api.model.Project ocProject = (io.fabric8.openshift.api.model.Project) m;
				openShiftUtils.getSystemClient().projects().create(ocProject);
			}
			else if(m instanceof RoleBinding) {
				RoleBinding roleBinding = (RoleBinding) m;
				openShiftUtils.getSystemClient().roleBindings().inNamespace(project.getName()).create(roleBinding);
			}
		}*/
		
		//openShiftUtils.getSystemClient().lists().create(l);
		
		
		
		
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
