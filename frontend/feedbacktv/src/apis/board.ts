import axiosInstance from './common/axiosConfig';
import {API_URL, authorization} from './constants/api';
import {PostBoardRequestDto} from '../types/request/board';
import ResponseDto from "../types/response/response.dto";
import {GetBoardListResponseDto, PostBoardResponseDto} from "../types/response/board";
import GetBoardDetailResponseDto from "../types/response/board/get-board-detail.response.dto";

export const getBoardCategoryListRequest = async (userLoginId: string): Promise<GetBoardListResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.BOARD_CATEGORY_LIST(userLoginId));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const getBoardList = async (userLoginId: string, category: string,page : number): Promise<GetBoardListResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.GET_BOARD_LIST(userLoginId, category,page));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const getBoardDetail = async (boardId: number): Promise<GetBoardDetailResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.GET_BOARD_DETAIL, {
      params: {boardId : boardId},
    });
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
}

export const postBoardRequest = async (requestBody: PostBoardRequestDto, accessToken: string): Promise<PostBoardResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.post(API_URL.POST_BOARD, requestBody, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};
