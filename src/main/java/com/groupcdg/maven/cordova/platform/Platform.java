package com.groupcdg.maven.cordova.platform;

import com.groupcdg.maven.cordova.BuildMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public abstract class Platform {

	public static enum OS {
		linux("Linux", "cordova"),
		osx("Mac", "cordova"),
		win32("Windows", "cmd", "/c", "cordova");

		public static OS system() throws MojoExecutionException {
			final String name = System.getProperty("os.name");
			for(OS os : values()) if(name.startsWith(os.key)) return os;
			throw new MojoExecutionException("Unsupported operating system: " + name);
		}

		private final String key;
		private final String[] command;

		private OS(String key, String... command) {
			this.key = key;
			this.command = command;
		}

		public List<String> cordova(String... parameters) {
			List<String> cordovaCommand = new ArrayList<>();
			cordovaCommand.addAll(Arrays.asList(command));
			cordovaCommand.addAll(Arrays.asList(parameters));
			return cordovaCommand;
		}

		public List<Platform> platforms(final List<String> configured) {
			final OS os = this;

			List<Platform> selected = new ArrayList<Platform>() {{
				if(configured != null && !configured.isEmpty()) {
					Platform found;
					for (String label : configured)
						if((found = PLATFORM.byLabel(label)) != null && found.supportedOn().contains(os))
							add(found);
				}
			}};

			return selected;
		}
	}

	public static enum PLATFORM {
		amazonFireos(new AmazonFireOS()),
		android(new Android()),
		blackberry10(new Blackberry10()),
		firefoxos(new FirefoxOS()),
		ios(new IOS()),
		windows8(new Windows8()),
		wp8(new Wp8());


		public static Platform byLabel(String label) {
			Platform result = null;
			if(label != null)
				for(PLATFORM p : values())
					if(label.equals(p.instance.getLabel())) {
						result = p.instance; break;
					}
			return result;
		}

		private final Platform instance;

		private PLATFORM(Platform instance) {
			this.instance = instance;
		}

		public Platform get() {
			return instance;
		}
	}


	public List<String> addCommand(OS os) { return os.cordova("platform", "add", getLabel()); }

	public void postBuild(OS system, BuildMojo buildMojo) throws MojoExecutionException {
		createIcons(system, buildMojo);
	}



	protected String getLabel() { return getClass().getSimpleName().toLowerCase(); }

	protected EnumSet<OS> supportedOn() { return EnumSet.of(OS.linux, OS.osx, OS.win32); }

	protected Icons getIcons(BuildMojo buildMojo) {
		Icons icons = new Icons().setTargetDir(getIconsTargetDir(buildMojo));
		if(buildMojo.getIcon() != null) icons.setsource(new File(buildMojo.getResourcesDirectory(), buildMojo.getIcon()));
		return icons;
	}

	protected File getIconsTargetDir(BuildMojo buildMojo) {
		return null;
	}



	private void createIcons(OS system, BuildMojo buildMojo) throws MojoExecutionException {
		getIcons(buildMojo).create(buildMojo);
	}
}
