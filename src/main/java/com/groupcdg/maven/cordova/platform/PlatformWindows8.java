package com.groupcdg.maven.cordova.platform;

import java.util.EnumSet;

public class PlatformWindows8 extends Platform {

	@Override protected EnumSet<OS> supportedOn() { return EnumSet.of(OS.win32); }
}
