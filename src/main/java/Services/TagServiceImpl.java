package Services;

import Dao.TagDao;
import Domain.Tag;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class TagServiceImpl implements TagService{

    @Inject
    private TagDao tagDao;

    @Override
    @Transactional
    public void saveTag(Tag tag) {
        tagDao.save(tag);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagDao.findTagById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDao.findTagByName(name);
    }

    @Override
    @Transactional
    public void removeTag(Long id) {
        tagDao.removeTag(id);
    }

    @Override
    @Transactional
    public void removeTag(Tag tag) {
        tagDao.removeTag(tag);
    }
}
