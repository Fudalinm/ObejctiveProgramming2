package Dowloader;

import Domain.Meme;
import io.reactivex.Observable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

class PhotoDownloaderUtils {
    static Observable<Document> getPageContent(String baseURL, String addition) {
        try {
            return Observable.just(
                    Jsoup.connect(baseURL + addition).get()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Meme downloadByMeme(Meme e) throws IOException {
        Connection.Response resultImageResponse = Jsoup.connect(e.getUrl())
                .ignoreContentType(true).execute();

        return e.addContent(resultImageResponse.bodyAsBytes());
    }
}
