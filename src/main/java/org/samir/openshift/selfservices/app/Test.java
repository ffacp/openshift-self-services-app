package org.samir.openshift.selfservices.app;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Parameter;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.api.model.ProjectList;
import io.fabric8.openshift.api.model.Template;
import io.fabric8.openshift.api.model.TemplateBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.ExtendedOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class Test {

	public static void main(String[] args) {
		
		Config config = new ConfigBuilder()
				.withMasterUrl("https://192.168.99.100:8443")
				.withUsername("admin")
				.withPassword("admin")
				.withNoProxy("192.168.99.100")
				.build();
		
		OpenShiftClient client = new DefaultOpenShiftClient(config);
		
		new ExtendedOpenShiftClient().
//		
//		KubernetesClient client = new DefaultKubernetesClient(config);

		ProjectList myNs = client.projects().list();
		
		for (Project ns : myNs.getItems()) {
			System.out.println(ns.getMetadata().getName());
		}
		
		client.resourceQuotas()
		
		/*Template t = client.templates().load("").process(arg0) get();
		
		client.templates().createOrReplace(t);
		
		client.templates().inNamespace("thisisatest").withName("eap6-basic-sti").process();
		
		new TemplateBuilder(t).withParameters(new Parameter()).build();*/
	}
}
