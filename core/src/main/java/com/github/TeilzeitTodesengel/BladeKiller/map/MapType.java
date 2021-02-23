package com.github.TeilzeitTodesengel.BladeKiller.map;

public enum MapType {
	MAP_1("Map.tmx"),
	MAP_2("Map2.tmx");

	private final String filePath;

	MapType(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
