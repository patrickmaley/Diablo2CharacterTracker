package diablo.file.utilities;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to run the thread that watches the Diablo 2 Save
 * File. This class will notify the program when updates have been made to the
 * .d2s file. This uses the WatchService library to watch the save game
 * directory for file changes.
 * 
 * @author Atlas16
 *
 */
public class SaveGameWatcher {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final Path filePath;

	public SaveGameWatcher(String dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.filePath = Paths.get(dir);
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void watchSaveGame() {
		try {
			registerModify();
		} catch (IOException e) {
			e.printStackTrace();
		}
		processEvents();
	}

	private void processEvents() {
		while (true) {
			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);

				// print out event
				System.out.format("%s: %s\n", event.kind().name(), child);
				if (child.toString().contains("Fahren.d2s")) {
					CharacterGameFileReader characterGameFileReader = new CharacterGameFileReader(child);
					try {
						characterGameFileReader.populateCharacterGameFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(1);
					}
				}
				// if directory is created, and watching recursively, then
				// register it and its sub-directories

			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}

	}

	private void registerModify() throws IOException {
		WatchKey key = this.filePath.register(this.watcher, ENTRY_MODIFY);
		keys.put(key, this.filePath);
	}

}
