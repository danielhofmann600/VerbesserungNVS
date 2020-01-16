package at.htl.dogschool.control;

import at.htl.dogschool.entity.Course;
import at.htl.dogschool.entity.CourseType;
import at.htl.dogschool.entity.Dog;
import at.htl.dogschool.entity.Person;
import com.sun.source.tree.IfTree;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class InitBean {

    private final String FILE_NAME = "META-INF/courses.csv";

    @PersistenceContext
    EntityManager em;

    @Inject
    CourseTypeRepository courseTypeRepository;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        initDb();
    }



    /**
     * Ändern Sie den Inhalt dieser Methode NICHT
     */
    private void initDb() {

        Person matt = new Person("Matt", "Murdock");
        Person mathilda = new Person("Mathilda", "Lando");

        em.persist(new Dog("Timmy", matt));
        em.persist(new Dog("Tino", matt));
        em.persist(new Dog("Arko", mathilda));
        em.persist(new Dog("Rex", mathilda));
        em.persist(new Dog("Edi", mathilda));

        CourseType welpenkurs = new CourseType("Welpenkurs", "w");
        CourseType begleithunde1 = new CourseType("Begleithunde1", "bg1");
        CourseType begleithunde2 = new CourseType("Begleithunde2", "bg2");
        em.persist(welpenkurs);
        em.persist(begleithunde1);
        em.persist(begleithunde2);

        readCsv();

    }

    /**
     * Einlesen des csv-Files und speichern in der DB.
     * Das Course-Objekt wird unter Verwendung der Methode parseCourse(...) erstellt,
     * und anschließend in dieser Methode persistiert
     *
     * ACHTUNG: Doppelte Kurse dürfen nicht eingelesen werden
     *          (dh name, startdatetime, price und no_of_days sind gleich)
     */
    private void readCsv() {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource("courses.csv");
        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)) {
            stream
                    .skip(1)
                    .distinct()
                    .forEach(s -> {
                        Course c = parseCourse(s.split(";"));
                        em.persist(c);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * parse one line of the csv-file and create a course-object
     *
     * we are using ISO-8601 for the datetime-field
     *
     * Use for getteing the coursetype a named-jpa-query
     *
     * @param elems
     * @return the new Course-object
     */
    private Course parseCourse(String[] elems) {
        String name = elems[4];
        double price = Double.parseDouble(elems[2]);
        LocalDateTime startDatetime = getCourseDate(elems[1]);
        int noOfMeetings = Integer.parseInt(elems[3]);
        CourseType courseType;
        TypedQuery<CourseType> courseTypeQuery = em.createQuery(
                "select c from CourseType c where c.abbr = :abbr",CourseType.class);
        courseType = courseTypeQuery.setParameter("abbr",elems[0]).getSingleResult();
        Course c = new Course(name,price,startDatetime,noOfMeetings,courseType);
        return c;
    }

    private LocalDateTime getCourseDate(String date){
        date = date.replace('T','-');
        date = date.replace(':','-');
        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        int hour = Integer.parseInt(date.split("-")[3]);
        int minute = Integer.parseInt(date.split("-")[4]);
        LocalDateTime dateTime = LocalDateTime.of(year,month,day,hour,minute);
        return dateTime;
    }

}
