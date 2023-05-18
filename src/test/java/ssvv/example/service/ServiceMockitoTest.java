package ssvv.example.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceMockitoTest {
    private StudentXMLRepo studentRepo;
    @Mock
    private StudentXMLRepo mockStudentRepo;

    private TemaXMLRepo temaRepo;
    @Mock
    private TemaXMLRepo mockTemaRepo;
    private NotaXMLRepo notaRepo;
    @Mock
    private NotaXMLRepo mockNotaRepo;

    private Service service;

    private StudentValidator studentValidator;
    private TemaValidator temaValidator;
    private NotaValidator notaValidator;


    String filenameStudent = "studenti.xml";
    String filenameTema = "teme.xml";
    String filenameNota = "note.xml";

    @After
    public void tearDown() {
        new File(filenameStudent).delete();
        new File(filenameTema).delete();
        new File(filenameNota).delete();
    }

    @Before
    public void setup() throws IOException {
        mockStudentRepo = mock(StudentXMLRepo.class);
        mockTemaRepo = mock(TemaXMLRepo.class);
        mockNotaRepo = mock(NotaXMLRepo.class);

        PrintWriter filenameStudentPW = new PrintWriter(filenameStudent);
        PrintWriter filenameTemaPW = new PrintWriter(filenameTema);
        PrintWriter filenameNotaPW = new PrintWriter(filenameNota);
        filenameStudentPW.write("\n");
        filenameTemaPW.write("\n");
        filenameNotaPW.write("\n");

        studentValidator = new StudentValidator();
        temaValidator = new TemaValidator();
        studentRepo = new StudentXMLRepo("studenti.xml");
        temaRepo = new TemaXMLRepo("teme.xml");
        notaRepo = new NotaXMLRepo("note.xml");
        notaValidator = new NotaValidator(studentRepo, temaRepo);

    }

    @Test
    public void addStudentXML_shouldSucceed() throws IOException {
        service = new Service(studentRepo, studentValidator, mockTemaRepo, temaValidator, mockNotaRepo, notaValidator);

        assertDoesNotThrow(() -> service.addStudent(new Student("1", "Sergiu", 933, "email@email.com")));

        StudentXMLRepo studentRepo = new StudentXMLRepo(filenameStudent);
        Iterable<Student> students = studentRepo.findAll();
        List<Student> studentList = new ArrayList<>();
        students.forEach(studentList::add);

        assertEquals(studentList.size(), 1);
        assertEquals(studentList.get(0).getID(), "1");
        assertEquals(studentList.get(0).getNume(), "Sergiu");
        assertEquals(studentList.get(0).getGrupa(), 933);
    }

    @Test
    public void addTema_Integration() {
        service = new Service(studentRepo, studentValidator, temaRepo, temaValidator, mockNotaRepo, notaValidator);

        assertDoesNotThrow(() -> service.addStudent(new Student("1", "Sergiu", 933, "email@email.com")));
        assertDoesNotThrow(() -> service.addTema(new Tema("tema1", "ddd", 7, 2)));

        StudentXMLRepo studentRepo = new StudentXMLRepo(filenameStudent);
        Iterable<Student> students = studentRepo.findAll();
        ArrayList<Student> studentList = new ArrayList<>();
        students.forEach(studentList::add);

        TemaXMLRepo assignmentRepo = new TemaXMLRepo(filenameTema);
        Iterable<Tema> assignments = assignmentRepo.findAll();
        ArrayList<Tema> assignmentList = new ArrayList<>();
        assignments.forEach(assignmentList::add);

        assertEquals(studentList.size(), 1);
        assertEquals(studentList.get(0).getID(), "1");
        assertEquals(studentList.get(0).getNume(), "Sergiu");
        assertEquals(studentList.get(0).getGrupa(), 933);

        assertEquals(assignmentList.size(), 1);
        assertEquals(assignmentList.get(0).getID(), "tema1");
        assertEquals(assignmentList.get(0).getDescriere(), "ddd");
        assertEquals(assignmentList.get(0).getDeadline(), 7);
        assertEquals(assignmentList.get(0).getPrimire(), 2);

    }

    @Test
    public void addGrade_Integration() {
        service = new Service(mockStudentRepo,studentValidator, mockTemaRepo, temaValidator, notaRepo, mock(NotaValidator.class));

        Student student = new Student("adsa", "Sergiu", 933, "email@email.com");
        Tema tema = new Tema("tema1", "ddd", 7, 2);

        when(this.mockStudentRepo.findOne("adsa")).thenReturn(student);
        when(this.mockTemaRepo.findOne("tema1")).thenReturn(tema);

        assertDoesNotThrow(() -> service.addNota(new Nota("1", "adsa", "tema1", 3, LocalDate.of(2018, 10, 30)), "ok"));


        NotaXMLRepo gradeRepo = new NotaXMLRepo(filenameNota);
        Iterable<Nota> grades = gradeRepo.findAll();
        ArrayList<Nota> gradeList = new ArrayList<>();
        grades.forEach(gradeList::add);

        assertEquals(gradeList.size(), 1);
        assertEquals(gradeList.get(0).getNota(), 3.0, 0.01);
    }
}
