package Dao;

import Domain.Meme;

import java.util.List;

public interface MemeDao {
    void save(Meme meme);

    Meme findMemeById(Long id);

    Meme findMemeByTitle(String title);

    List findMemeByTag(String tag);

    List findMemeBySite(String site);

    void removeMeme(Long id);

    void removeMeme(Meme meme);
}
