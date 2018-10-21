package org.samir.openshift.selfservices.app;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectList;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class Test {

	public static void main(String[] args) {
		
		Config config = new ConfigBuilder()
				.withMasterUrl("https://sb1ocpconsole.belldev.dev.bce.ca:8443")
				.withUsername("samir.bassaly")
				.withPassword("")
				//.withNoProxy("fastweb.int.bell.ca:8083")
				.withTrustCerts(true)
				.build();
		
		OpenShiftClient client = new DefaultOpenShiftClient(config);
		
		
//		
//		KubernetesClient client = new DefaultKubernetesClient(config);

		ProjectList myNs = client.projects().list();
		
		for (Project ns : myNs.getItems()) {
			System.out.println(ns.getMetadata().getName());
		}
		
	
		
		/*Template t = client.templates().load("").process(arg0) get();
		
		client.templates().createOrReplace(t);
		
		client.templates().inNamespace("thisisatest").withName("eap6-basic-sti").process();
		
		new TemplateBuilder(t).withParameters(new Parameter()).build();*/
	}
}
