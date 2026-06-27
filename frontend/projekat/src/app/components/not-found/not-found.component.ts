import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="nf-page">
      <div class="nf-content">
        <div class="nf-code">404</div>
        <h1 class="nf-title">Page Not Found</h1>
        <p class="nf-desc">The page you're looking for doesn't exist or has been moved.</p>
        <div class="nf-actions">
          <a routerLink="/" class="btn-primary">
            <span class="material-symbols-outlined">home</span>
            Back to Home
          </a>
          <a routerLink="/menu" class="btn-outlined">
            <span class="material-symbols-outlined">dashboard</span>
            My Dashboard
          </a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    :host { display: block; }

    .nf-page {
      min-height: calc(100vh - 64px);
      display: flex;
      align-items: center;
      justify-content: center;
      background: #faf9fc;
      padding: 24px;
    }

    .nf-content {
      text-align: center;
      max-width: 480px;
    }

    .nf-code {
      font-family: 'JetBrains Mono', 'Courier New', monospace;
      font-size: 120px;
      font-weight: 700;
      line-height: 1;
      color: #e4e2e6;
      margin-bottom: 24px;
      letter-spacing: -4px;
    }

    .nf-title {
      font-family: 'Merriweather', Georgia, serif;
      font-size: 28px;
      font-weight: 700;
      color: #002444;
      margin: 0 0 12px;
    }

    .nf-desc {
      font-family: 'Inter', system-ui, sans-serif;
      font-size: 15px;
      color: #545f72;
      margin: 0 0 32px;
      line-height: 1.6;
    }

    .nf-actions {
      display: flex;
      gap: 12px;
      justify-content: center;
      flex-wrap: wrap;
    }

    .btn-primary {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      height: 44px;
      padding: 0 24px;
      background: #002444;
      color: #ffddb1;
      border: none;
      border-radius: 8px;
      font-family: 'Inter', system-ui, sans-serif;
      font-size: 14px;
      font-weight: 700;
      text-decoration: none;
      transition: background 0.15s;
    }

    .btn-primary:hover { background: #003a66; }
    .btn-primary .material-symbols-outlined { font-size: 18px; }

    .btn-outlined {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      height: 44px;
      padding: 0 24px;
      border: 1.5px solid #002444;
      border-radius: 8px;
      background: transparent;
      color: #002444;
      font-family: 'Inter', system-ui, sans-serif;
      font-size: 14px;
      font-weight: 600;
      text-decoration: none;
      transition: background 0.15s;
    }

    .btn-outlined:hover { background: rgba(0, 36, 68, 0.06); }
    .btn-outlined .material-symbols-outlined { font-size: 18px; }

    @media (max-width: 480px) {
      .nf-code { font-size: 80px; }
      .nf-title { font-size: 22px; }
      .nf-actions { flex-direction: column; align-items: center; }
    }
  `],
})
export class NotFoundComponent {}
