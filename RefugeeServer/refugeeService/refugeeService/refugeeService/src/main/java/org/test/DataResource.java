package org.test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.test.domain.Contestants;
import org.test.domain.Courses;
import org.test.exception.ServiceException;
import org.test.repository.ContestantsRepository;
import org.test.repository.CoursesRepository;
import org.test.repository.RegistrationRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Path("/data")
public class DataResource {

    private static final Logger Log = Logger.getLogger(DataResource.class);

    @Inject
    CoursesRepository courseRepository;

    @Inject
    ContestantsRepository contestantsRepository;

    @Inject
    RegistrationRepository registrationRepository;

    @GET
    @Path("allContestants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContestants() {
        return Response.ok(contestantsRepository.listAll()).build();
    }

    @POST
    @Transactional
    @Path("registerContestant")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerContestant(Contestants contestants) {
        Log.info("registerContestant");
        contestantsRepository.persist(contestants);
        if (contestantsRepository.isPersistent(contestants)) {
            return Response.ok().build();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("getContestantByMail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContestantByMail(@PathParam("email") String email) {
        Log.info("getContestantByMail");
        Optional<Contestants> contestants = contestantsRepository.findByEmail(email);
        if (contestants.isPresent()) {
            return Response.ok(contestants.get()).build();
        }
        return Response.noContent().build();
    }


    @GET
    @Path("allCourses")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourses() {
        Log.info("getAllCourses");
        return Response.ok(courseRepository.listAll()).build();
    }

    @POST
    @Transactional
    @Path("createCourse")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(Courses courses) {
        Log.info("createCourse");
        courses.setName(courses.getName().toUpperCase().toLowerCase());
        courseRepository.persist(courses);
        if (courseRepository.isPersistent(courses)) {
            return Response.ok().build();
        }
        return Response.serverError().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getCoursesByName/{name}")
    public Response getCoursesByName(@PathParam("name") final String name) {
        Log.info("getCoursesByName");
        Optional<Courses> courses = courseRepository.findByName(name);
        if (courses.isPresent()) {
            return Response.ok(courses.get()).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getRegistrations")
    public Response getRegistrations() {
        Log.info("getRegistrations");
        return Response.ok(registrationRepository.listAll()).build();
    }

    @POST
    @Transactional
    @Path("enrollContestant/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enrollContestant(@PathParam("courseId") Integer id, Contestants contestants) {
        Log.info("enrollContestant");
        try {
            if (registrationRepository.enroll(id, contestants)) {
                return Response.ok().build();
            }
        } catch (ServiceException e) {
            Log.error(e.getLocalizedMessage());
        }
        return Response.serverError().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getContestantsForCourse/{id}")
    public Response getContestantsForCourse(@PathParam("id") final int id) {
        Log.info("getContestantsForCourse");
        List<Contestants> registration = registrationRepository.getContestantsForCourse(id);
        if (!registration.isEmpty()) {
            return Response.ok(registration).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Produces("image/png")
    @Path("createQrCode/{data}")
    public Response createQrCode(@PathParam("data") String data) {
        if (data == null || data.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            return Response.ok(createQrCodeFromString(data)).build();
        } catch (WriterException | IOException e) {
            Log.error(e.getLocalizedMessage());
        }
        return Response.serverError().build();
    }

    private byte[] createQrCodeFromString(String data) throws WriterException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
        return bos.toByteArray();
    }
}
