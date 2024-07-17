import axiosInstance from './common/axiosConfig';
import {API_URL, authorization} from './constants/api';
import {
  ChangeNicknameResponseDto,
  CheckNicknameAvailabilityResponseDto,
  GetLoginUserResponseDto,
  GetMemberResponseDto,
  GetStarCandyResponseDto
} from "../types/response/member";
import ResponseDto from "../types/response/response.dto";
import ChangePasswordResponseDto from "../types/response/member/change-password.response.dto";
import ChangePasswordRequestDto from "../types/request/member/change-password.request.dto";
import ChangeProfileImageResponseDto
  from "../types/response/member/change-profile-image.response.dto";
import ChangeProfileImageRequestDto from "../types/request/member/change-profile-image.request.dto";
import ChangeNicknameRequestDto from "../types/request/member/change-nickname.request.dto";

export const getSignInUserRequest = async (accessToken: string): Promise<GetLoginUserResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.MEMBER, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const getUserRequest = async (userLoginId: string): Promise<GetMemberResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(`${API_URL.MEMBER}/${userLoginId}`);
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const getMemberCandyAmount = async (accessToken: string): Promise<GetStarCandyResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.STAR_CANDY, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
};

export const checkNicknameAvailability = async (nickname: string,accessToken:string): Promise<CheckNicknameAvailabilityResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.get(API_URL.CHECK_NICKNAME(nickname),authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
}

export const changeNickname = async (request : ChangeNicknameRequestDto) : Promise<ChangeNicknameResponseDto | ResponseDto | null> => {
  const{newNickname,accessToken} = request
  try{
    const response = await axiosInstance.patch(API_URL.CHANGE_NICKNAME(), {newNickname}, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
}


export const changeNewPassword = async (request: ChangePasswordRequestDto, accessToken: string): Promise<ChangePasswordResponseDto | ResponseDto | null> => {
  try {
    const response = await axiosInstance.patch(API_URL.CHANGE_NEW_PASSWORD(), request, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
}

export const changeProfileImage = async (request : ChangeProfileImageRequestDto, accessToken : string) : Promise<ChangeProfileImageResponseDto | ResponseDto | null> => {
  const{userLoginId,image} = request
  try {
    const response = await axiosInstance.patch(API_URL.CHANGE_PROFILE_IMAGE(userLoginId), {
      image
    }, authorization(accessToken));
    return response.data;
  } catch (error) {
    return error as ResponseDto;
  }
}