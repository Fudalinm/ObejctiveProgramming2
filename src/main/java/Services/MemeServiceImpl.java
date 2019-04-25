package Services;

import Dao.MemeDao;
import Domain.Meme;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.util.List;

public class MemeServiceImpl implements MemeService {

    @Inject
    private MemeDao memeDao;

    @Override
    @Transactional
    public void saveMeme(Meme meme) {
        memeDao.save(meme);
    }

    @Override
    public Meme getMemeById(Long id) {
        return memeDao.findMemeById(id);
    }

    @Override
    public Meme getMemeByTitle(String title) {
        return memeDao.findMemeByTitle(title);
    }

    @Override
    public List getMemesByTag(String tag) {
        return memeDao.findMemeByTag(tag);
    }

    @Override
    public List getMemesBySite(String site) {
        return memeDao.findMemeBySite(site);
    }

    @Override
    @Transactional
    public void removeMeme(Long id) {
        memeDao.removeMeme(id);
    }

    @Override
    @Transactional
    public void removeMeme(Meme meme) {
        memeDao.removeMeme(meme);
    }
}
