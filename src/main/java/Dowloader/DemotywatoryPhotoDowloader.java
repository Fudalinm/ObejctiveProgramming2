package Dowloader;

import Domain.Meme;
import Domain.Sites;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DemotywatoryPhotoDowloader implements PhotoDownloader {
    private final Logger log = Logger.getLogger(this.getClass().toString());

    private final String baseURL = "https://demotywatory.pl";

    @Override
    public Observable<Meme> downloadPhoto() {
        log.info("Request to download photos from main Demotywatory page");

        Observable<Document> webpageContent = this.getPageContent();

        Observable<Meme> memes = this.downloadPhotosFromWebContent(webpageContent);

        Observable<Long> page = this.getPageNumberFromDocument(webpageContent).repeat().toObservable();

        return Observable.zip(memes, page, (e,a) -> e.setPageNumber(a));
    }

    private Single<Long> getPageNumberFromDocument(Observable<Document> webpageContent) {
        Observable<Optional<Element>> nextPageElement
                = webpageContent.map(e ->
                e.body().getElementsByClass("list_next_page_button").stream().findFirst());

        return nextPageElement
                .map(e -> e.map(a -> a.attr("href")))
                .map(e -> e.orElse(""))
                .map(e -> this.convertURLtoPageNumber(e))
                .first(1L);
    }

    private Long convertURLtoPageNumber(String url) {
        String[] splitted = url.split("/");
        String pageNumber = splitted[splitted.length - 1];

        return Long.parseLong(pageNumber);
    }

    @Override
    public Observable<Meme> downloadPhoto(long latestPageNumber) {
        final long currentPage = latestPageNumber + 1L;
        String downloadingPage = String.valueOf(currentPage);
        log.info("Request to download photos from Demotywatory page: " + downloadingPage);

        Observable<Document> webpageContent = this.getPageContent("/page/"+downloadingPage);

        Observable<Meme> memes = this.downloadPhotosFromWebContent(webpageContent);

        return memes.map(e -> e.setPageNumber(currentPage));
    }

    private Observable<Meme> downloadPhotosFromWebContent(Observable<Document> webpageContent){
        return webpageContent
                .flatMap(e -> Observable.fromIterable(this.getElementsFromDocument(e)))
                .map(e -> this.mapElementToMeme(e))
                .filter(e -> !Objects.equals(e.getUrl(), "")) // Gifs + fajnie byloby uzyc decoratora TODO
                .map(e -> PhotoDownloaderUtils.downloadByMeme(e))
                .subscribeOn(Schedulers.io());
    }

    private Meme mapElementToMeme(Element element){
        return this.createMemeFromElement(element);
    }

    private Meme createMemeFromElement(Element element){
        String title = this.getTitleFromElement(element);

        String url = this.getUrlFromElement(element);

        List<String> tags = this.getTagsFromTitle(title);

        return new Meme(Sites.DEMOTYWATORY, title, url, -1L, tags);
    }

    private List<String> getTagsFromTitle(String title) {
        return Arrays.stream(title.split(" ")).filter(e -> e.length() > 3).collect(Collectors.toList());
    }

    private String getUrlFromElement(Element element) {
        Optional<Element> picture = this.mapPictureWrappedInHtmlToElement(element);
        return picture.map(e -> e.attr("src")).orElse("");
    }

    private String getTitleFromElement(Element element) {
        Optional<Element> titleElement = element.getElementsByTag("a").stream().findFirst();

        return titleElement
                .flatMap(e -> e.childNodes().stream().findFirst())
                .filter(e -> e.getClass().equals(TextNode.class))
                .map(e -> (TextNode) e)
                .map(e -> e.text())
                .orElse("")
                .trim();
    }

    private Optional<Element> mapPictureWrappedInHtmlToElement(Element element){
        return element
                .getElementsByClass("demot").stream().findFirst();
    }

    private Iterable<Element> getElementsFromDocument(Document document){
        return document
                .body()
                .getElementsByClass("demotivator pic ");
    }

    private Observable<Document> getPageContent(){
        return this.getPageContent("");
    }

    private Observable<Document> getPageContent(String additions){
        return PhotoDownloaderUtils.getPageContent(this.baseURL, additions);
    }
}
