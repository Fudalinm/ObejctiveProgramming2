package Services;

import Domain.Meme;
import com.google.inject.Inject;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class CacheService {

    private DB dbMemory;
    private DB dbDisk;
    private HTreeMap inMemory;
    private HTreeMap onDisk;
    private File file;


    @Inject
    public CacheService(){

        this.file = getFile();

        dbMemory = DBMaker
                .memoryDB()
                .closeOnJvmShutdown()
                .make();

        dbDisk = DBMaker.fileDB(this.file)
                .closeOnJvmShutdown()
                .fileDeleteAfterClose()
                .make();

        onDisk = dbDisk
                .hashMap("onDisk")
                .create();

        inMemory = dbMemory
                .hashMap("inMemory")
                .expireStoreSize(20*1024*1024) // 20 MB
                .expireMaxSize(16)
                .expireAfterCreate()
                .expireOverflow(onDisk)
                .expireExecutor(Executors.newScheduledThreadPool(2))
                .create();

    }

    public void addToGallery(Long key, Meme value){
        this.inMemory.put(key, value);
    }


    public void setMemesForIndex(List<Meme> list, Long index){
        list.clear();
        if(index<=2)
            index = 2L;

        for(Long memeId = index-2; memeId<=index+3; memeId++){
            if(inMemory.containsKey(memeId))
                list.add((Meme) inMemory.get(memeId));
            else
                list.add((Meme) onDisk.get(memeId));
        }

    }

    private File getFile(){
        File file = new File("./cache.dat");
        if (file.exists()) {
            file.delete();
        }
        return new File("./cache.dat");
    }
}
