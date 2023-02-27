package com.khopan.system.winimation.settings;

import java.awt.Image;

import com.khopan.winimation.Application;
import com.khopan.winimation.api.ColorList;
import com.khopan.winimation.api.SVGFormatReader;
import com.khopan.winimation.api.annotation.Color;
import com.khopan.winimation.api.annotation.Developer;
import com.khopan.winimation.api.annotation.DynamicIcon;
import com.khopan.winimation.api.annotation.Format;
import com.khopan.winimation.api.annotation.Icon;
import com.khopan.winimation.api.annotation.IconBackground;
import com.khopan.winimation.api.annotation.IconBackgroundType;
import com.khopan.winimation.api.annotation.Identifier;
import com.khopan.winimation.api.annotation.Manifest;
import com.khopan.winimation.api.annotation.Name;
import com.khopan.winimation.api.annotation.Path;
import com.khopan.winimation.api.annotation.Raw;
import com.khopan.winimation.api.annotation.Scale;
import com.khopan.winimation.api.annotation.Version;
import com.khopan.winimation.api.annotation.Viewbox;
import com.khopan.winimation.graphics.Display;

public class SettingsApplication extends Application {
	@Name
	public static final String NAME = "Settings";

	@Version
	public static final String VERSION = "1.0.0";

	@Developer
	public static final String[] DEVELOPER = {"KHOPAN", "Motor"};

	@Manifest
	public static final String MANIFEST = "manifest.json";

	@Icon
	@Format(SVGFormatReader.class)
	@Scale(80.0d)
	@Raw
	@Path
	@Viewbox(24)
	@Color(ColorList.WHITESMOKE)
	public static final String ICON = "M19.43 12.98c.04-.32.07-.64.07-.98s-.03-.66-.07-.98l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.22-.39-.3-.61-.22l-2.49 1c-.52-.4-1.08-.73-1.69-.98l-.38-2.65C14.46 2.18 14.25 2 14 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.61.25-1.17.59-1.69.98l-2.49-1c-.23-.09-.49 0-.61.22l-2 3.46c-.13.22-.07.49.12.64l2.11 1.65c-.04.32-.07.65-.07.98s.03.66.07.98l-2.11 1.65c-.19.15-.24.42-.12.64l2 3.46c.12.22.39.3.61.22l2.49-1c.52.4 1.08.73 1.69.98l.38 2.65c.03.24.24.42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.61-.25 1.17-.59 1.69-.98l2.49 1c.23.09.49 0 .61-.22l2-3.46c.12-.22.07-.49-.12-.64l-2.11-1.65zM12 15.5c-1.93 0-3.5-1.57-3.5-3.5s1.57-3.5 3.5-3.5 3.5 1.57 3.5 3.5-1.57 3.5-3.5 3.5z";

	@IconBackground(IconBackgroundType.GRADIENT)
	public static final String BACKGROUND = "TL57BA20BR4CA31B";

	//@IconBackground(IconBackgroundType.COLOR)
	//public static final String BACKGROUND = "DaRkGrEeN";

	//public static final String ICON = "C:\\Users\\puthi\\Downloads\\business-service-svgrepo-com.svg";

	@DynamicIcon
	private Image icon;

	@Identifier
	private int id;

	public SettingsApplication() {
		//206,856,796
	}

	public void initialize() {
		System.out.println("Initializing Settings Application");
	}

	@Override
	public Display getDisplay() {
		return new SettingsDisplay();
	}

	/*@Override
	public IconManager getIcon() {
		return new SettingsIconManager();
	}*/
}
