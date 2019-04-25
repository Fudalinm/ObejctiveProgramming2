package Dao;

import Domain.Meme;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.List;

public class MemeDaoImpl implements MemeDao {

    @Inject
    private Provider<EntityManager> emp;

    @Override
    public void save(Meme meme) {
        emp.get().merge(meme);
    }

    @Override
    public Meme findMemeById(Long id) {
        return emp.get().find(Meme.class, id);
    }

    @Override
    public Meme findMemeByTitle(String title) {
        return (Meme) emp.get().createQuery("select m from Meme m where m.title = :title").setParameter(title, "title").getSingleResult();
    }

    @Override
    public List findMemeByTag(String tag) {
        return emp.get().createQuery("select m from Meme m where m.tag = :tag").setParameter(tag, "tag").getResultList();
    }

    @Override
    public List findMemeBySite(String site) {
        return emp.get().createQuery("select m from Meme m where m.site = :site").setParameter(site, "site").getResultList();
    }

    @Override
    public void removeMeme(Long id) {
        emp.get().remove(emp.get().find(Meme.class, id));
    }

    @Override
    public void removeMeme(Meme meme) {
        emp.get().remove(meme);
    }
}
