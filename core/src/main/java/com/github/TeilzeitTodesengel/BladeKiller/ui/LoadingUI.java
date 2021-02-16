package com.github.TeilzeitTodesengel.BladeKiller.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LoadingUI extends Table {
	private final ProgressBar progressBar;
	private final TextButton pressAnyKeyButton;
	private final TextButton txtButton;

	public LoadingUI(Skin skin) {
		super(skin);
		setFillParent(true);

		progressBar = new ProgressBar(0, 1, 1.01f, false, skin, "default");
		progressBar.setAnimateDuration(1);

		txtButton = new TextButton("Loading...", skin, "fat");
		txtButton.getLabel().setWrap(true);

		pressAnyKeyButton = new TextButton("Press any key", skin, "fat");
		pressAnyKeyButton.getLabel().setWrap(true);
		pressAnyKeyButton.setVisible(false);

		add(pressAnyKeyButton).expand().fill().center().row();
		add(txtButton).expandX().fillX().bottom().row();
		add(progressBar).expandX().fillX().bottom().pad(20,25,20,25);
		bottom();
	}

	public void setProgress(final float progress) {
		progressBar.setValue(progress);
		if (progress >= 1 && !pressAnyKeyButton.isVisible()) {
			pressAnyKeyButton.setVisible(true);
			pressAnyKeyButton.setColor(1,1,1,0);
			pressAnyKeyButton.addAction(Actions.forever(Actions.sequence(
					Actions.alpha(1, 1),
					Actions.alpha(0, 1)
			)));
		}
	}
}
