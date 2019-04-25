package Module;

import Services.DriverConfigurationService;
import Services.GalleryManager;
import Services.PhotoDownloaderFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class DriverConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("memeLibraryPath")).toInstance("memes");
        bind(PhotoDownloaderFactory.class).in(Singleton.class);
        bind(DriverConfigurationService.class).in(Singleton.class);
    }

}
