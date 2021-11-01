package com.kekens.soa_back.resource;

import com.kekens.soa_back.model.Discipline;
import com.kekens.soa_back.service.DisciplineService;
import com.kekens.soa_back.service.impl.DisciplineServiceImpl;
import com.kekens.soa_back.validator.IntegrityError;
import com.kekens.soa_back.validator.exception.IncorrectDataException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplineResource {

    DisciplineService disciplineService = new DisciplineServiceImpl();

    @GET
    public Response getAllDisciplines() {
        List<Discipline> disciplineList = disciplineService.findAllDisciplines();
        return Response.status(Response.Status.OK).entity(disciplineList).build();
    }

    @GET
    @Path("/{id}")
    public Response getDisciplineById(@PathParam("id") String id) {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            return Response.status(Response.Status.OK).entity(discipline).build();
        } catch (NumberFormatException | IncorrectDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404,"Resource not found")).build();
        }
    }

    @POST
    public Response createDiscipline(Discipline discipline) {
        try {
            disciplineService.createDiscipline(discipline);
            return Response.status(Response.Status.OK).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity( e.getErrorList()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateDiscipline(@PathParam("id") String id, Discipline discipline) {
        try {
            discipline.setId(disciplineService.findDisciplineById(Integer.parseInt(id)).getId());
            disciplineService.updateDiscipline(discipline);
            return Response.status(Response.Status.OK).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404, "Resource not found")).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

            return Response.status(status).entity( e.getErrorList()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDiscipline(@PathParam("id") String id) {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            disciplineService.deleteDiscipline(discipline);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404, "Resource not found")).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

            return Response.status(status).entity( e.getErrorList()).build();
        }
    }

}