package com.example.demo.studentController;

import com.example.demo.repositoory.StudentRepo;
import com.example.demo.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.favre.lib.crypto.bcrypt.*;

@RestController
public class StudentController {

    private StudentRepo studentRepo;
    public StudentController(StudentRepo studentRepo){ this.studentRepo = studentRepo; }

    // Get all the students
    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents(){

        try{

            /* Create a list and get all the student data
             and store all that data in a list
             */
            List<Student> studentList = new ArrayList<>();
            studentRepo.findAll().forEach(studentList::add);

            // Check if list is empty
            if(studentList.isEmpty()){

                // Return http response
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // Return user data and http response
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a student by their id
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id){
        Optional<Student> studentData = studentRepo.findById(id);

        if(studentData.isPresent()){
            return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Add a new student
    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){

        // Encrypt user password
        String hashedPsw = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(12, student.getPassword().toCharArray());
        student.setPassword(hashedPsw);

        // Add the student data
        Student studentObj = studentRepo.save(student);

        return new ResponseEntity<>(studentObj, HttpStatus.OK);
    }

    // Update student details
    @PostMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student newStudData){

        // Find the use by their id
        Optional<Student> studentData = studentRepo.findById(id);

        // Verify is user exists
        if(studentData.isPresent()){

            // Set the new student data
            Student updatedStudent = studentData.get();
            updatedStudent.setName(newStudData.getName());
            updatedStudent.setAge(newStudData.getAge());
            updatedStudent.setLastName(newStudData.getLastName());
            updatedStudent.setEmail(newStudData.getEmail());

            // Save the updated student data
            Student savedStudent = studentRepo.save(updatedStudent);
            return new ResponseEntity<>(savedStudent, HttpStatus.OK);
        }

        // Return a not-found error if the student does not exist
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a user
    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id){

        studentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
