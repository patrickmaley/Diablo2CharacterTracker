package diablo.file.application;

import java.io.IOException;

import diablo.file.utilities.CharacterGameFile;
import diablo.file.utilities.SaveGameWatcher;

public class Diablo2Application {

	public static void main(String[] args) {

		// Watch File
		SaveGameWatcher diabloSaveGameWatcher = null;
		CharacterGameFile saveGameFile = new CharacterGameFile();

		try {
			diabloSaveGameWatcher = new SaveGameWatcher("C:\\Program Files\\Diablo II\\D2SE\\CORES\\1.13c\\save");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exiting Application");
			System.exit(1);
		}
		System.out.println("Watching File");
		diabloSaveGameWatcher.watchSaveGame();

	}

}
