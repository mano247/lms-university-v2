import { NgFor, NgIf, NgStyle } from '@angular/common';
import { Component, OnInit, NO_ERRORS_SCHEMA } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { Student } from '../../../model/users/student';
import { StudentService } from '../../../services/student.service';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { TeacherService } from '../../../services/teacher.service';
import { FormsModule } from '@angular/forms';
import { ExamAttemptService } from '../../../services/exam-attempt.service';
import { ThesisService } from '../../../services/thesis.service';
import { Course } from '../../../model/academic/course';

@Component({
  schemas: [NO_ERRORS_SCHEMA],
  selector: 'app-student-list',
  standalone: true,
  imports: [TableModule, NgFor, DropdownModule, InputGroupModule, FormsModule, InputGroupAddonModule, 
    ButtonModule, DialogModule, DividerModule, NgStyle, NgIf],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent implements OnInit{
  visible: boolean = false;

  filteredStudents: Student[] = [];
  studenti: Student[] = [];

  coursei: Course[] = [];
  izabranicourse: any;

  // godinaUpisa: any;
  // smer: any;
  enrollments: any;
  // polozeni: any;
  // neuspesnaPolaganja: any;
  prijavljeniIspiti: any;

  studentDetails: any = {}
  studentInfo: any;

  Thesis: any = {}

  ETCS:number = 0;
  avgOcena:number = 0;

  pretraga = {
    firstName: '',
    lastName: '',
    indexNumber: '',
    // godinaUpisa: '',
    // prosecnaOcena: ''
  };

  constructor(private StudentService: StudentService, private TeacherService: TeacherService, 
    private ExamAttemptService: ExamAttemptService, private ThesisService: ThesisService){}

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      const parsedUser = JSON.parse(user);
      const id = parsedUser.id;
      this.loadCourses(id);
    }
  }

  loadCourses(id: number) {
    this.TeacherService.mojicoursei(id).subscribe(coursei => {
      this.coursei = coursei;
      if (this.coursei.length > 0) {

        this.izabranicourse = this.coursei[0];
        this.filteredStudents = this.izabranicourse.students;
      }
    });
  }

  oncourseChange(event: any) {
    console.log("Izabrani Course:", event.value); 
  
    if (event.value && event.value.students) {
      this.filteredStudents = event.value.students;
    } else {
      this.filteredStudents = [];
    }
  }

  getStudentInfo(id: number) {
    this.StudentService.getenrollments(id).subscribe(x=>{
      this.studentDetails.enrollments = x;
    })

    this.ExamAttemptService.getPrijavljeni(id).subscribe(x=>{
      this.studentDetails.prijavljeniIspiti = x;
    });

    this.ThesisService.findByStudent(id).subscribe(x=>{
      this.studentDetails.Thesis = x;
    })

    this.StudentService.polozeniIspiti(id).subscribe(x=>{
      this.studentDetails.polozeniIspiti = x;

      if (this.studentDetails.polozeniIspiti.length > 0) {
        const sumaOcena = this.studentDetails.polozeniIspiti.reduce((sum: number, ispit: any) => sum + ispit.ocena, 0);
        this.studentDetails.avgOcena = sumaOcena / this.studentDetails.polozeniIspiti.length;
      } else {
        this.avgOcena = 0;  
      }

      this.studentDetails.ETCS = this.studentDetails.polozeniIspiti.reduce((sum: number, ispit: any) => sum + ispit.ects, 0);

    })

    this.StudentService.nepolozeniIspiti(id).subscribe(x=>{
      this.studentDetails.nepolozeniIspiti = x;
    })


  }


  openStudentDetails(student: Student){
    this.visible = true;
    this.studentDetails = student;
    if(student.id){
      this.getStudentInfo(student.id);
    }
    console.log(this.studentDetails);
  }

  filterStudents(){
    this.filteredStudents = this.studenti.filter(s =>
      (this.pretraga.firstName ? (s.firstName || '').toLowerCase().includes(this.pretraga.firstName.toLowerCase()) : true) &&
      (this.pretraga.lastName ? (s.lastName || '').toLowerCase().includes(this.pretraga.lastName.toLowerCase()) : true) &&
      (this.pretraga.indexNumber ? (s.indexNumber || '').toLowerCase().includes(this.pretraga.indexNumber.toLowerCase()) : true)
    );
  }

  clearFilter(){
    this.filteredStudents = this.studenti;
    this.pretraga = {
      firstName: '',
      lastName: '',
      indexNumber: '',
    }
  }


}


