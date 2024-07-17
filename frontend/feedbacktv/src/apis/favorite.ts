import axiosInstance from './common/axiosConfig';
import { API_URL } from './constants/api';
import { GetIsFavoriteResponseDto } from '../types/response/favorite';
import ResponseDto from "../types/response/response.dto";

export const getIsFavorite = async (followeeId: string, followerId: string): Promise<GetIsFavoriteResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.FAVORITE(followeeId, followerId));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};
