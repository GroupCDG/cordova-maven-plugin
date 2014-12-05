package com.groupcdg.maven.cordova.platform;

import com.groupcdg.maven.cordova.BuildMojo;

import java.io.File;

public class Android extends Platform {

	@Override
	protected Icons getIcons(BuildMojo buildMojo) {
		return super.getIcons(buildMojo)
				.addIcon("drawable/icon.png", 96)
				.addIcon("drawable-hdpi/icon.png", 72)
				.addIcon("drawable-ldpi/icon.png", 36)
				.addIcon("drawable-mdpi/icon.png", 48)
				.addIcon("drawable-xhdpi/icon.png", 96);
	}

	@Override
	protected File getIconsTargetDir(BuildMojo buildMojo) {
		return new File(buildMojo.getOutputDirectory(), "platforms/android/res/");
	}
}
