import axiosInstance from './common/axiosConfig';
import { API_URL, authorization } from './constants/api';
import {LoginRequestDto, SignUpRequestDto,} from '../types/request/auth';
import { LoginResponseDto, SignUpResponseDto } from '../types/response/auth';
import ResponseDto from "../types/response/response.dto";

export const logInRequest = async (requestBody: LoginRequestDto): Promise<LoginResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.post(API_URL.LOGIN, requestBody);
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const signUpRequest = async (requestBody: SignUpRequestDto): Promise<SignUpResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.post(API_URL.SIGNUP, requestBody);
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const logoutRequest = async (accessToken: string): Promise<ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.LOGOUT, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const reissueToken = async (accessToken: string, refreshToken: string): Promise<string | ResponseDto | null> => {
  const data = {
    accessToken,
    refreshToken,
  };
  try {
    const response = await axiosInstance.put(API_URL.REISSUE, data);
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};
