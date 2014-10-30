package com.armada.deployerhost;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {
    @Autowired
    private DockerHostService dockerHostService;
	String username;

	@Before
	public void setUp() {
		dockerHostService.connectToDockerHost();
//		username = dockerHostService.authConfig().getUsername();

	}


	@Test
	public void testPushingToDockerhub() {
		String imageName = "armadaproject/helloworldtest2";
		dockerHostService.pushImage(imageName);
	}

	@Test
	public void testConnectToDockerHost() {
		dockerHostService.connectToDockerHost();
	}

	@Test
	public void testBuildImageFromDockerfile() {
		String path = "/home/jodavidson/Documents/DockerProj/images/helloworldtest";
		System.out.println(dockerHostService.buildImageFromDockerfile(path, "jdavidson091/testtag"));
	}


    @Test
    public void testSayHello() {
        Assert.assertEquals("Hello world!", dockerHostService.sayHello());
    }
}
