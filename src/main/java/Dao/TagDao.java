package Dao;

import Domain.Meme;
import Domain.Tag;

import java.util.List;

public interface TagDao {

    void save(Tag tag);

    Tag findTagById(Long id);

    Tag findTagByName(String name);

    void removeTag(Long id);

    void removeTag(Tag tag);
}
