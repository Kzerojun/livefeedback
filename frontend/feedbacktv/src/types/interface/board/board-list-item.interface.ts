export default interface BoardListItem {
  boardId: number;
  title : string;
  content: string;
  viewCount: number;
  boardTitleImage : string | null;
  createdAt: string;
}