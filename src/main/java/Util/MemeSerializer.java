package Util;

import Domain.Meme;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class MemeSerializer {

    private final String memeLibraryPath;

    @Inject
    public MemeSerializer(@Named("memeLibraryPath") String memeLibraryPath) throws IOException {
        this.memeLibraryPath = memeLibraryPath;
        createLibraryDirectory();
    }

    private void createLibraryDirectory() throws IOException {
        File memeLibraryDir = new File(memeLibraryPath);
        if (!memeLibraryDir.exists()) {
            memeLibraryDir.mkdir();
        }
        if (!memeLibraryDir.isDirectory()) {
            throw new IOException("This is not valid photo library directory path or directory could not be created: " + memeLibraryPath);
        }
    }

    public void savePhoto(Meme meme) {
        try (FileOutputStream outputStream = new FileOutputStream(getPhotoPath(meme))) {
            outputStream.write(meme.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLibraryContents() {
        File photoLibraryDirectory = new File(memeLibraryPath);
        for (String childFile : photoLibraryDirectory.list()) {
            File libraryFile = new File(memeLibraryPath, childFile);
            libraryFile.delete();
        }
    }

    private String getPhotoPath(Meme meme) {
        return Paths.get(memeLibraryPath, String.format("%s.%s", meme.getId().toString(), meme.getExtension())).toString();
    }
}
