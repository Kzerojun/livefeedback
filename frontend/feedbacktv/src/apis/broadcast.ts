import {
  BroadcastInfoRequestDto,
  StartBroadcastRequest, StreamingBroadcastInfoRequestDto,
  StreamStatusRequestDto
} from "../types/request/broadcast";
import {API_URL} from "./constants/api";
import axiosInstance from "./common/axiosConfig";
import ResponseDto from "../types/response/response.dto";
import StreamStatusResponseDto from "../types/response/broadcast/stream-status-response.dto";
import {
  BroadcastInfoResponseDto,
  StartBroadcastResponse,
  StreamingBroadcastInfoResponseDto
} from "../types/response/broadcast";
import BroadcastsInfoResponseDto from "../types/response/broadcast/broadcasts-info.response.dto";

export const checkStreamStatus = async (request: StreamStatusRequestDto): Promise<StreamStatusResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.STREAM, {
      params: request,
    });
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const startBroadCast = async (request: StartBroadcastRequest): Promise<StartBroadcastResponse | ResponseDto | null> => {
  try {
    const response = await axiosInstance.post<StartBroadcastResponse>(API_URL.START_BROADCAST, request);
    return response.data;
  } catch (error: any) {
    if (error.response) {
      return error.response.data as ResponseDto;
    }
    return null;
  }
};

export const getBroadcastInfo = async (request : number) : Promise<StreamingBroadcastInfoResponseDto|ResponseDto|null> =>{
  try {
    const response = await axiosInstance.get(API_URL.GET_BROADCAST_INFO,{
      params: {broadcastId : request},
    })
    return response.data;
  } catch (error: any) {
    if (error.response) {
      return error.response.data as ResponseDto;
    }
    return null;
  }
}


export const getBroadcastsByCategory = async (request : string):Promise<BroadcastsInfoResponseDto|ResponseDto|null> =>{
  try {
    const response = await axiosInstance.get(API_URL.GET_BROADCASTS_BY_CATEGORY, {
      params: { category : request },
    });
    return response.data;
  } catch (error: any) {
    if (error.response) {
      return error.response.data as ResponseDto;
    }
    return null;
  }
}

