import ResponseDto from "../response.dto";

export default interface LoginResponseDto extends ResponseDto {

  grantType: string;

  accessToken: string;

  refreshToken: string;

  accessTokenExpirationTime : number;

  refreshTokenExpirationTime: number;

}