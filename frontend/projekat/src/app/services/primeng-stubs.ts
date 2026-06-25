import { NgModule, Injectable } from '@angular/core';

// Stub NgModules for all PrimeNG module imports
@NgModule({}) export class ButtonModule {}
@NgModule({}) export class TableModule {}
@NgModule({}) export class DividerModule {}
@NgModule({}) export class ToastModule {}
@NgModule({}) export class DialogModule {}
@NgModule({}) export class DropdownModule {}
@NgModule({}) export class InputGroupModule {}
@NgModule({}) export class InputGroupAddonModule {}
@NgModule({}) export class DataViewModule {}
@NgModule({}) export class ConfirmPopupModule {}
@NgModule({}) export class TabViewModule {}
@NgModule({}) export class TabPanelModule {}
@NgModule({}) export class CalendarModule {}
@NgModule({}) export class ProgressSpinnerModule {}
@NgModule({}) export class MenubarModule {}
@NgModule({}) export class InputSwitchModule {}
@NgModule({}) export class ConfirmDialogModule {}
@NgModule({}) export class CardModule {}
@NgModule({}) export class TieredMenuModule {}
@NgModule({}) export class ProgressBarModule {}
@NgModule({}) export class MenuModule {}
@NgModule({}) export class InputTextareaModule {}
@NgModule({}) export class AvatarModule {}

// Stub services — replaced by real notification service in later phase
@Injectable({ providedIn: 'root' })
export class MessageService {
  add(message: any): void {
    console.log('[Toast]', message?.severity?.toUpperCase(), message?.summary, message?.detail);
  }
  clear(): void {}
}

@Injectable({ providedIn: 'root' })
export class ConfirmationService {
  confirm(confirmation: any): void {
    confirmation?.accept?.();
  }
}

// Type stubs
export interface MenuItem {
  label?: string;
  icon?: string;
  items?: MenuItem[];
  command?: (event?: any) => void;
  routerLink?: string | string[];
  [key: string]: any;
}

export interface SortEvent {
  data?: any[];
  mode?: string;
  field?: string;
  order?: number;
  multiSortMeta?: any[];
}

export interface Message {
  severity?: string;
  summary?: string;
  detail?: string;
  [key: string]: any;
}
