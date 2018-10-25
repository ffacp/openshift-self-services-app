package org.samir.openshift.selfservices.app;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class Test {

	/*public static void main(String[] args) {
		
		Config config = new ConfigBuilder()
				.withMasterUrl("https://sb1ocpconsole.belldev.dev.bce.ca:8443")
				.withUsername("samir.bassaly")
				.withPassword("")
				//.withNoProxy("fastweb.int.bell.ca:8083")
				.withTrustCerts(true)
				.build();
		
		OpenShiftClient client = new DefaultOpenShiftClient(config);

		ProjectList myNs = client.projects().list();
		
		for (Project ns : myNs.getItems()) {
			System.out.println(ns.getMetadata().getName());
		}
		
		Map<String,String> parameters = new HashMap<>();
		parameters.put("PROJECT_NAME", project.getName());
		parameters.put("PROJECT_DISPLAYNAME", project.getDisplayName());
		parameters.put("PROJECT_DESCRIPTION", project.getDescription());
		parameters.put("PROJECT_REQUESTING_USER", (String) WebUtils.getSessionAttribute("username"));
		parameters.put("QUOTA_ID", project.getQuotaId());
		parameters.put("QUOTA_OWNER", project.getQuotaOwner());
		
		KubernetesList l = openShiftUtils.getSystemClient().templates()
		.load(ProjectsService.class.getResourceAsStream("/project-template.yaml"))
		.process(parameters);
		
		KubernetesList l = openShiftUtils.getSystemClient().templates().inNamespace("default")
		.withName("project-request").process(parameters);
		
		openShiftUtils.getSystemClient().lists().create(l);
	
		
	
	}*/
}
