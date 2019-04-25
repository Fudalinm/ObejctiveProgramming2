package Domain;


import com.google.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.subjects.BehaviorSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Gallery {

    private BehaviorSubject<Integer> currentIndx;
    private Optional<List<Meme>> memes = Optional.of(new ArrayList<>());

    @Inject
    public Gallery(Observer<Integer> observer) {
        this.currentIndx = BehaviorSubject.createDefault(0);
        this.currentIndx.skip(1).subscribe(observer);
    }

    public Optional<Meme> getCurrentMeme() {
        if(currentIndx.getValue() <= 2) return this.memes.flatMap(e -> Optional.ofNullable(e.get(currentIndx.getValue())));
        return this.memes.flatMap(e -> Optional.ofNullable(e.get(2)));
    }

    public List<Meme> getCurrentList(){
        return this.memes.get();
    }

    public Optional<Meme> swipeGalleryLeft() {
        if (isIndexValid(this.currentIndx.getValue() - 1)) {
            this.currentIndx.onNext(this.currentIndx.getValue() - 1);
            return this.getCurrentMeme();
       }
       else {
            return Optional.empty();
       }
    }

    public Optional<Meme> swipeGalleryRight() {
        if (isIndexValid(this.currentIndx.getValue() + 1)) {
            this.currentIndx.onNext(this.currentIndx.getValue() + 1);
            return this.getCurrentMeme();
        }
        else {
            return Optional.empty();
        }
    }

    public Gallery addToGallery(Meme meme){
        this.memes = this.memes.map(e -> {
            e.add(meme);
            return e;
        });
        return this;
    }


    private Boolean isIndexValid(Integer index) {
        return index >= 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Gallery{");
        sb.append("currentIndex=").append(this.currentIndx.getValue());
        sb.append(", memes=").append(memes);
        sb.append('}');
        return sb.toString();
    }
}