import { environment } from '../../environments/environment';
import { NgModule, Injectable } from '@angular/core';

@NgModule({}) export class ButtonModule {}
@NgModule({}) export class InputGroupModule {}
@NgModule({}) export class InputGroupAddonModule {}
@NgModule({}) export class TableModule {}
@NgModule({}) export class InputTextModule {}
@NgModule({}) export class InputNumberModule {}
@NgModule({}) export class DropdownModule {}
@NgModule({}) export class MultiSelectModule {}
@NgModule({}) export class CalendarModule {}
@NgModule({}) export class ToastModule {}
@NgModule({}) export class ConfirmDialogModule {}
@NgModule({}) export class DialogModule {}
@NgModule({}) export class PanelModule {}
@NgModule({}) export class CardModule {}
@NgModule({}) export class TabViewModule {}
@NgModule({}) export class SidebarModule {}
@NgModule({}) export class MenuModule {}
@NgModule({}) export class TieredMenuModule {}
@NgModule({}) export class AvatarModule {}
@NgModule({}) export class ChipModule {}
@NgModule({}) export class TagModule {}
@NgModule({}) export class BadgeModule {}
@NgModule({}) export class ProgressSpinnerModule {}
@NgModule({}) export class ProgressBarModule {}
@NgModule({}) export class FileUploadModule {}
@NgModule({}) export class CheckboxModule {}
@NgModule({}) export class RadioButtonModule {}
@NgModule({}) export class SelectButtonModule {}
@NgModule({}) export class AccordionModule {}
@NgModule({}) export class TreeModule {}
@NgModule({}) export class TreeTableModule {}
@NgModule({}) export class DividerModule {}
@NgModule({}) export class ToolbarModule {}
@NgModule({}) export class BreadcrumbModule {}
@NgModule({}) export class PaginatorModule {}
@NgModule({}) export class ImageModule {}
@NgModule({}) export class GalleriaModule {}
@NgModule({}) export class SkeletonModule {}
@NgModule({}) export class InputTextareaModule {}
@NgModule({}) export class AutoCompleteModule {}
@NgModule({}) export class MessagesModule {}
@NgModule({}) export class MessageModule {}
@NgModule({}) export class TooltipModule {}
@NgModule({}) export class OverlayPanelModule {}
@NgModule({}) export class FieldsetModule {}
@NgModule({}) export class ScrollPanelModule {}
@NgModule({}) export class DataViewModule {}
@NgModule({}) export class SplitterModule {}
@NgModule({}) export class StepsModule {}
@NgModule({}) export class MenubarModule {}
@NgModule({}) export class PanelMenuModule {}
@NgModule({}) export class MegaMenuModule {}
@NgModule({}) export class ContextMenuModule {}
@NgModule({}) export class SliderModule {}
@NgModule({}) export class RatingModule {}
@NgModule({}) export class KnobModule {}
@NgModule({}) export class InputSwitchModule {}
@NgModule({}) export class PasswordModule {}
@NgModule({}) export class ColorPickerModule {}
@NgModule({}) export class EditorModule {}
@NgModule({}) export class TimelineModule {}
@NgModule({}) export class VirtualScrollerModule {}
@NgModule({}) export class BlockUIModule {}
@NgModule({}) export class ConfirmPopupModule {}
@NgModule({}) export class DynamicDialogModule {}
@NgModule({}) export class SpeedDialModule {}
@NgModule({}) export class SplitButtonModule {}
@NgModule({}) export class OrderListModule {}
@NgModule({}) export class PickListModule {}
@NgModule({}) export class OrganizationChartModule {}
@NgModule({}) export class TerminalModule {}
@NgModule({}) export class InplaceModule {}
@NgModule({}) export class InputMaskModule {}
@NgModule({}) export class CaptchaModule {}
@NgModule({}) export class CarouselModule {}

@Injectable({ providedIn: 'root' })
export class MessageService {
  add(message: any): void {
    const level = (message?.severity ?? 'info').toUpperCase();
    console.log(`[Toast] ${level}:`, message?.summary, '—', message?.detail);
  }
  clear(): void {}
}

@Injectable({ providedIn: 'root' })
export class ConfirmationService {
  confirm(confirmation: any): void {
    confirmation?.accept?.();
  }
}

@Injectable({ providedIn: 'root' })
export class DialogService {
  open(component: any, config?: any): any { return {}; }
}

export interface MenuItem {
  label?: string;
  icon?: string;
  items?: MenuItem[];
  command?: (event?: any) => void;
  routerLink?: string | any[];
  url?: string;
  target?: string;
  disabled?: boolean;
  visible?: boolean;
  separator?: boolean;
  [key: string]: any;
}

export interface Message {
  severity?: string;
  summary?: string;
  detail?: string;
  [key: string]: any;
}

export interface SortEvent {
  data?: any[];
  mode?: string;
  field?: string;
  order?: number;
  multiSortMeta?: any[];
}

export interface SelectItem {
  label?: string;
  value?: any;
  disabled?: boolean;
  icon?: string;
  title?: string;
  [key: string]: any;
}

export interface TreeNode {
  label?: string;
  data?: any;
  children?: TreeNode[];
  leaf?: boolean;
  expanded?: boolean;
  [key: string]: any;
}

export interface DynamicDialogRef {
  close(result?: any): void;
}

export interface DynamicDialogConfig {
  header?: string;
  width?: string;
  data?: any;
  [key: string]: any;
}
