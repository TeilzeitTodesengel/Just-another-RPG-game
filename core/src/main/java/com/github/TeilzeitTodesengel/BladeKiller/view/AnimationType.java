package com.github.TeilzeitTodesengel.BladeKiller.view;

public enum AnimationType {
	HERO_MOVE_UP("characters_and_effects/character_and_effects.atlas", "warrior_m", 0.25f, 0),
	HERO_MOVE_DOWN("characters_and_effects/character_and_effects.atlas", "warrior_m", 0.25f, 2),
	HERO_MOVE_LEFT("characters_and_effects/character_and_effects.atlas", "warrior_m", 0.25f, 3),
	HERO_MOVE_RIGHT("characters_and_effects/character_and_effects.atlas", "warrior_m", 0.25f, 1);

	private final String atlasPath;
	private final String atlasKey;
	private final float frameTime;
	private final int rowIndex;

	AnimationType(String atlasPath, String atlasKey, float frameTime, int rowIndex) {
		this.atlasPath = atlasPath;
		this.atlasKey = atlasKey;
		this.frameTime = frameTime;
		this.rowIndex = rowIndex;
	}

	public String getAtlasPath() {
		return atlasPath;
	}

	public String getAtlasKey() {
		return atlasKey;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public int getRowIndex() {
		return rowIndex;
	}
}
