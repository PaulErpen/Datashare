package com.github.paulerpen.datashare.ManagerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class MyLoadBalancer {

	public String getConnectionURI(String requiredService, String requiredMapping,DiscoveryClient discoveryClient) {
		System.out.println(discoveryClient);
    	List<ServiceInstance> instances = discoveryClient.getInstances(requiredService);
    	ServiceInstance instance = instances.get((int) (Math.random()%instances.size()));
    	return instance.getUri()+requiredMapping;
	}

}
