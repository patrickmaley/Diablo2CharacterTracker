package diablo.file.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CharacterGameFileReader {

	private CharacterGameFile characterGameFile = new CharacterGameFile();
	private Path filePath;
	byte[] data;

	public CharacterGameFileReader(Path child) {
		this.filePath = child;
	}

	public void populateCharacterGameFile() throws IOException {
		data = Files.readAllBytes(filePath);
		characterGameFile.setCharacterLevel(data[43]);
		characterGameFile.setCharacterName(new String(extractName()));
		System.out.println("Character Level: " + characterGameFile.getCharacterLevel() + "\nCharacter Name: "
				+ characterGameFile.getCharacterName());

	}

	private byte[] extractName() {
		byte[] name = java.util.Arrays.copyOfRange(data, 20, 40);
		return name;
	}

}
