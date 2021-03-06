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

import com.groupcdg.maven.cordova.platform.Platform;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;

import static com.groupcdg.maven.cordova.platform.Platform.OS;


@Mojo(name = "build", defaultPhase = LifecyclePhase.PACKAGE)
public class BuildMojo extends AbstractCordovaMojo {


	public static final String BUILD = "build";



	public void execute() throws MojoExecutionException {
		final File outputDirectory = getOutputDirectory();

		if (!outputDirectory.exists() && !outputDirectory.mkdirs())
			throw new MojoExecutionException(CREATE_DIRECTORY_ERROR_MESSAGE + outputDirectory.getAbsolutePath());

		build(outputDirectory);
	}

	private void build(final File outputDirectory) throws MojoExecutionException {
		final OS system = OS.system();
		run(new ProcessBuilder(system.cordova(BUILD)).directory(outputDirectory), BUILD);
		for(Platform platform : system.platforms(getPlatforms())) platform.postBuild(system, this);
	}
}
