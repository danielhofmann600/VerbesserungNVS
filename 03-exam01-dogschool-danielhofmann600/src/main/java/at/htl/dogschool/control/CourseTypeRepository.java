package at.htl.dogschool.control;

import at.htl.dogschool.entity.CourseType;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CourseTypeRepository {


    public CourseType findByAbbr(String abbr) {
        return null;
    }

    public List<CourseType> findAll() {
        return null;
    }

    public CourseType save(CourseType courseType) {
        return null;
    }

    public CourseType findById(long id) {
        return null;
    }

    public void delete(long id) {

    }

}
