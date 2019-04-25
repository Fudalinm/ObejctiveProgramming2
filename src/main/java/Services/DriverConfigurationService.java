package Services;

import Domain.Sites;
import com.google.inject.Inject;

public class DriverConfigurationService  {

    @Inject
    PhotoDownloaderFactory photoDownloaderFactory;

    @Inject
    GalleryManager galleryManager;

    public GalleryManager get(Sites sites) {
        galleryManager.setPhotoDownloader(photoDownloaderFactory.get(sites));
        return galleryManager;
    }

    public GalleryManager get() {
        return galleryManager;
    }
}
