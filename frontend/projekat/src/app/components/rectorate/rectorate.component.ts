import { Component, OnInit } from '@angular/core';
import { RectorateService } from '../../services/rectorate.service';
import { Rectorate } from '../../model/rectorate';

@Component({
  selector: 'app-rectorate',
  standalone: true,
  imports: [],
  templateUrl: './rectorate.component.html',
  styleUrl: './rectorate.component.css',
})
export class RectorateComponent implements OnInit {
  rectorate: Rectorate = {
    name: '',
    contact: '',
    image: '',
    address: '',
    universities: [],
    rectorName: '',
  };
  isLoading = true;

  constructor(private rectorateService: RectorateService) {}

  ngOnInit(): void {
    this.rectorateService.getById(1).subscribe({
      next: (x) => {
        this.rectorate = x;
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  readonly functions = [
    {
      icon: 'map',
      title: 'Strategic Planning',
      desc: 'Developing and implementing strategic plans that guide the university toward its long-term objectives and academic vision.',
    },
    {
      icon: 'auto_stories',
      title: 'Academic Policy',
      desc: 'Defining academic standards and policy to ensure high-quality education and research across all faculties.',
    },
    {
      icon: 'manage_accounts',
      title: 'Administrative Management',
      desc: 'Overseeing administrative operations, the budget, and university resources for efficient functioning of all departments.',
    },
    {
      icon: 'public',
      title: 'Representation',
      desc: 'Representing the university at official and international forums, and fostering collaboration with partner institutions.',
    },
  ];
}
