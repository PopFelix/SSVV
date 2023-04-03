package ssvv.example.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;
import ssvv.example.validation.ValidationException;


class ServiceTest {
    public static final String ID_INCORECT = "Id incorect!";
    public static final String NUME_INCORECT = "Nume incorect!";
    public static final String GRUPA_INCORECTA = "Grupa incorecta!";
    public static final String EMAIL_INCORECT = "Email incorect!";
    private Service service;
    private StudentXMLRepo studentRepo;
    private StudentValidator studentValidator;
    private TemaXMLRepo assignmentRepo;
    private TemaValidator assignmentValidator;
    String filenameStudent = "fisiere/test/Studenti.xml";
    String filenameAssignment = "fisiere/test/Teme.xml";
    private Student student;
    private Tema assignment;

    @BeforeEach
    public void init() {
        studentRepo = new StudentXMLRepo(filenameStudent);
        studentValidator = new StudentValidator();
        assignmentRepo = new TemaXMLRepo(filenameAssignment);
        assignmentValidator = new TemaValidator();
        service = new Service(studentRepo, studentValidator, assignmentRepo, assignmentValidator, null, null);
        student = new Student("1", "name", 933, "email");
        assignment = new Tema("1","desc",2,3);

    }

    @AfterEach
    public void tearDown() {
        service.deleteStudent("1");
        service.deleteTema("1");
    }

    @Test
    public void givenValidStudent_shouldReturnStudent() {
        Student addedStudent = service.addStudent(student);
        Assertions.assertNull(addedStudent);
    }

    @Test
    public void givenValidStudent_withStudentAlreadySavedInMemory_shouldReturnNull() {
        Student addedStudent = service.addStudent(student);
        Assertions.assertEquals(student, service.addStudent(student));

    }

    @Test
    void givenInvalidStudentId_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("", "name", 331, "email")));
        Assertions.assertEquals(ID_INCORECT, thrown.getMessage());
    }

    /*
     * In order to check if id is null, we shouldn't call the equals method before the null check in the studentValidator
     * Changed order so null check is before empty check
     * */
    @Test
    void givenNullStudentId_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student(null, "name", 331, "email")));
        Assertions.assertEquals(ID_INCORECT, thrown.getMessage());
    }

    @Test
    void givenEmptyStudentName_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("122", "", 331, "email")));
        Assertions.assertEquals(NUME_INCORECT, thrown.getMessage());
    }

    /*
     * Same goes for name
     * */
    @Test
    void givenNullStudentName_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("122", null, 331, "email")));
        Assertions.assertEquals(NUME_INCORECT, thrown.getMessage());
    }

    @Test
    void givenNegativeStudentGroup_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("122", "nume", -123, "email")));
        Assertions.assertEquals(GRUPA_INCORECTA, thrown.getMessage());
    }

    @Test
    void givenNullStudentEmail_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("122", "nume", 123, null)));
        Assertions.assertEquals(EMAIL_INCORECT, thrown.getMessage());
    }

    @Test
    void givenEmptyStudentEmail_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addStudent(new Student("122", "nume", 123, null)));
        Assertions.assertEquals(EMAIL_INCORECT, thrown.getMessage());
    }

    @Test
    void givenValidAssignment_shouldReturnAssignment() {
        Tema addedAssignment = service.addTema(assignment);
        Assertions.assertNull(addedAssignment);
    }

    @Test
    public void givenValidAssignment_withAssignmentAlreadySavedInMemory_shouldReturnNull() {
        Tema addedAssignment = service.addTema(assignment);
        Assertions.assertEquals(assignment, service.addTema(assignment));

    }

    /*
    * Same thing with the null safe check
    * */
    @Test
    void givenNullAssignmentId_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addTema(new Tema(null, "desc", 1, 3)));
        Assertions.assertEquals("Numar tema invalid!", thrown.getMessage());
    }

    @Test
    void givenEmptyAssignmentId_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addTema(new Tema("", "desc", 1, 3)));
        Assertions.assertEquals("Numar tema invalid!", thrown.getMessage());
    }

    @Test
    void givenEmptyAssignmentDesc_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addTema(new Tema("2", "", 1, 3)));
        Assertions.assertEquals("Descriere invalida!", thrown.getMessage());
    }

    @Test
    void givenInvalidAssignmentDeadline_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addTema(new Tema("2", "desc", 122, 3)));
        Assertions.assertEquals("Deadlineul trebuie sa fie intre 1-14.", thrown.getMessage());
    }

    @Test
    void givenInvalidAssignmentPrimire_shouldThrowException() {
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> service.addTema(new Tema("2", "desc", 1, 213)));
        Assertions.assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", thrown.getMessage());
    }
}