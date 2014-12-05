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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.groupcdg.maven.cordova.platform.Platform.OS;
import static com.groupcdg.maven.cordova.platform.Platform.PLATFORM;
import static org.apache.commons.collections.CollectionUtils.isEqualCollection;
import static org.junit.Assert.assertTrue;

public class AbstractCordovaMojoTest {


	@Test
	public void testOSPlatformFilter() {
		final List<String> configured = Arrays.asList("android", "ios", "wp8", "madeup");
		assertTrue(isEqualCollection(Arrays.asList(PLATFORM.byLabel("android")), OS.linux.platforms(configured)));
		assertTrue(isEqualCollection(Arrays.asList(PLATFORM.byLabel("android"), PLATFORM.byLabel("ios")), OS.osx.platforms(configured)));
		assertTrue(isEqualCollection(Arrays.asList(PLATFORM.byLabel("android"), PLATFORM.byLabel("wp8")), OS.win32.platforms(configured)));


		assertTrue(isEqualCollection(Arrays.asList(PLATFORM.byLabel("ios")), OS.osx.platforms(Arrays.asList("ios", "wp8"))));
	}
}
