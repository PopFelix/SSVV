package ssvv.example.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ServiceIntegrationTest {

    public Service service;

    String filenameStudent = "studenti.xml";
    String filenameTema = "teme.xml";
    String filenameNota = "note.xml";

    @Before
    public void init() throws IOException {


        PrintWriter filenameStudentPW = new PrintWriter(filenameStudent);
        PrintWriter filenameTemaPW = new PrintWriter(filenameTema);
        PrintWriter filenameNotaPW = new PrintWriter(filenameNota);
        filenameStudentPW.write("\n");
        filenameTemaPW.write("\n");
        filenameNotaPW.write("\n");

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        StudentXMLRepo fileRepository1 = new StudentXMLRepo("studenti.xml");
        TemaXMLRepo fileRepository2 = new TemaXMLRepo("teme.xml");
        NotaXMLRepo fileRepository3 = new NotaXMLRepo("note.xml");
        NotaValidator notaValidator = new NotaValidator(fileRepository1, fileRepository2);

        service = new Service(fileRepository1, studentValidator, fileRepository2, temaValidator, fileRepository3, notaValidator);

    }



    @Test
    public void addStudent() {

        service.addStudent(new Student("2111a", "Felix", 936, "email@email.com"));
        Student student = service.findStudent("2111a");
        assertEquals(student.getID(), "2111a");
    }

    @Test
    public void addAssignment() {
        service.addTema(new Tema("tema1", "ddd", 7, 2));
        Iterable<Tema> teme = service.getAllTeme();
        assertEquals(teme.iterator().next().getID(), "tema1");
    }


    @Test
    public void addGrade() {
        service.addStudent(new Student("2111a", "Felix", 936, "email@email.com"));
        service.addTema(new Tema("tema1", "ddd", 7, 2));
        service.addNota(new Nota("1", "2111a", "tema1", 3, LocalDate.of(2018,10,30)), "ok");
        Iterable<Nota> note = service.getAllNote();
        assertEquals(note.iterator().next().getID(),"1");
    }


}