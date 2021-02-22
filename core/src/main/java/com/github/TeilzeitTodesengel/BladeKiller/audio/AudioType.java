package com.github.TeilzeitTodesengel.BladeKiller.audio;


public enum AudioType {
	BACKGROUND("audio/Woodland Fantasy.mp3", true, 0.2f),
	INTRO("audio/The Fall of Arcana.mp3", true, 0.2f),
	SELECT("audio/select.wav", false, 0.4f);

	private final String filePath;
	private final boolean isMusic;
	private final float volume;

	AudioType(String filePath, boolean isMusic, float volume) {
		this.filePath = filePath;
		this.isMusic = isMusic;
		this.volume = volume;
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean isMusic() {
		return isMusic;
	}

	public float getVolume() {
		return volume;
	}
}
