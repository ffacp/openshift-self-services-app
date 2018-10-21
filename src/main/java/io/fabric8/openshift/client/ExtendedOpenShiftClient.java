package io.fabric8.openshift.client;

import io.fabric8.kubernetes.api.model.DoneableResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuotaList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class ExtendedOpenShiftClient extends DefaultOpenShiftClient {

	private NamespacedKubernetesClient delegate;

	public ExtendedOpenShiftClient(Config config) throws KubernetesClientException {
		this(new OpenShiftConfig(config));
	}

	public ExtendedOpenShiftClient(OpenShiftConfig config) throws KubernetesClientException {
		super(config);
		this.delegate = new DefaultKubernetesClient(this.httpClient, config);
	}

	/*public MixedOperation<ClusterResourceQuota, ResourceQuotaList, DoneableResourceQuota, Resource<ResourceQuota, DoneableResourceQuota>> resourceQuotas() {
		return delegate.resourceQuotas();
	}*/

}
