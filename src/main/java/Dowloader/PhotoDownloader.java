package Dowloader;

import Domain.Meme;
import io.reactivex.Observable;

public interface PhotoDownloader {
    Observable<Meme> downloadPhoto();

    Observable<Meme> downloadPhoto(long latestPageNumber);
}
