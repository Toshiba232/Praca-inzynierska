import { Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { RoslinaBoxComponent } from "../../components/roslina-box/roslina-box.component";
import { DragDropModule, DragRef, Point } from '@angular/cdk/drag-drop';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { CommonModule } from '@angular/common';
import { RoslinaCanvasBoxComponent } from "../../components/roslina-canvas-box/roslina-canvas-box.component";



@Component({
  selector: 'app-dzialka-page',
  standalone: true,
  imports: [CommonModule, RoslinaBoxComponent, DragDropModule, ScrollingModule, RoslinaCanvasBoxComponent],
  templateUrl: './dzialka-page.component.html',
  styleUrl: './dzialka-page.component.css'
})
export class DzialkaPageComponent implements OnInit, OnDestroy {
  @ViewChild('canvas', { static: true }) canvasElement!: ElementRef;


  @ViewChild('boundary', { static: true }) boundaryElement!: ElementRef;
  @ViewChild('innerBoundary', { static: true }) innerBoundaryElement!: ElementRef;
  @ViewChildren(RoslinaBoxComponent) roslinaBoxes!: QueryList<RoslinaBoxComponent>;
  @ViewChildren(RoslinaCanvasBoxComponent) roslinaCanvasBoxes!: QueryList<RoslinaCanvasBoxComponent>;

  private scrollThreshold = 60;
  private scrollSpeed = 1;
  private isScrolling = false;



  ngOnInit() {
    if (this.boundaryElement) {
      this.boundaryElement.nativeElement.addEventListener('mousedown', this.onMouseDown);
      this.boundaryElement.nativeElement.addEventListener('mouseup', this.onMouseUp);
      this.boundaryElement.nativeElement.addEventListener('mouseleave', this.onMouseUp);
    }
  }

  ngOnDestroy() {
    if (this.boundaryElement) {
      this.boundaryElement.nativeElement.removeEventListener('mousedown', this.onMouseDown);
      this.boundaryElement.nativeElement.removeEventListener('mouseup', this.onMouseUp);
      this.boundaryElement.nativeElement.removeEventListener('mouseleave', this.onMouseUp);
      this.boundaryElement.nativeElement.removeEventListener('mousemove', this.smoothScroll);
    }
  }

  onMouseDown = (event: MouseEvent) => {
    this.isScrolling = true;
    this.boundaryElement.nativeElement.addEventListener('mousemove', this.smoothScroll);

    this.smoothScroll(event);
  };

  onMouseUp = () => {
    this.isScrolling = false;
    this.boundaryElement.nativeElement.addEventListener('mousemove', this.smoothScroll);

  };

  setZoom(zoom: number, el: HTMLElement) {
    const transformOrigin = [0, 0];
    const s = `scale(${zoom})`;
    const oString = `${transformOrigin[0] * 100}% ${transformOrigin[1] * 100}%`;

    const prefixes = ["webkit", "moz", "ms", "o"];
    prefixes.forEach(prefix => {
      (el.style as any)[`${prefix}Transform`] = s;
      (el.style as any)[`${prefix}TransformOrigin`] = oString;
    });

    el.style.transform = s;
    el.style.transformOrigin = oString;

    console.log(this.roslinaBoxes);
    //this.roslinaBoxes.forEach(box => box.setZoomScale(zoom));

    this.roslinaCanvasBoxes.forEach(box => box.setZoomScale(zoom));

   /*

    this.roslinaBoxes.forEach(box => {
      box.setZoomScale(zoom);
      const dragRef = box.dragRef;
      if (dragRef) {
        const position = dragRef.getFreeDragPosition();
        console.log('Position: ' + position.x + ' ' + position.y);
        dragRef['_pointerDown']({ clientX: position.x, clientY: position.y } as MouseEvent);
        dragRef['_pointerMove']({ clientX: position.x, clientY: position.y } as MouseEvent);
        dragRef['_pointerUp']({ clientX: position.x, clientY: position.y } as MouseEvent);
      }
    });
*/


  }

  showVal(event: any) {
    const value = event?.target.value;
    const zoomScale = Number(value) / 10;
    //this.setZoom(zoomScale, this.innerBoundaryElement.nativeElement);
    this.setZoom(zoomScale, this.canvasElement.nativeElement);
    //this.setZoom(zoomScale, this.boundaryElement.nativeElement);
  }


  smoothScroll = (event: MouseEvent) => {
    return;
    if (!this.isScrolling) return;

    const boundaryElement = this.boundaryElement.nativeElement;
    if (!boundaryElement) return;

    const rect = boundaryElement.getBoundingClientRect();

    let x = 0;
    let y = 0;

    if (event.clientY < rect.top + this.scrollThreshold) {
      y = -this.scrollSpeed;
    } else if (event.clientY > rect.bottom - this.scrollThreshold) {
      y = this.scrollSpeed;
    }

    if (event.clientX < rect.left + this.scrollThreshold) {
      x = -this.scrollSpeed;
    } else if (event.clientX > rect.right - this.scrollThreshold) {
      x = this.scrollSpeed;
    }

    boundaryElement.scrollTop += y;
    boundaryElement.scrollLeft += x;

    setTimeout(() => this.smoothScroll(event), 16);
  };
}
