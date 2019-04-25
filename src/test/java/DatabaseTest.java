import Domain.Meme;
import Domain.Sites;
import Module.DatabaseModule;
import Services.MemeService;
import Services.PersistenceInitializer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;


public class DatabaseTest {

    private MemeService memeService;

    @BeforeAll
    public void setUp() {
        Injector injector = Guice.createInjector(new DatabaseModule());
        PersistenceInitializer ps = injector.getInstance(PersistenceInitializer.class);
        memeService = injector.getInstance(MemeService.class);
    }

    @Test
    public void testAddMemes() {
        memeService.saveMeme(new Meme(Sites.KWEJK, "Test1", "TestUrl1", (long) 1, Collections.EMPTY_LIST));
        memeService.saveMeme(new Meme(Sites.NINEGAG, "Test2", "TestUrl2", (long) 1, Collections.EMPTY_LIST));
    }

    @Test
    public void testSearchMeme() {
        testAddMemes();
        Meme meme = memeService.getMemeByTitle("Test1");
        assert("Test1".equals(meme.getTitle()));
        assert("TestUrl1".equals(meme.getUrl()));
        assert(Sites.KWEJK == meme.getSite());
    }

    @Test
    public void testRemoveMeme(){
        testAddMemes();
        memeService.removeMeme((long) 1);
        memeService.removeMeme(memeService.getMemeByTitle("Test2"));
    }
}
