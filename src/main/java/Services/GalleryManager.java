package Services;

import Domain.Gallery;
import Domain.Meme;
import Dowloader.KwejkPhotoDownloader;
import Dowloader.PhotoDownloader;
import Util.MemeSerializer;
import com.google.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;

public class GalleryManager {

    //@Inject
    private PersistanceService persistanceService;

    @Inject
    private MemeSerializer memeSerializer;

    private CacheService cacheService;

    private PhotoDownloader photoDownloader = new KwejkPhotoDownloader();

    private Gallery gallery;

    private Long memeCounter = 0L;

    private Long pageNumber;

    public void setPhotoDownloader(PhotoDownloader photoDownloader) {
        this.photoDownloader = photoDownloader;
    }

    public List<Meme> getMemeList(){
        return (List<Meme>) this.photoDownloader.downloadPhoto();
    }

    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer value) {
            if(memeCounter <= value + 5){
                photoDownloader.downloadPhoto(pageNumber--)
                        .subscribe(meme -> cacheService.addToGallery(memeCounter++, meme));
            }
            cacheService.setMemesForIndex(gallery.getCurrentList(), (long)value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };


    @Inject
    public GalleryManager(CacheService cacheService){
        this.cacheService = cacheService;
        this.gallery = new Gallery(this.observer);
        this.photoDownloader.downloadPhoto().forEach(meme -> cacheService.addToGallery(memeCounter++, meme));
        cacheService.setMemesForIndex(gallery.getCurrentList(), 0L);
        this.pageNumber =  gallery.getCurrentMeme().get().getPageNumber();
    }

    public Gallery getGallery() {
        return gallery;
    }
}
