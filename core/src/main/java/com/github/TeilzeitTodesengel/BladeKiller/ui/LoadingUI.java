package com.github.TeilzeitTodesengel.BladeKiller.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;

public class LoadingUI extends Table {
	private final ProgressBar progressBar;
	private final TextButton pressAnyKeyButton;
	private final TextButton txtButton;

	public LoadingUI(final GameCore context) {
		super(context.getSkin());
		setFillParent(true);

		final I18NBundle i18NBundle = context.getI18NBundle();

		progressBar = new ProgressBar(0, 1, 1.01f, false, getSkin(), "default");
		progressBar.setAnimateDuration(1);

		txtButton = new TextButton(i18NBundle.format("loading"), getSkin(), "fat");
		txtButton.getLabel().setWrap(true);

		pressAnyKeyButton = new TextButton(i18NBundle.format("pressAnyKey"), getSkin(), "fat");
		pressAnyKeyButton.getLabel().setWrap(true);
		pressAnyKeyButton.setVisible(false);

		add(pressAnyKeyButton).expand().fill().center().row();
		add(txtButton).expandX().fillX().bottom().row();
		add(progressBar).expandX().fillX().bottom().pad(20, 25, 20, 25);
		bottom();
	}

	public void setProgress(final float progress) {
		progressBar.setValue(progress);
		if (progress >= 1 && !pressAnyKeyButton.isVisible()) {
			pressAnyKeyButton.setVisible(true);
			pressAnyKeyButton.setColor(1, 1, 1, 0);
			pressAnyKeyButton.addAction(Actions.forever(Actions.sequence(
					Actions.alpha(1, 1),
					Actions.alpha(0, 1)
			)));
		}
	}
}
