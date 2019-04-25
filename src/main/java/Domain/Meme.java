package Domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Meme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Sites site;

    private String url;

    @Transient
    private byte[] content;

    @Transient
    private String extension = "jpeg";

    @ManyToMany
    private List<Tag> tags;

    private Long pageNumber;

    public Meme(){}

    public Meme(Sites site,String title, String url, Long pageNumber, List<String> tags){
        this.site = site;
        this.title = title;
        this.url = url;
        //this.tags = tags;
        this.pageNumber = pageNumber;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getExtension() { return extension; }

    public void setExtension(String extension) { this.extension = extension; }

    public Sites getSite() {
        return site;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Meme addContent(byte[] content){
        this.content = content;
        return this;
    }

    public Meme setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public Long getPageNumber(){
        return this.pageNumber;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Meme{");
        sb.append("title='").append(title).append('\'');
        sb.append(", site=").append(site);
        sb.append(", url='").append(url).append('\'');
        sb.append(", tags=").append(tags);
        sb.append(", pageNumber=").append(pageNumber);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meme)) return false;
        Meme meme = (Meme) o;
        return getSite() == meme.getSite() &&
                Objects.equals(getUrl(), meme.getUrl()) &&
                Arrays.equals(getContent(), meme.getContent());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSite(), getUrl());
        result = 31 * result + Arrays.hashCode(getContent());
        return result;
    }

}
