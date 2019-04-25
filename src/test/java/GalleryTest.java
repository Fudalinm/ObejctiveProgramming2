import Domain.Gallery;
import Domain.Meme;
import Domain.Sites;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GalleryTest {
    public Gallery gallery;
    public List<Meme> memes;

    @BeforeEach
    public void init(){
        Date releaseDate = new Date();

        Meme meme1 = new Meme(Sites.KWEJK, releaseDate, "sample url",new byte[]{},Collections.emptyList());
        Meme meme2 = new Meme(Sites.NINEGAG, releaseDate, "sample url2",new byte[]{},Collections.emptyList());

        memes = new LinkedList<>();
        memes.add(meme1);
        memes.add(meme2);

        gallery = new Gallery();
    }

    @AfterEach
    public void tearDown(){
        this.gallery = null;
        this.memes = null;
    }

    @Test
    public void galleryShouldHandleNullInitialValue(){
        Gallery gallery = new Gallery();

        assertTrue(!gallery.getCurrentMeme().isPresent());
        assertTrue(!gallery.swipeGalleryLeft().isPresent());
        assertTrue(!gallery.swipeGalleryRight().isPresent());
    }

    @Test
    public void galleryShouldHandleSwipeRight(){
        Optional<Meme> current = gallery.swipeGalleryRight();
        assertTrue(current.isPresent());
        assertEquals(current.get(), memes.get(1));
    }

    @Test
    public void galleryShouldHandleNSwipesRight(){
        Meme meme3 = new Meme(Sites.KWEJK, new Date(), "sample url",new byte[]{},Collections.emptyList());

        gallery.addToGallery(meme3);

        Optional<Meme> current = gallery.swipeGalleryRight();
        assertTrue(current.isPresent());
        assertEquals(current.get(), memes.get(1));

        Optional<Meme> after = gallery.swipeGalleryRight();
        assertTrue(after.isPresent());
        assertEquals(after.get(),meme3);
    }

    @Test
    public void galleryShouldHandleSwipeRightOutOfRange(){
        gallery.swipeGalleryRight();
        Optional<Meme> outOfRangeSwipe = gallery.swipeGalleryRight();
        assertFalse(outOfRangeSwipe.isPresent());
    }

    @Test
    public void galleryShouldHandleGetCurrentMemeWithNonEmptyValues(){
        Optional<Meme> current = gallery.getCurrentMeme();
        assertTrue(current.isPresent());
        assertEquals(current.get(), memes.get(0));
    }

    @Test
    public void galleryShouldHandleSwipeLeftFromFirstPosition(){
        Optional<Meme> current = gallery.swipeGalleryLeft();
        assertFalse(current.isPresent());
    }

    @Test
    public void gallerShouldHandleNSwipesLeft(){
        Meme meme3 = new Meme(Sites.KWEJK, new Date(), "sample url",new byte[]{},Collections.emptyList());

        gallery.addToGallery(meme3);

        //Three swipes will get us to the last element of memes list
        gallery.swipeGalleryRight();
        gallery.swipeGalleryRight();
        gallery.swipeGalleryRight();

        //From there we will be able to access 2 using swipe left:
        Optional<Meme> current = gallery.swipeGalleryLeft();

        assertTrue(current.isPresent());
        assertEquals(current.get(), memes.get(1));

        Optional<Meme> after = gallery.swipeGalleryLeft();
        assertTrue(after.isPresent());
        assertEquals(after.get(), memes.get(0));
    }


}
