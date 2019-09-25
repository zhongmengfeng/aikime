package com.ichi2yiji.anki.features.ready.bean;

import java.io.Serializable;

public class InitFilterConditionBean implements Serializable{


	private String[][] options;
	
	private String[] decks;

	private String currentDeckname;

	public String[][] getOptions() {
		return options;
	}

	public void setOptions(String[][] options) {
		this.options = options;
	}

	public String[] getDecks() {
		return decks;
	}

	public void setDecks(String[] decks) {
		this.decks = decks;
	}

	public String getCurrentDeckname() {
		return currentDeckname;
	}

	public void setCurrentDeckname(String currentDeckname) {
		this.currentDeckname = currentDeckname;
	}
}
