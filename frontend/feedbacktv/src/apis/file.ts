import axiosInstance from './common/axiosConfig';
import { API_URL, multipartFormData } from './constants/api';

export const fileUploadRequest = async (data: FormData): Promise<string | null> => {
  try {
    const response = await axiosInstance.post(API_URL.FILE_UPLOAD, data, multipartFormData);
    return response.data;
  } catch (error) {
    return null;
  }
};

export const profileImageUpload = async (data :FormData) : Promise<string | null> => {
  try {
    const response = await axiosInstance.post(API_URL.PROFILE_UPLOAD, data, multipartFormData);
    return response.data;
  } catch (error) {
    return null;
  }
}