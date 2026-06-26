import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="unauthorized-page">
      <div class="unauthorized-card">
        <div class="code">403</div>
        <h1>Access Denied</h1>
        <p>You do not have permission to view this page.</p>
        <a routerLink="/" class="btn-primary">Back to Home</a>
      </div>
    </div>
  `,
  styles: [`
    .unauthorized-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--color-background);
      padding: var(--space-md);
    }
    .unauthorized-card {
      text-align: center;
      padding: var(--space-xl);
    }
    .code {
      font-family: var(--font-mono);
      font-size: 96px;
      font-weight: 700;
      color: var(--color-outline-variant);
      line-height: 1;
      margin-bottom: var(--space-sm);
    }
    h1 {
      font-family: var(--font-serif);
      font-size: 24px;
      color: var(--color-primary);
      margin: 0 0 12px;
    }
    p {
      color: var(--color-secondary);
      margin: 0 0 var(--space-md);
    }
  `],
})
export class UnauthorizedComponent {}
