export const DOMAIN = 'http://localhost:8080';
export const AUTH = 'auth';
export const BOARD = 'board';
export const API_URL = {
  //Auth
  LOGIN: `${DOMAIN}/${AUTH}/login`,
  SIGNUP: `${DOMAIN}/${AUTH}/signup`,
  LOGOUT: `${DOMAIN}/${AUTH}/logout`,
  REISSUE: `${DOMAIN}/${AUTH}/reissue`,


  MEMBER: `${DOMAIN}/member`,
  CHECK_NICKNAME :(nickname : string) => `${DOMAIN}/member/${nickname}/availability`,
  CHANGE_NICKNAME :() => `${DOMAIN}/member/nickname`,
  CHANGE_NEW_PASSWORD :() => `${DOMAIN}/member/password`,
  CHANGE_PROFILE_IMAGE : (userId : string) => `${DOMAIN}/member/${userId}/profileImage`,

  STREAM: `${DOMAIN}/stream`,

  START_BROADCAST: `${DOMAIN}/broadcast/start`,
  GET_BROADCAST_INFO : `${DOMAIN}/broadcast/info`,
  GET_BROADCASTS_BY_CATEGORY :`${DOMAIN}/broadcast/category`,


  POST_BOARD: `${DOMAIN}/${BOARD}/post`,
  GET_BOARD_DETAIL : `${DOMAIN}/board/detail`,
  GET_BOARD_LIST : (userLoginId :string, category:string, page :number) =>
      `${DOMAIN}/board/${userLoginId}?category=${category}&page=${page}`,


  DONATION_TOP3: (userLoginId: string) => `${DOMAIN}/donation/${userLoginId}/top3fanList`,
  DONATION_TOP20: (userLoginId: string) => `${DOMAIN}/donation/${userLoginId}/top20fanList`,
  STATION: (userLoginId: string) => `${DOMAIN}/station/${userLoginId}`,


  BOARD_CATEGORY_LIST: (userLoginId: string) => `${DOMAIN}/boardcategory/${userLoginId}`,
  FAVORITE: (followeeId: string, followerId: string) => `${DOMAIN}/favorite`,
  STAR_CANDY: `${DOMAIN}/member?starcandy`,

  FILE_UPLOAD: `${DOMAIN}/file/upload`,
  PROFILE_UPLOAD :`${DOMAIN}/file/upload/profile`,
  PAYMENT_PREPARE: `${DOMAIN}/payment/prepare`,
  PAYMENT_VALIDATE: `${DOMAIN}/payment/validate`,
};

export const authorization = (accessToken: string) => ({
  headers: { Authorization: `Bearer ${accessToken}` },
});

export const refresh = (refreshToken: string) => ({
  headers: { Refresh_Token: `${refreshToken}` },
});

export const multipartFormData = {
  headers: { 'Content-Type': 'multipart/form-data' },
};
