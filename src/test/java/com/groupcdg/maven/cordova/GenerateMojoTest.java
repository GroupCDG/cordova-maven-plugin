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

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GenerateMojoTest {

	private static final String GENERATE_GOAL = "generate";

	private static final String CORDOVA_PLUGIN_NAME = "cordova-maven-plugin";

	private static final String ASSETS_TARGET = "target/generated-sources/cordova/www";

	private static final File DEFAULT_PROJECT = new File("src/test/resources/unit/default");

	private static final File CUSTOM_RESOURCES_PROJECT = new File("src/test/resources/unit/custom-resources");

	private static final String DEFAULT_ASSET_CONTENT = "Assets taken from default webapp directory";

	private static final String CUSTOM_ASSET_CONTENT = "Assets taken from custom assets directory";

	private static final String OVERRIDEN_ASSET_CONTENT = "Assets taken from custom overrides directory";


	@Rule
	public MojoRule rule = new MojoRule();



	@Test
	public void testDefaultGeneration() throws Exception {
		FileUtils.cleanDirectory(new File(DEFAULT_PROJECT, "target"));
		rule.configureMojo(new GenerateMojo(), CORDOVA_PLUGIN_NAME, pom(DEFAULT_PROJECT));
		rule.executeMojo(DEFAULT_PROJECT, GENERATE_GOAL);

		assertIncluded(asset(DEFAULT_PROJECT, "index.html"), DEFAULT_ASSET_CONTENT);
		assertIncluded(asset(DEFAULT_PROJECT, "css/style.css"), DEFAULT_ASSET_CONTENT);
	}

	@Test
	public void testCustomResourcesGeneration() throws Exception {
		FileUtils.cleanDirectory(new File(CUSTOM_RESOURCES_PROJECT, "target"));
		rule.configureMojo(new GenerateMojo(), CORDOVA_PLUGIN_NAME, pom(CUSTOM_RESOURCES_PROJECT));
		rule.executeMojo(CUSTOM_RESOURCES_PROJECT, GENERATE_GOAL);

		assertIncluded(asset(CUSTOM_RESOURCES_PROJECT, "index.html"), CUSTOM_ASSET_CONTENT);
		assertIncluded(asset(CUSTOM_RESOURCES_PROJECT, "css/style.css"), CUSTOM_ASSET_CONTENT);
		assertIncluded(asset(CUSTOM_RESOURCES_PROJECT, "other.html"), OVERRIDEN_ASSET_CONTENT);
		assertExcluded(asset(CUSTOM_RESOURCES_PROJECT, "excluded.txt"));

	}



	private File pom(File projectDir) {
		return new File(projectDir, "pom.xml");
	}

	private File asset(File projectDir, String fileName) {
		return new File(new File(projectDir, ASSETS_TARGET), fileName);
	}

	private void assertIncluded(File file, String content) throws IOException {
		assertTrue("File should be included: " + file.getAbsolutePath(), file.exists());
		assertThat(FileUtils.readFileToString(file), containsString(content));
	}

	private void assertExcluded(File file) {
		assertFalse("File should be excluded: " + file.getAbsolutePath(), file.exists());
	}
}
