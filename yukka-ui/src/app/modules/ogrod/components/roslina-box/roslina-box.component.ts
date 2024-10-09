import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import {CdkDrag, CdkDragEnd, CdkDragHandle, CdkDragMove, CdkDragStart, DragRef, Point} from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-roslina-box',
  standalone: true,
  imports: [CommonModule, CdkDrag, CdkDragHandle],
  templateUrl: './roslina-box.component.html',
  styleUrl: './roslina-box.component.css'
})
export class RoslinaBoxComponent implements OnInit {
  @Input() dragPosition = {x: 0, y: 0};
  @ViewChild(CdkDrag, { static: true }) cdkDrag!: CdkDrag;
  zoomScale = 1;

  constructor(public elementRef: ElementRef) {
  }

  ngOnInit(): void {
    console.log('RoslinaBoxComponent');
    console.log('Initial position - x: ' + this.dragPosition.x + ' y: ' + this.dragPosition.y);

  }

  get dragRef(): DragRef {
    return this.cdkDrag['_dragRef'];
  }

  onDragStarted(event: CdkDragStart): void {
    event.source.getFreeDragPosition();

    console.log('STARTED:  x: ' + event.source.getFreeDragPosition().x + ' y: ' + event.source.getFreeDragPosition().y);
    const dragPosition = event.source.getFreeDragPosition();
    const scale = this.zoomScale;

  //  this.dragPosition.x = dragPosition.x / scale;
 //   this.dragPosition.y = dragPosition.y / scale;
    //console.log('Drag started');
  }

  onDrag(event: CdkDragMove) {
   // event.source.getFreeDragPosition();
    //const { x, y } = event.source.getFreeDragPosition();


   // console.log('FREEDRAG|| x: ' + event.source.getFreeDragPosition().x + ' y: ' + event.source.getFreeDragPosition().y);
   // console.log('POIINTER|| x: ' + event.pointerPosition.x + ' y: ' + event.pointerPosition.y);
    //this.dragPosition = event.source.getFreeDragPosition();

    // This is okay
    const dragPosition = event.source.getFreeDragPosition();
    const scale = this.zoomScale;
    //this.dragPosition.x = dragPosition.x / scale;
   // this.dragPosition.y = dragPosition.y / scale;
  }

  onDragEnded(event: CdkDragEnd): void {
    const dragPosition = event.source.getFreeDragPosition();
    const scale = this.zoomScale;

    //this.dragPosition = dragPosition;
    this.dragPosition.x = dragPosition.x / scale;
    this.dragPosition.y = dragPosition.y / scale;
    console.log('x: ' + this.dragPosition.x + ' y: ' + this.dragPosition.y + ' scale: ' + this.zoomScale);
  }

  setZoomScale(scale: number) {
    this.zoomScale = scale;
  }
}
