package com.flemmli97.fatemod.common.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LibReference {

	public static final String MODID = "fatemod";
	public static final String MODNAME = "Fate/Unlimited Block Works";
	public static final String VERSION = "${@VERSION}";
	public static final String DEPENDENCIES = "required:tenshilib@[1.1.0,);";
	public static final String guiFactory = "com.flemmli97.fatemod.client.gui.GuiFactory";
	public static final Logger logger = LogManager.getLogger(LibReference.MODID);
}
