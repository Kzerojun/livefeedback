import axiosInstance from './common/axiosConfig';
import { API_URL } from './constants/api';
import {GetFanListResponseDto} from "../types/response/donation";
import ResponseDto from "../types/response/response.dto";

export const getTop3FanListRequest = async (userLoginId: string): Promise<GetFanListResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.DONATION_TOP3(userLoginId));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const getTop20FanListRequest = async (userLoginId: string): Promise<GetFanListResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.DONATION_TOP20(userLoginId));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};
