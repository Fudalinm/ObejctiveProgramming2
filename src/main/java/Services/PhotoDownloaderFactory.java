package Services;

import Domain.Sites;
import Dowloader.KwejkPhotoDownloader;
import Dowloader.DemotywatoryPhotoDowloader;
import Dowloader.PhotoDownloader;

public class PhotoDownloaderFactory {
    public PhotoDownloader get(Sites site){
        switch(site){
            case KWEJK:
                return new KwejkPhotoDownloader();
            case DEMOTYWATORY:
                return new DemotywatoryPhotoDowloader();
            default:
                return new KwejkPhotoDownloader();
        }
    }
}
