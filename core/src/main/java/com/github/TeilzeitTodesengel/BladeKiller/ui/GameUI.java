package com.github.TeilzeitTodesengel.BladeKiller.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;

public class GameUI extends Table {
	public GameUI(final GameCore context){
		super(context.getSkin());
		setFillParent(true);

		add(new TextButton("Blub", getSkin(), "huge"));
	}
}
