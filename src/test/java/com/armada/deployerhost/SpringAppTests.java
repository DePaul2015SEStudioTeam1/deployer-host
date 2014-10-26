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

	@Before
	public void setUp() {
		System.out.println(dockerHostService.connectToDockerHost());
	}


	@Test
	public void testPushingToDockerhub() {

		dockerHostService.pullImageBusybox();

	}

	@Test
	public void testConnectToDockerHost() {
		System.out.println(dockerHostService.connectToDockerHost());
	}

	@Test
	public void testBuildImageFromDockerfile() {
		String path = "/home/jodavidson/Documents/DockerProj/images/testsinatra";
		System.out.println(dockerHostService.buildImageFromDockerfile(path));
	}


    @Test
    public void testSayHello() {
        Assert.assertEquals("Hello world!", dockerHostService.sayHello());
    }
}
