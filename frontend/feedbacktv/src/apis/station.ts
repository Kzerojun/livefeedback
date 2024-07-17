import axiosInstance from './common/axiosConfig';
import { API_URL } from './constants/api';
import {GetStationResponseDto} from "../types/response/station";
import ResponseDto from "../types/response/response.dto";

export const getStationRequest = async (userLoginId: string): Promise<GetStationResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.STATION(userLoginId));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};
