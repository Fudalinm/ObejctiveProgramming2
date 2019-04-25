package Dao;

import Domain.Meme;
import Domain.Tag;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;

public class TagDaoImpl implements TagDao {

    @Inject
    private Provider<EntityManager> emp;

    @Override
    public void save(Tag tag) {
        emp.get().merge(tag);
    }

    @Override
    public Tag findTagById(Long id) {
        return emp.get().find(Tag.class, id);
    }

    @Override
    public Tag findTagByName(String name) {
        return (Tag) emp.get().createQuery("select t from Tag t where t.name= :name").setParameter(name, "name").getSingleResult();
    }

    @Override
    public void removeTag(Long id) {
        emp.get().remove(emp.get().find(Tag.class, id));
    }

    @Override
    public void removeTag(Tag tag) {
        emp.get().remove(tag);
    }
}
