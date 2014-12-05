package com.groupcdg.maven.cordova.platform;

import com.groupcdg.maven.cordova.BuildMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Icons {

	public static class Icon {
		private final String name;
		private final Integer size;

		public Icon(String name, Integer size) {
			this.name = name;
			this.size = size;
		}

		public void create(BuildMojo buildMojo, File source, File targetDir) throws MojoExecutionException {
			buildMojo.run(new ProcessBuilder(Arrays.asList("convert", source.getAbsolutePath(),
							"-resize", "" + size + 'x' + size, "-format", "PNG", "-quality", "1",
							new File(targetDir, name).getAbsolutePath())),
					buildMojo.BUILD, buildMojo.isFailOnError());
		}
	}



	private File source;

	private File targetDir;

	private final List<Icon> icons;



	public Icons() {
		icons = new ArrayList<>();
	}



	public Icons setsource(File source) {
		this.source = source;
		return this;
	}

	public Icons setTargetDir(File targetDir) {
		this.targetDir = targetDir;
		return this;
	}

	public Icons addIcon(String name, int size) {
		icons.add(new Icon(name, size));
		return this;
	}


	public void create(BuildMojo buildMojo) throws MojoExecutionException {
		if(source != null  && targetDir != null && source.canRead() && targetDir.isDirectory())
			for(Icon icon : icons) icon.create(buildMojo, source, targetDir);
	}
}
