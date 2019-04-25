package Services;

import Domain.Meme;

import java.util.List;

public interface MemeService {

    void saveMeme(Meme meme);

    Meme getMemeById(Long id);

    Meme getMemeByTitle(String title);

    List getMemesByTag(String tag);

    List getMemesBySite(String site);

    void removeMeme(Long id);

    void removeMeme(Meme meme);
}
