package com.khopan.winimation.graphics;

import com.khopan.winimation.Wallpaper;
import com.khopan.winimation.graphics.draw.Area;

public class HomeDisplay extends Display {
	public HomeDisplay() {
		this.setFragment(new HomeFragment());
	}

	@Override
	public void load() {
		Wallpaper.loadingHook(() -> {
			this.pane.showBars();
		});

		Wallpaper.load();
	}

	private class HomeFragment extends Fragment {
		private HomeFragment() {

		}

		@Override
		public void render(Area area) {
			Wallpaper.draw(area);
		}
	}
}
