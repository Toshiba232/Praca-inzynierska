import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegistrationRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services/authentication.service';
import { TokenService } from '../../services/token/token.service';
import { register } from '../../services/fn/authentication/register';
import { ErrorHandlingService } from '../../services/error-handler/error-handling.service';
import { ErrorMsgComponent } from "../../components/error-msg/error-msg.component";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ErrorMsgComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    nazwa: 'TestowanyTestowiec24',
    email: 'beb@email.pl',
    haslo: 'testowiec12345678',
    powtorzHaslo: 'testowiec12345678'
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService,
    private errorHandlingService: ErrorHandlingService
  ) {

  }

  register () {
    this.errorMsg = [];
    this.authService.register({
      body: this.registerRequest
    }).subscribe( {
        next: (res) => {
          console.log(res);
          this.router.navigate(['aktywacja-konta']);
          //this.login();
        },
        error: (err) => {
          console.log(err);
          this.errorMsg = this.errorHandlingService.handleErrors(err, this.errorMsg);
          //  if (err.error.validationErrors) {
          //   this.errorMsg = err.error.validationErrors;
          // } else if (typeof err.error === 'string') {
          //   this.errorMsg.push(err.error);
          // } else if (err.error.error) {
          //   this.errorMsg.push(err.error.error);
          // } else {
          //   this.errorMsg.push(err.message);
          // }
        }
    });
  }

  login() {
    this.authService.login({
      body: { email: this.registerRequest.email, haslo: this.registerRequest.haslo }
    }).subscribe({
      next: (res) => {
        console.log(res);
        this.tokenService.token = res.token as string;
        this.router.navigate(['aktywacja-konta']);
      },
      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else if (err.error.error) {
          this.errorMsg.push(err.error.error);
        } else {
          this.errorMsg.push(err.message);
        }
      }
    });
  }

}
