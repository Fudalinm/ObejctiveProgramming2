package Dowloader;

import Domain.Meme;
import Domain.Sites;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class KwejkPhotoDownloader implements PhotoDownloader{
    private final Logger log = Logger.getLogger(this.getClass().toString());

    private final String baseURL = "https://kwejk.pl/";
    private final String infixWhenCallingPageByID = "strona/";

    public Observable<Meme> downloadPhoto() {
        log.info("Request to download photos from main Kwejk page");

        Observable<Document> webpageContent = this.getPageContent();

        Observable<Meme> memes = this.downloadPhotosFromWebContent(webpageContent);
        Single<Long> pageNumber = webpageContent.map(e -> this.getPageNumberFromDocument(e)).single(0L);

        Observable<Long> zippingPageNumber = pageNumber.repeat().toObservable();

        return Observable.zip(memes, zippingPageNumber, (e, a) -> e.setPageNumber(a));
    }

    public Observable<Meme> downloadPhoto(long latestPage) {
        final long currentPage = latestPage - 1L;
        String downloadingPage = String.valueOf(currentPage);
        log.info("Request to download photos from Kwejk page: " + downloadingPage);

        Observable<Document> webpageContent
                = this.getPageContent(this.infixWhenCallingPageByID + downloadingPage);

        Observable<Meme> memes = this.downloadPhotosFromWebContent(webpageContent);

        return memes.map(e -> e.setPageNumber(currentPage));
    }

    private Observable<Meme> downloadPhotosFromWebContent(Observable<Document> webpageContent) {
        return webpageContent
                .map(e -> getElementsFromDocument(e))
                .flatMap(e -> Observable.fromIterable(e))
                .filter(e -> e.nextElementSibling() != null)
                .map(e -> this.getMemeFromElement(e))
                .filter(e -> !Objects.equals(e.getUrl(), "")) // Gifs
                .map(e -> PhotoDownloaderUtils.downloadByMeme(e))
                .subscribeOn(Schedulers.io());
    }

    private Iterable<Element> getElementsFromDocument(Document document) {
        return document.body().getElementsByClass("content");
    }

    private Meme getMemeFromElement(Element element) {
        String title = this.getTitleFromElement(element).orElse("");

        Element elementWithURL = element.nextElementSibling();
        String imageURL = this.getSrcFromElement(elementWithURL).orElse("");

        List<String> tags = this.getTagsFromElement(element);

        return new Meme(Sites.KWEJK, title, imageURL, 0L, tags);
    }

    private Optional<String> getTitleFromElement(Element element) {
        return element
                .getElementsByAttributeValue("dusk", "media-title-selector")
                .stream()
                .filter(a -> this.isNodeAnElement(a))
                .findFirst()
                .flatMap(e -> Optional.ofNullable((TextNode) e.childNodes().get(0))).map(e -> e.text().trim());
    }

    private Optional<String> getSrcFromElement(Element element) {
        return element
                .getElementsByClass("full-image")
                .stream()
                .map(e -> e.attr("src"))
                .findFirst();
    }

    private List<String> getTagsFromElement(Element element) {
        Optional<List<Node>> elementContainingTags = element
                .getElementsByClass("tag-list")
                .stream()
                .findFirst()
                .map(e -> e.childNodes());

        List<Node> nodesWithTags = elementContainingTags.orElse(Collections.emptyList());

        return nodesWithTags
                .stream()
                .filter(e -> e.getClass().equals(Element.class))
                .flatMap(e -> e.childNodes().stream())
                .map(e -> e.toString())
                .map(e -> this.removeHashFromTag(e))
                .collect(Collectors.toList());
    }

    private String removeHashFromTag(String tag) {
        if (tag.indexOf('#') == 0 && tag.length() > 1) {
            return tag.substring(1);
        }
        return tag;
    }

    private Long getPageNumberFromDocument(Document document) {
        return document
                .body()
                .getElementsByClass("pager")
                .stream()
                .findFirst()
                .flatMap(e -> getPagerFromElement(e))
                .map(e -> Long.parseLong(e.toString())).orElse(0L);
    }

    private Optional<Node> getPagerFromElement(Element e) {
        return e.getElementsByClass("current")
                .stream()
                .findFirst()
                .flatMap(
                        b -> b.childNodes().stream().filter(a -> isNodeAnElement(a)).findFirst()
                )
                .flatMap(
                        a -> a.childNodes().stream().findFirst()
                );
    }

    private boolean isNodeAnElement(Node node) {
        return node.getClass().equals(Element.class);
    }

    private Observable<Document> getPageContent() {
        return this.getPageContent("");
    }

    private Observable<Document> getPageContent(String addition) {
        return PhotoDownloaderUtils.getPageContent(this.baseURL, addition);
    }
}
