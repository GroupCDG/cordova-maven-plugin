/*
 * Copyright 2014 Computing Distribution Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.groupcdg.maven.cordova;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractCordovaMojo extends AbstractMojo {

	protected static final String CREATE_DIRECTORY_ERROR_MESSAGE = "Could not create directory ";


	private static final String LOGS_DIRECTORY = "logs";

	private static final String OUT_LOG_SUFFIX = ".out";

	private static final String ERR_LOG_SUFFIX = ".err";

	private static final String COMMAND_MESSAGE_PREFIX = "Running: ";

	@Parameter(defaultValue = "${project.build.directory}/cordova", required = true, readonly = true)
	private File cordovaDirectory;

	@Parameter(defaultValue = "${project.basedir}/src/main/webapp", required = true, readonly = true)
	private String defaultFileSet;

	@Parameter(property = "project", defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	@Parameter(property = "command", defaultValue = "cordova", required = true)
	private String command;

	@Parameter(property = "name", defaultValue = "${project.name}", required = true)
	private String name;

	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/cordova", required = true)
	private File outputDirectory;

	@Parameter(property = "fileSets")
	private List<FileSet> fileSets;

	@Parameter(property = "platforms", required = true)
	private List<String> platforms;

	@Parameter(property = "plugins")
	private List<String> plugins;

	private final Log log = getLog();
	
	public void setProject(MavenProject project) {
		this.project = project;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void setFileSets(List<FileSet> fileSets) {
		this.fileSets = fileSets;
	}

	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}

	public void setPlugins(List<String> plugins) {
		this.plugins = plugins;
	}

	protected MavenProject getProject() {
		return project;
	}

	protected String getCommand() {
		return command;
	}

	protected String getName() {
		return name;
	}

	protected String getEscapedName() {
		return getName().replaceAll("\\s", "_");
	}

	protected File getOutputDirectory() {
		return outputDirectory;
	}

	protected List<FileSet> getFileSets() {
		if(fileSets == null || fileSets.isEmpty()) {
			FileSet r = new FileSet();
			r.setDirectory(defaultFileSet);
			fileSets = Collections.singletonList(r);
		}
		return fileSets;
	}

	protected List<String> getPlatforms() {
		return platforms;
	}

	protected List<String> getPlugins() {
		return plugins;
	}

	protected File getCordovaDirectory() {
		cordovaDirectory.mkdirs();
		return cordovaDirectory;
	}

	protected File getLogsDirectory() {
		File logsDirectory = new File(getCordovaDirectory(), LOGS_DIRECTORY);
		logsDirectory.mkdirs();
		return logsDirectory;
	}

	protected ProcessBuilder logCommand(ProcessBuilder processBuilder) {
		if(log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder(COMMAND_MESSAGE_PREFIX);
			for(String s : processBuilder.command()) sb.append(' ').append(s);
			log.info(sb);
		}
		return processBuilder;
	}

	protected int run(ProcessBuilder processBuilder, String goal) throws InterruptedException, IOException {
		final File out = new File(getLogsDirectory(), goal + OUT_LOG_SUFFIX);
		final File err = new File(getLogsDirectory(), goal + ERR_LOG_SUFFIX);
		return logCommand(processBuilder)
				.redirectOutput(out.exists() ? ProcessBuilder.Redirect.appendTo(out) : ProcessBuilder.Redirect.to(out))
				.redirectError(err.exists() ? ProcessBuilder.Redirect.appendTo(err) : ProcessBuilder.Redirect.to(err))
				.start().waitFor();
	}
	
	protected static final ProcessBuilder createProcessBuilder(File directory, String ... commands) {
		
		ProcessBuilder pb = createProcessBuilder(commands).directory(directory);
		
		return pb;
	}
	
	protected static final ProcessBuilder createProcessBuilder(String ... commands) {

		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		
		final ProcessBuilder pb;
		if (isWindows) {
			String[] windowsCommands = new String[commands.length + 2];
			windowsCommands[0] = "cmd";
			windowsCommands[1] = "/c";
			for (int i = 0; i < commands.length; i++) {
				windowsCommands[2+i] = commands[i];
			}
			pb = new ProcessBuilder(windowsCommands);
		} else {
			pb = new ProcessBuilder(commands);
		}
		Map<String, String> env = pb.environment();
		if (System.getenv() != null)  {
		   env.putAll(System.getenv());
		}
		
		return pb;
	}
}
