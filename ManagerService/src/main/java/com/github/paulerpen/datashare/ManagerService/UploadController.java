package com.github.paulerpen.datashare.ManagerService;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The controller which takes care of all the request concerning the upload
 * of files.
 * @author paul
 *
 */
@Controller
public class UploadController {
	
	//service which handles file storage and makes files downloadable
    //private final StorageService storageService;
    private final String requiredService = "storageservice";
    
    @Autowired
    private DiscoveryClient discoveryClient;

   /* @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }*/

    @RequestMapping(value = "/hub", method = RequestMethod.GET)
    @ResponseBody
    public String listUploadedFiles(Model model,
			HttpServletResponse response,
			MyLoadBalancer myLoadBalancer) throws IOException {
    	RestTemplate template = new RestTemplate();
    	return template.getForObject(myLoadBalancer.getConnectionURI(requiredService, "/hub",discoveryClient), String.class);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,
    		MyLoadBalancer myLoadBalancer) {
    	
    	RestTemplate template = new RestTemplate();
    	ServiceInstance instance =  discoveryClient.getInstances(requiredService).get(0);
    	String url = instance.getUri()+"/"+requiredService+"/files/"+filename;
    	System.out.println(url);
    	return template.getForObject(myLoadBalancer.getConnectionURI(requiredService, "/"+requiredService+"/files/"+filename,discoveryClient), ResponseEntity.class);
    }

    @RequestMapping(value = "/hub", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            MyLoadBalancer myLoadBalancer) throws IOException {
    	
    	ServiceInstance instance =  discoveryClient.getInstances(requiredService).get(0);
    	String url = myLoadBalancer.getConnectionURI(requiredService, "/upload",discoveryClient);
    	System.out.println(url);
    	
    	LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    	String fileName = file.getOriginalFilename();
    	System.out.println(fileName);
    	ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()){
            @Override
            public String getFilename(){
                return fileName;
            }
        };

        map.add("file", contentsAsResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);//    	try {
        return "redirect:/hub";     
    }
}