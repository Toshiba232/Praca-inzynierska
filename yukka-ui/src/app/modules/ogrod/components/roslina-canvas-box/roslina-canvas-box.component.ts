import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-roslina-canvas-box',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roslina-canvas-box.component.html',
  styleUrl: './roslina-canvas-box.component.css'
})
export class RoslinaCanvasBoxComponent implements OnInit {
  @Input() dragPosition = { x: 0, y: 0 };
  @ViewChild('box', { static: true }) boxElement!: ElementRef;


  zoomScale = 1;
  private isDragging = false;
  private startX = 0;
  private startY = 0;

  constructor(public elementRef: ElementRef) {}

  ngOnInit(): void {
    this.updatePosition();
  }

  setZoomScale(scale: number) {
    this.zoomScale = scale;
    this.updatePosition();
  }

  onMouseDown(event: MouseEvent) {
    this.isDragging = true;
    this.startX = event.clientX - this.dragPosition.x;
    this.startY = event.clientY - this.dragPosition.y;
    document.addEventListener('mousemove', this.onMouseMove);
    document.addEventListener('mouseup', this.onMouseUp);
  }

  onMouseMove = (event: MouseEvent) => {
    if (!this.isDragging) return;
    this.dragPosition.x = (event.clientX - this.startX) / this.zoomScale;
    this.dragPosition.y = (event.clientY - this.startY) / this.zoomScale;
    this.updatePosition();
  };

  onMouseUp = () => {
    this.isDragging = false;
    document.removeEventListener('mousemove', this.onMouseMove);
    document.removeEventListener('mouseup', this.onMouseUp);
  };

  updatePosition() {
    const el = this.boxElement.nativeElement;
    el.style.transform = `translate(${this.dragPosition.x}px, ${this.dragPosition.y}px)`;
  }
}
