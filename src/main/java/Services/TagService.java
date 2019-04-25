package Services;

import Domain.Tag;

public interface TagService {

    void saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    void removeTag(Long id);

    void removeTag(Tag tag);
}
