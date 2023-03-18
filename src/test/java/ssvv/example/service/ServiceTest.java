package ssvv.example.service;

import ssvv.example.domain.Student;
import org.junit.jupiter.api.*;

import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.validation.StudentValidator;


class ServiceTest {
    private Service service;
    private StudentXMLRepo repo;
    private StudentValidator validator;
    String filenameStudent = "fisiere/test/Studenti.xml";
    private Student student;
    @BeforeEach
    public  void init(){
        repo = new StudentXMLRepo(filenameStudent);
        validator = new StudentValidator();
        service = new Service(repo,validator,null,null,null,null);
        student = new Student("1","name",933,"email");
    }
    @AfterEach
    public void tearDown(){
        service.deleteStudent("1");
    }
    @Test
    public void givenValidStudent_shouldReturnStudent(){
        Student addedStudent = service.addStudent(student);
        Assertions.assertNull(addedStudent);
    }

    @Test
    public void givenValidStudent_withStudentAlreadySavedInMemory_shouldReturnNull(){
        Student addedStudent = service.addStudent(student);
        Assertions.assertEquals(student,service.addStudent(student));

    }
}