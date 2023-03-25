package ssvv.example.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Student;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.ValidationException;


class ServiceTest {
    public static final String ID_INCORECT = "Id incorect!";
    public static final String NUME_INCORECT = "Nume incorect!";
    public static final String GRUPA_INCORECTA = "Grupa incorecta!";
    public static final String EMAIL_INCORECT = "Email incorect!";
    private Service service;
    private StudentXMLRepo repo;
    private StudentValidator validator;
    String filenameStudent = "fisiere/test/Studenti.xml";
    private Student student;

    @BeforeEach
    public void init() {
        repo = new StudentXMLRepo(filenameStudent);
        validator = new StudentValidator();
        service = new Service(repo, validator, null, null, null, null);
        student = new Student("1", "name", 933, "email");
    }

    @AfterEach
    public void tearDown() {
        service.deleteStudent("1");
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
     * In order to check if id is null, we shouldn't call the equals method before the null check in the validator
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
}