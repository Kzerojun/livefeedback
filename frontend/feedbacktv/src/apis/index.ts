import {LoginRequestDto} from "./request/auth";
import LoginResponseDto from "./response/auth/login-response.dto";
import ResponseDto from "./response/response.dto";
import axios from "axios";
import SignUpRequestDto from "./request/auth/sign-up-request.dto";
import SignUpResponseDto from "./response/auth/sign-up-response.dto";
import GetLoginUserResponseDto from "./response/member/get-login-user.response.dto";
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;

const DOMAIN = 'http://localhost:8080';

const authorization = (accessToken: string) => {
  return {headers: {Authorization: `Bearer ${accessToken}`}}
};

const refresh = (refreshToken : string) => {
  return {headers: {Refresh_Token : `${refreshToken}`}}
}

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

const GET_SIGN_IN_USER_URL = () => `${DOMAIN}/member`
export const getSignInUserRequest = async (accessToken: string) => {
  return await axios.get(GET_SIGN_IN_USER_URL(), authorization(accessToken))
  .then(response => {
    const responseBody: GetLoginUserResponseDto = response.data;
    return responseBody;
  })
  .catch(error => {
    if (!error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
  });
}

const LOGOUT_URL = () => `${DOMAIN}/logout`
export const logoutRequest = async (accessToken:string) =>{
  return await axios.get(LOGOUT_URL(), authorization(accessToken))
  .then(response => {
    const responseBody: GetLoginUserResponseDto = response.data;
    return responseBody;
  })
  .catch(error => {
    if (!error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
  });
}

const REISSUE_URL = () => `${DOMAIN}/reissue`;
export const reissueToken = async (accessToken: string, refreshToken: string) => {
  let reIssueAccessToken = authorization(accessToken);
  let reIssueRefreshToken = refresh(refreshToken);
  const data = {reIssueAccessToken, reIssueRefreshToken};

  return await axios.put(REISSUE_URL(),data)
  .then(response => {
    const responseBody: string = response.data;
    return responseBody;
  })
  .catch(error =>{
    if(!error.response.data) return null;
    const responseBody: ResponseDto = error.response.data;
    return responseBody;
  })

};
