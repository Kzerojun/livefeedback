import {LoginRequestDto} from "./request/auth";
import LoginResponseDto from "./response/auth/login-response.dto";
import ResponseDto from "./response/response.dto";
import axios from "axios";
import SignUpRequestDto from "./request/auth/sign-up-request.dto";
import {SignUpResponseDto} from "./response/auth/sign-up-response.dto";

const DOMAIN = 'http://localhost:8080';

const authorization = (accessToken: string) => {
  return {headers: {Authorization: `Bearer ${accessToken}`}}
};

const LOGIN_URL = () => `${DOMAIN}/login`;

export const logInRequest = async (requestBody: LoginRequestDto) => {
  return await axios.post(LOGIN_URL(), requestBody)
  .then(response => {
    const responseBody: LoginResponseDto = response.data;
    return responseBody;
  })
  .catch(error => {
    if (!error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
  });
}

const SIGN_UP_URL = () => `${DOMAIN}/signup`
export const signUpRequest = async (requestBody: SignUpRequestDto) => {
  return await axios.post(SIGN_UP_URL(), requestBody)
  .then(response => {
    const responseBody: SignUpResponseDto = response.data;
    return responseBody;
  })
  .catch(error => {
    if (!error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
  });
}

