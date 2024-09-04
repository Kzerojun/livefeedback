export default interface ChatMessageRequest {
  userLoginId: string;
  userNickname: string;
  content: string;
  streamerId: string;
  broadcastId: string;
}