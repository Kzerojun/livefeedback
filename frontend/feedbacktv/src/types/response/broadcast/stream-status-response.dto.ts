import ResponseDto from "../response.dto";

export default interface StreamStatusResponseDto extends ResponseDto{
  streamStatus : string;
}