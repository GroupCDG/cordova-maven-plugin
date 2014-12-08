package com.groupcdg.maven.cordova.platform;

import com.groupcdg.maven.cordova.BuildMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.EnumSet;

public class IOS extends Platform {

	@Override
	public void postBuild(OS system, BuildMojo buildMojo) throws MojoExecutionException {
		super.postBuild(system, buildMojo);
		// TODO package ios app binary

	}

	@Override
	protected EnumSet<OS> supportedOn() {
		return EnumSet.of(OS.osx);
	}

	@Override
	protected Icons getIcons(BuildMojo buildMojo) {
		return super.getIcons(buildMojo)
				.addIcon("icon-40.png", 40)
				.addIcon("icon-40@2x.png", 80)
				.addIcon("icon-50.png", 50)
				.addIcon("icon-50@2x.png", 100)
				.addIcon("icon-60.png", 60)
				.addIcon("icon-60@2x.png", 120)
				.addIcon("icon-60@3x.png", 80)
				.addIcon("icon-72.png", 72)
				.addIcon("icon-72@2x.png", 144)
				.addIcon("icon-76.png", 76)
				.addIcon("icon-76@2x.png", 152)
				.addIcon("icon-small.png", 29)
				.addIcon("icon-small@2x.png", 58)
				.addIcon("icon.png", 57)
				.addIcon("icon@2x.png", 114);
	}

	@Override
	protected File getIconsTargetDir(BuildMojo buildMojo) {
		return new File(buildMojo.getOutputDirectory(), "platforms/ios/" + buildMojo.getEscapedName() + "/Resources/icons/");
	}


	@Override
	protected Icons getSplashScreens(BuildMojo buildMojo) {
		return super.getSplashScreens(buildMojo)
				.addIcon("Default~iphone.png", 320, 480)
				.addIcon("Default@2x~iphone.png", 640, 960)
				.addIcon("Default-568h@2x~iphone.png", 640, 1136)
				.addIcon("Default-Portrait@2x~ipad.png", 1536, 2048)
				.addIcon("Default-Portrait~ipad.png", 768, 1024)
				.addIcon("Default-Landscape~ipad.png", 1024, 768)
				.addIcon("Default-Landscape@2x~ipad.png", 2048, 1536);
	}

	@Override
	protected File getSplashScreensTargetDir(BuildMojo buildMojo) {
		return new File(buildMojo.getOutputDirectory(), "platforms/ios/" + buildMojo.getEscapedName() + "/Resources/splash/");
	}
}
