package com.armada.deployerhost;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
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

	protected HostDockerCmdExecFactory dockerCmdExecFactory = new HostDockerCmdExecFactory(DockerClientBuilder.getDefaultDockerCmdExecFactory());


	public String sayHello() {
        return "Hello world!";
    }


	public void connectToDockerHost() {
		//TODO: move this over to properties file like in the client
//		DockerClientConfig.DockerClientConfigBuilder configBuilder = DockerClientConfig.createDefaultConfigBuilder();
//		configBuilder.withVersion("1.12");
//		configBuilder.withUri("http://localhost:2375");
//		configBuilder.withUsername("armadaproject");
//		configBuilder.withPassword("dockerrocks");
//		configBuilder.withEmail("DePaul2015SEStudioTeam1@gmail.com@gmail.com");
//		DockerClientConfig config = configBuilder.build();
//		dockerHostClient = DockerClientBuilder.getInstance(config).build();
//		return "connected to: " + dockerHostClient.toString();

		DockerClientConfig.DockerClientConfigBuilder b = DockerClientConfig.createDefaultConfigBuilder();
		LOG.info(b.toString());
		dockerHostClient = DockerClientBuilder.getInstance(b.build())
				.withDockerCmdExecFactory(dockerCmdExecFactory).build();

		LOG.info("connected to: " + dockerHostClient.toString());
	}



	public String buildImageFromDockerfile(String fileLocation, String tag) {

		File baseDir = new File(fileLocation);
		BuildImageCmd buildImageCmd = dockerHostClient.buildImageCmd(baseDir);
		LOG.info("Tag of new image: " + buildImageCmd.getTag());

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

	public void pushImage(String imageName) {

		String username = dockerHostClient.authConfig().getUsername();
		asString(dockerHostClient.pushImageCmd(imageName).exec());


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
