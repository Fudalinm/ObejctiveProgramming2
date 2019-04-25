package Module;

import Dao.MemeDao;
import Dao.MemeDaoImpl;
import Dao.TagDao;
import Dao.TagDaoImpl;
import Services.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.persist.jpa.JpaPersistModule;

public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("memesApp_persister"));
        bind(MemeService.class).to(MemeServiceImpl.class).in(Singleton.class);
        bind(MemeDao.class).to(MemeDaoImpl.class).in(Singleton.class);
        bind(TagService.class).to(TagServiceImpl.class).in(Singleton.class);
        bind(TagDao.class).to(TagDaoImpl.class).in(Singleton.class);
        bindInterceptor(Matchers.subclassesOf(MemeService.class), Matchers.any());
        bindInterceptor(Matchers.subclassesOf(TagService.class), Matchers.any());
        bind(PersistenceInitializer.class);
    }
}
