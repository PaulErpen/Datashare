package com.github.paulerpen.datashare.Storageservice;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	/**
	 * initializes the Storageservice
	 */
    void init();
    /**
     * Stores a file.
     * @param file to be stored
     */
    void store(MultipartFile file);
    /**
     * loads up all previously stored files
     * @return a stream of files
     */
    Stream<Path> loadAll();
    /**
     * resolves the given filename in relation to the root location. 
     * For example if root = "/resources/img" and filename = "img2.jpg"
     * then result = "/resources/img/img2.jpg"
     * @param filename path of the file which is supposed to be resolved
     * @return complete path
     */
    Path load(String filename);
    /**
     * makes a specific file to a resource which is addressable by URL
     * @param filename name of the file, which is to be accesed by URL
     * @return the resource which now adressable
     */
    Resource loadAsResource(String filename);
    /**
     * Deletes all the contents, which are in the root location of the 
     * fileStorage
     */
    void deleteAll();

}
