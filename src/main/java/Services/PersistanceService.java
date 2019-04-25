package Services;

import Module.DatabaseModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class PersistanceService {

    private MemeService memeService;
    private TagService tagService;


    public PersistanceService(){
        Injector injector = Guice.createInjector(new DatabaseModule());
        PersistenceInitializer ps = injector.getInstance(PersistenceInitializer.class);
        this.memeService = injector.getInstance(MemeService.class);
        this.tagService = injector.getInstance(TagService.class);
    }
}
