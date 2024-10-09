import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaCanvasBoxComponent } from './roslina-canvas-box.component';

describe('RoslinaCanvasBoxComponent', () => {
  let component: RoslinaCanvasBoxComponent;
  let fixture: ComponentFixture<RoslinaCanvasBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaCanvasBoxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaCanvasBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
