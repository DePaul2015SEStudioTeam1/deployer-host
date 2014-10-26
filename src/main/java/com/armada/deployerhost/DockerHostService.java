package com.armada.deployerhost;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

@Component
public class DockerHostService {

	public static final Logger LOG = LoggerFactory
			.getLogger(DockerHostService.class);

	private DockerClient dockerHostClient;

    public String sayHello() {
        return "Hello world!";
    }


	public String connectToDockerHost() {
		//TODO: move this over to properties file like in the client
		DockerClientConfig.DockerClientConfigBuilder configBuilder = DockerClientConfig.createDefaultConfigBuilder();
		configBuilder.withVersion("1.12");
		configBuilder.withUri("http://localhost:2375");
		configBuilder.withUsername("armadaproject");
		configBuilder.withPassword("dockerrocks");
		configBuilder.withEmail("DePaul2015SEStudioTeam1@gmail.com@gmail.com");
		DockerClientConfig config = configBuilder.build();
		dockerHostClient = DockerClientBuilder.getInstance(config).build();
		return "connected to: " + dockerHostClient.toString();
	}





	public String buildImageFromDockerfile(String fileLocation) {

		File baseDir = new File(fileLocation);

//		InputStream response = dockerHostClient.buildImageCmd(baseDir).exec();
		BuildImageCmd buildImageCmd = dockerHostClient.buildImageCmd(baseDir).withTag("jdavidson091/sinatratest");
		System.out.println("Tag of new image: " + buildImageCmd.getTag());


		InputStream response = dockerHostClient.buildImageCmd(baseDir).exec();


		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line);
				LOG.info(line);
			}
		}catch (IOException ioe) {}
		finally {
			IOUtils.closeQuietly(response);
		}
		return "exiting";
	}

	public String pushImage(String imageName) {
		return null;
	}

	protected String asString(InputStream response)  {

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response, "UTF-8");

			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + (itr.hasNext() ? "\n" : ""));
				//LOG.info("line: "+line);
			}

			return logwriter.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

}
