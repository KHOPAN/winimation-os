package com.khopan.system.winimation.settings;

import com.khopan.winimation.graphics.Display;
import com.khopan.winimation.graphics.Fragment;
import com.khopan.winimation.graphics.draw.Area;

public class SettingsDisplay extends Display {
	public SettingsDisplay() {
		this.setFragment(new SettingsFragment());
	}

	private class SettingsFragment extends Fragment {
		private SettingsFragment() {

		}

		@Override
		public void render(Area area) {
			area.color(0xFF0000);
			area.rect(0, 0, this.width, this.height);
		}
	}
}
