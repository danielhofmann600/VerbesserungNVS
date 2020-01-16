package at.htl.dogschool.boundary;

import at.htl.dogschool.control.CourseTypeRepository;
import at.htl.dogschool.entity.CourseType;
import at.htl.dogschool.entity.Person;

import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class CourseTypeEndpoint {

    @PersistenceContext
    EntityManager em;
    private long id;
    private CourseType courseType;

    @GET
    @Path("/school/api/course_type")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CourseType> readAll() {
        return em
                .createNamedQuery("CourseType.findAll", CourseType.class)
                .getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CourseType courseType, @Context UriInfo info) {
        em.merge(courseType);
        URI uri = info.getAbsolutePathBuilder()
                .path("/school/api/course_type/" + courseType.getId())
                .build();
        return Response
                .created(uri)
                .header("operation", "object created")
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") long id, CourseType courseType) {
        delete(id);
        em.persist(courseType);
        return Response
                .accepted()
                .header("operation","object updated")
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        TypedQuery<CourseType> query = em.createQuery(
                "SELECT ct FROM CourseType ct WHERE ct.id = :id", CourseType.class);
        CourseType c = query.setParameter("id", id).getSingleResult();
        em.remove(c);
        return Response
                .accepted()
                .header("operation","object deleted")
                .build();
    }


}
